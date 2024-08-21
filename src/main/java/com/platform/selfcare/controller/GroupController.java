package com.platform.selfcare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.platform.selfcare.dto.GroupDto;
import com.platform.selfcare.dto.PostingDto;
import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.Group;
import com.platform.selfcare.entity.Posting;
import com.platform.selfcare.service.CustomUserDetails;
import com.platform.selfcare.service.IGroupService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/group")
public class GroupController extends ModelAttributes {
	
	@Autowired
	private IGroupService groupService;
	
	@GetMapping("/")
	public String group(Model model, HttpServletRequest request) {
		
		return "group";
	}
	
	@GetMapping("/{groupId}")
	public String groupById(Model model, HttpServletRequest request, @PathVariable("groupId") final Long groupId) {
		Optional<Group> group = this.groupService.getGroupById(groupId);
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		
		if (userDetails != null && group.isPresent() && group.get().isAuthorized(userDetails.getUser())) {
			model.addAttribute("group", group.get());
			List<Posting> postings = this.groupService.findByGroupAndNoParent(group.get());
			
			model.addAttribute("postings", postings);
		}
		
		model.addAttribute("postingDto", new PostingDto());
		
		return "group";
	}
	
	@GetMapping("/new")
	public String groupNew(Model model, HttpServletRequest request) {
		if (model.getAttribute("newGroup") == null) {
			model.addAttribute("newGroup", new GroupDto());
		}
		
		return "newGroup";
	}
	
	@PostMapping("/createNew")
	public String createNewGroup(final HttpServletRequest request, 
			final @Valid @ModelAttribute("newGroup") GroupDto groupDto, 
			BindingResult result, 
			RedirectAttributes redirectAttr, 
			Model model) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("newGroup", groupDto);
			return "newGroup";
		}
		
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		
		if (userDetails != null) {
			Group group = this.groupService.createNewGroup(userDetails.getUser(), groupDto.getTitle(), groupDto.getDescription());
			if (group != null) {
				redirectAttr.asMap().clear();
				model.addAttribute("message", "Gruppe erfolgreich angelegt!");
				model.addAttribute("newGroup", new GroupDto());
			}
		}
		
		return "newGroup";
	}
	
	@GetMapping("/register/{groupId}")
	public String register(Model model, HttpServletRequest request, @PathVariable("groupId") final Long groupId) {
		Optional<Group> group = this.groupService.getGroupById(groupId);
		if (group.isPresent()) {
			GroupDto dto = new GroupDto();
			dto.setTitle(group.get().getTitle());
			dto.setGroupId(group.get().getId());
			model.addAttribute("groupDto", dto);
		}
		return "registerByGroup";
	}
	
	@PostMapping("/sendMessageToCreator")
	public String sendMessageToCreator(final HttpServletRequest request, 
			final @Valid @ModelAttribute("groupDto") GroupDto groupDto, 
			BindingResult result, 
			RedirectAttributes redirectAttr, 
			Model model) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("groupDto", groupDto);
			return "registerByGroup";
		}
		
		Optional<Group> group = this.groupService.getGroupById(groupDto.getGroupId());
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		
		if (userDetails != null 
				&& group.isPresent() 
				&& !group.get().isCandidate(userDetails.getUser())
				&& !group.get().isMember(userDetails.getUser())
				&& !group.get().isCreator(userDetails.getUser())
				&& !group.get().isBlacklisted(userDetails.getUser())) {
			
			Candidate candidate = new Candidate();
			candidate.setUser(userDetails.getUser());
			candidate.setGroup(group.get());
			candidate.setMessage(groupDto.getMsgToCreator());
			
			boolean status = this.groupService.createCandidate(candidate);
			if (!status) {
				model.addAttribute("globalError", "Kandidat konnte nicht erstellt werden.");
				redirectAttr.addFlashAttribute("groupDto", groupDto);
			}
			else {
				model.addAttribute("message", "Kandidatur erfolgreich!");
				redirectAttr.addFlashAttribute("groupDto", new GroupDto());
				model.addAttribute("groupDto", new GroupDto());
			}
			
			redirectAttr.asMap().clear();
		}
		else {
			model.addAttribute("globalError", "Ein unbekannter Fehler ist aufgetreten.");
			redirectAttr.addFlashAttribute("groupDto", groupDto);
		}
		
		return "registerByGroup";
	}
	
	@PostMapping("/{groupId}/posting")
	public String sendPosting(final HttpServletRequest request, 
			final @Valid @ModelAttribute("postingDto") PostingDto postingDto, @PathVariable("groupId") final Long groupId,
			BindingResult result, 
			RedirectAttributes redirectAttr, 
			Model model) {
		
		Optional<Group> group = this.groupService.getGroupById(groupId);
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		
		if (userDetails != null && group.isPresent() && group.get().isAuthorized(userDetails.getUser())) {
			
			if (result.hasErrors()) {
				redirectAttr.addFlashAttribute("postingDto", postingDto);
				return groupById(model, request, groupId);
			}
			
			Posting posting = new Posting(userDetails.getUser(), group.get(), postingDto.getText());
			
			if (postingDto.getPostingId() != null) {
				// is answer
				Optional<Posting> parentPosting = this.groupService.findPostingById(postingDto.getPostingId());
				if (parentPosting.isPresent()) {
					posting.setReplied(parentPosting.get());
				}
				else {
					model.addAttribute("error", "Etwas ist schiefgelaufen bei der Antwort auf einen Beitrag");
				}
			}
			else {
				// is new posting
			}
			
			this.groupService.savePosting(posting);
		}
		
		redirectAttr.asMap().clear();
		
		return groupById(model, request, groupId);
	}
}

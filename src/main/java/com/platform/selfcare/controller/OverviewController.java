package com.platform.selfcare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.platform.selfcare.entity.Candidate;
import com.platform.selfcare.entity.Group;
import com.platform.selfcare.service.CustomUserDetails;
import com.platform.selfcare.service.IGroupService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OverviewController extends ModelAttributes {

	@Autowired
	private IGroupService groupService;
	
	@GetMapping("/overview")
	public String overview(Model model, HttpServletRequest request) {
		
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		if (userDetails != null) {
			List<Group> groups = this.groupService.getAll();
			// set markers to check group authorities
			groups.stream().forEach(g -> g.setAuthorized(g.isAuthorized(userDetails.getUser())));
			groups.stream().forEach(g -> g.setCandidate(g.isCandidate(userDetails.getUser())));
			groups.stream().forEach(g -> g.setMember(g.isMember(userDetails.getUser())));
			groups.stream().forEach(g -> g.setOwner(g.isCreator(userDetails.getUser())));
			groups.stream().forEach(g -> g.setBlacklisted(g.isBlacklisted(userDetails.getUser())));
			model.addAttribute("groups", groups);
			
			List<Candidate> candidates = this.groupService.getAllCandidatesByCreator(userDetails.getUser());
			if (candidates != null && candidates.size() > 0) {
				model.addAttribute("candidates", candidates);
			}
		}
		
		return "overview";
	}
	
	@PostMapping("/feedbackCandidature")
	public String feedbackCandidature(Model model, HttpServletRequest request, Candidate candidate, final String feedback) {
		
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		Optional<Candidate> foundCandidate = this.groupService.findByCandidateId(candidate.getId());
		
		if (userDetails != null 
				&& foundCandidate.isPresent() 
				&& foundCandidate.get().getGroup().isCreator(userDetails.getUser())) {
			
			boolean feedbackStatus = feedback.equals("accept") ? true : false;

			boolean status = this.groupService.changeCandidateStatus(foundCandidate.get(), feedbackStatus);
			if (status) {
				this.groupService.deleteCandidate(foundCandidate.get());
			}
			
			if (status && feedbackStatus) {
				model.addAttribute("message", "Kandidat ist ab jetzt ein Mitglied.");
			}
			else {
				model.addAttribute("message", "Kandidatur abgelehnt.");
			}
		}
		
		return overview(model, request);
	}
}

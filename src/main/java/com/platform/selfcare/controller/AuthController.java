package com.platform.selfcare.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.platform.selfcare.dto.ChangePasswordDto;
import com.platform.selfcare.dto.EmailDto;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.entity.VerificationToken;
import com.platform.selfcare.enums.TokenStatus;
import com.platform.selfcare.enums.TokenType;
import com.platform.selfcare.event.OnPasswordForgottenEvent;
import com.platform.selfcare.service.CustomUserDetails;
import com.platform.selfcare.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AuthController extends ModelAttributes {

	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
	IUserService userService;
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("logout")
	public String logout(final HttpServletRequest request) {
		return "logout";
	}
	
	@GetMapping("/forgottenPassword")
	public String forgottenPassword(Model model) {
		if (model.getAttribute("emailCheck") == null) {
			model.addAttribute("emailCheck", new EmailDto());
		}
		return "forgottenPassword";
	}
	
	@PostMapping("/sendPasswordForgottenEmail")
	public String sendPasswordForgottenEmail(final HttpServletRequest request, 
			final @Valid @ModelAttribute("emailCheck") EmailDto emailDto, 
			BindingResult result, 
			RedirectAttributes redirectAttr, 
			Model model) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("emailCheck", emailDto);
			return "forgottenPassword";
		}
		
		Optional<User> existingUser = this.userService.findUserByEmail(emailDto.getEmail());
		
		if (existingUser.isPresent() && existingUser.get().isEnabled()) {
			this.eventPublisher.publishEvent(new OnPasswordForgottenEvent(existingUser.get(), request.getLocale(), "https://" + request.getServerName() + request.getContextPath()));
		} 
		else {
			// Do nothing for more security -> otherwise Hacker can use rainbow attack to search for existing emails
		}
		
		redirectAttr.asMap().clear();
		
		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		if (userDetails != null && existingUser.isPresent()) {
			// logout user 
			request.getSession().removeAttribute("user");
			request.getSession().invalidate();
		}
		
		try {
			return "redirect:/login?message=" + URLEncoder.encode("Bitte prüfen Sie Ihre E-Mail nach einem Bestätigungslink", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
		return "login";
	}
	
	@GetMapping("/changeForgottenPassword")
	public String changePassword(final HttpServletRequest request, Model model, @RequestParam("token") final String token) {
		final VerificationToken verifiedToken = userService.validateVerificationToken(token, TokenType.PASSWORD_TOKEN, false);
		
		if (model.getAttribute("newPassword") == null) {
			ChangePasswordDto cpwDto = new ChangePasswordDto();
			cpwDto.setToken(token);
			model.addAttribute("newPassword", cpwDto);
		}
		
		if (verifiedToken.getStatus().equals(TokenStatus.TOKEN_VALID)) {
			return "changeForgottenPassword";
		}
		
		return "error";
	}
	
	@PostMapping("/saveForgottenPassword")
	public String saveForgottenPassword(final HttpServletRequest request, 
			final @Valid @ModelAttribute("newPassword") ChangePasswordDto passwordDto, 
			BindingResult result, 
			RedirectAttributes redirectAttr, 
			Model model) {
		
		if (result.hasErrors()) {
			if (result.hasGlobalErrors()) {
				model.addAttribute("globalError", result.getGlobalError().getDefaultMessage());
			}
			
			redirectAttr.addFlashAttribute("newPassword", passwordDto);
			return "changeForgottenPassword";
		}
		
		final VerificationToken verifiedToken = userService.validateVerificationToken(passwordDto.getToken(), TokenType.PASSWORD_TOKEN, true);
		
		if (verifiedToken.getUser() != null && verifiedToken.getStatus().equals(TokenStatus.TOKEN_VALID)) {
			this.userService.changePassword(verifiedToken.getUser(), passwordDto.getPassword());
		}
		else {
			model.addAttribute("globalError", "Bei der Passwortänderung ist etwas schiefgelaufen. Bitte versuchen Sie es mit einer neuen Beantragung.");
			return "changeForgottenPassword";
		}
		
		redirectAttr.asMap().clear();
		
		try {
			return "redirect:/login?message=" + URLEncoder.encode("Ihr Passwort ist erfolgreich geändert. Sie können sich nun damit einloggen.", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
		return "login";
	}
	
	@GetMapping("/updateOldPassword")
	public String updateOldPassword(Model model) {
		if (model.getAttribute("updatePassword") == null) {
			model.addAttribute("updatePassword", new ChangePasswordDto());
		}
		return "updateOldPassword";
	}
	
	@PostMapping("/saveUpdatedPassword")
	public String saveUpdatedPassword(final HttpServletRequest request, 
			final @Valid @ModelAttribute("updatePassword") ChangePasswordDto passwordDto, 
			BindingResult result, 
			RedirectAttributes redirectAttr, 
			Model model) {

		CustomUserDetails userDetails = (CustomUserDetails) request.getSession().getAttribute("user");
		if (userDetails == null || !userDetails.isEnabled()) {
			return "error";
		}
		
		if (result.hasErrors()) {
			if (result.hasGlobalErrors()) {
				model.addAttribute("globalError", result.getGlobalError().getDefaultMessage());
			}
			
			redirectAttr.addFlashAttribute("updatePassword", passwordDto);
			return "updateOldPassword";
		}
		
		if (!this.userService.checkPasswordMatched(userDetails.getUser(), passwordDto.getOldPassword())) {
			result.rejectValue("oldPassword", "oldPassword", "altes Passwort stimmt nicht überein");
			redirectAttr.addFlashAttribute("updatePassword", passwordDto);
			return "updateOldPassword";
		}
		
		this.userService.changePassword(userDetails.getUser(), passwordDto.getPassword());
		
		redirectAttr.asMap().clear();
		// reset form data too
		model.addAttribute("updatePassword", new ChangePasswordDto());
		model.addAttribute("success", "Das Passwort wurde erfolgreich geändert");
		
		return "updateOldPassword";
	}
}

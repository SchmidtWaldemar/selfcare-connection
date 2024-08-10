package com.platform.selfcare.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.platform.selfcare.dto.RegisterUserDto;
import com.platform.selfcare.entity.User;
import com.platform.selfcare.entity.VerificationToken;
import com.platform.selfcare.enums.TokenStatus;
import com.platform.selfcare.enums.TokenType;
import com.platform.selfcare.event.OnRegistrationCompleteEvent;
import com.platform.selfcare.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
	IUserService userService;
	
	private final static String REDIRECT = "register"; 
	
	@GetMapping("/")
	public String register(Model model) {
		if (model.getAttribute("newAccount") == null) {
			model.addAttribute("newAccount", new RegisterUserDto());
		}
		return REDIRECT;
	}
	
	@PostMapping("/new")
	public String registerNewUser(final HttpServletRequest request, 
			final @Valid @ModelAttribute("newAccount") RegisterUserDto userDto, 
			BindingResult result, 
			RedirectAttributes redirectAttr, 
			Model model) {
		
		if (result.hasErrors()) {
			redirectAttr.addFlashAttribute("newAccount", userDto);
			return REDIRECT;
		}
		
		final User registered = this.userService.registerNewUser(userDto);
		this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), "https://" + request.getServerName() + request.getContextPath()));
		
		redirectAttr.asMap().clear();
		
		try {
			return "redirect:/login?message=" + URLEncoder.encode("Sie haben sich erfolgreich registriert. Bitte prüfen Sie als nächstes Ihre E-Mail", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
		return "login";
	}
	
	@GetMapping("/registrationConfirm")
	public String confirmRegistration(final HttpServletRequest request, Model model, @RequestParam("token") final String token) {
		final VerificationToken verifiedToken = userService.validateVerificationToken(token, TokenType.REGISTER_TOKEN);
		
		if (verifiedToken.getStatus().equals(TokenStatus.TOKEN_VALID)) {
			final User user = verifiedToken.getUser();
			
			if (user != null) {
				try {
					return "redirect:/login?message=" + URLEncoder.encode("Ihre Registrierung ist abgeschlossen. Sie können sich nun anmelden.", "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// do nothing
				}
				return "login";
			}
		}
		
		return "error";
	}
}

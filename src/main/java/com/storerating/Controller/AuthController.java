package com.storerating.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.storerating.Entity.User;
import com.storerating.Service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/signup")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "signup";
		}

		if (userService.existsByEmail(user.getEmail())) {
			model.addAttribute("emailError", "Email already exists");
			return "signup";
		}

		user.setRole(User.Role.NORMAL_USER);
		userService.save(user);
		return "redirect:/login?success";
	}

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication) {
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"))) {
			return "redirect:/admin/dashboard";
		} else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STORE_OWNER"))) {
			return "redirect:/store-owner/dashboard";
		} else {
			return "redirect:/user/dashboard";
		}
	}

}

package com.itbcafrica.cmsecommercecar.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itbcafrica.cmsecommercecar.models.data.User;
import com.itbcafrica.cmsecommercecar.models.data.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping
	public String register(User user) {
		return "register";
	}

	@PostMapping
	public String postRegister(@Valid User user, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "register";
		}
		if (!user.getPassword().equals(user.getConfirmpassword())) {
			model.addAttribute("passwordMatchProblem", "passwords do not match!");
			return "register";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);

		return "redirect:/login";
	}
}

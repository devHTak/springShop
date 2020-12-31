package com.shop.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	@GetMapping("/")
	public String main() {
		return "index";
	}
	
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
}

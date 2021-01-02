package com.shop.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.account.Account;
import com.shop.account.CurrentUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	@GetMapping("/")
	public String main(@CurrentUser Account account, Model model) {
		model.addAttribute("account", account);
		return "index";
	}
	
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
}

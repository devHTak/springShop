package com.shop.account;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shop.account.form.AccountSignUpForm;
import com.shop.account.form.AccountSignUpFormValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccountController {
	
	private final AccountService accountService;
	private final AccountSignUpFormValidator accountSignUpFormValidator;
	
	@InitBinder
	public void dataBinder(DataBinder binder) {
		binder.addValidators(accountSignUpFormValidator);
	}
	
	@GetMapping("/accounts")
	public String signUp(Model model) {
		model.addAttribute("accountSignUpForm", new AccountSignUpForm());
		return "account/sign-up";
	}

	@PostMapping("/accounts")
	public String doSignUp(@Valid @ModelAttribute AccountSignUpForm accountSignUpForm, Errors errors) {
		if(errors.hasErrors()) {
			return "account/sign-up";
		}
		Account account = accountService.signUp(accountSignUpForm);
		accountService.login(account);
		return "redirect:/";
	}
	
	@GetMapping("/accounts/{id}")
	public String getProfile(@CurrentUser Account account, @PathVariable Long id, Model model) {
		Account byId = accountService.findById(id);
		
		model.addAttribute("account", byId);
		
		return "account/profile";
	}
	
	@PatchMapping("/accounts/{id}")
	public String updateProfile(@CurrentUser Account account, @PathVariable Long id, @ModelAttribute Account newAccount) {
		Account byId = accountService.updateById(id, newAccount);
		
		return "account/profile";
	}
}

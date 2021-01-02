package com.shop.account;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.account.form.AccountSignUpForm;
import com.shop.account.form.AccountSignUpFormValidator;
import com.shop.account.form.ProfileForm;
import com.shop.account.form.ProfileFormValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccountController {
	
	private final AccountService accountService;
	private final AccountSignUpFormValidator accountSignUpFormValidator;
	private final ProfileFormValidator profileFormValidator;
	
	@InitBinder("accountSignUpForm")
	public void signUpFormDataBinder(WebDataBinder binder) {
		binder.addValidators(accountSignUpFormValidator);
	}
	
	@InitBinder("profileForm")
	public void  profileFormDataBinder(WebDataBinder binder) {
		binder.addValidators(profileFormValidator);
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
		model.addAttribute("account", account);
		model.addAttribute("profileForm", new ProfileForm());
		
		return "account/profile";
	}
	
	@PostMapping("/accounts/{id}")
	public String updateProfile(@CurrentUser Account account, @PathVariable Long id, @Valid @ModelAttribute ProfileForm profileForm, Errors errors, RedirectAttributes attribute, Model model) {
		if(errors.hasErrors()) {
			model.addAttribute("account", account);
			return "account/profile";
		}
		
		Account byId = accountService.updateById(account, profileForm);
		attribute.addFlashAttribute("successMessage", "프로필 수정을 완료하였습니다.");
		return "redirect:/accounts/" + byId.getId();
	}
}

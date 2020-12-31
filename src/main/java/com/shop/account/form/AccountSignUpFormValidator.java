package com.shop.account.form;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.shop.account.AccountService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountSignUpFormValidator implements Validator {
	
	private final AccountService accountService;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return AccountSignUpForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		AccountSignUpForm accountSignUpForm = (AccountSignUpForm)target;
		
		if(accountService.existsByEmail(accountSignUpForm.getEmail())) {
			errors.rejectValue("email", "email", "Email is duplicated.");
		}
		if(accountService.existsByNickname(accountSignUpForm.getNickname())) {
			errors.rejectValue("nickname", "nickname", "Nickname is duplicated.");
		}
	}
	
	

}

package com.shop.account.form;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.shop.account.AccountService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileFormValidator implements Validator {
	
	private final AccountService accountService;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return ProfileForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		ProfileForm profileForm = (ProfileForm)target;
		
		if(accountService.countByEmail(profileForm.getEmail()) > 1) {
			errors.rejectValue("email", "email", "Email is duplicated.");
		}
		
		if(accountService.countByNickname(profileForm.getNickname()) > 1) {
			errors.rejectValue("nickname", "nickname", "Nickname is duplicated.");
		}
	}
	
	

}

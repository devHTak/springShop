package com.shop.account.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.shop.account.AccountCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AccountSignUpForm {
	
	@NotBlank
	@Length(min = 2, max = 20)
	private String nickname;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Length(min = 8, max = 20)
	private String password;
	
	private AccountCode accountCode;
}

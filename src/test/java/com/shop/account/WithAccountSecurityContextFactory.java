package com.shop.account;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.shop.account.form.AccountSignUpForm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockAccount>{
	
	private final AccountService accountService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public SecurityContext createSecurityContext(WithMockAccount annotation) {
		// TODO Auto-generated method stub
		String nickname = annotation.nickname();
		String role = annotation.role();
		
		AccountSignUpForm form = new AccountSignUpForm();
		form.setAccountCode(role.equals("ROLE_VENDOR") ? AccountCode.VENDOR : AccountCode.ACCOUNT);
		form.setEmail("test@test.com");
		form.setNickname(nickname);
		form.setPassword(passwordEncoder.encode("test"));
		
		Account account = accountService.signUp(form);
		
		UserDetails userDetails = accountService.loadUserByUsername(account.getNickname());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		
		return context;		
	}

	
}

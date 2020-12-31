package com.shop.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserAccount extends User {
	
	private Account account;
	
	public UserAccount(Account account) {
		super(account.getNickname(), account.getPassword(), authorities(account));
		this.account = account;
	}
	
	public static List<GrantedAuthority> authorities(Account account) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(account.getRole()));
		
		return authorities;
	}
	
	public Account getAccount() {
		return this.account;
	}

}

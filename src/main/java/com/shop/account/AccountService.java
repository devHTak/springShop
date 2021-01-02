package com.shop.account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.account.form.AccountSignUpForm;
import com.shop.account.form.ProfileForm;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
	
	private final PasswordEncoder passwordEncoder;
	private final AccountRepository accountRepository;
	
	public boolean existsByNickname(String nickname) {
		return accountRepository.existsByNickname(nickname);
	}
	
	public boolean existsByEmail(String email) {
		return accountRepository.existsByEmail(email);
	}
	
	public int countByNickname(String nickname) {
		return accountRepository.countByNickname(nickname);
	}
	
	public int countByEmail(String email) {
		return accountRepository.countByEmail(email);
	}
	
	public Account signUp(AccountSignUpForm accountSignUpForm) {
		Account account = Account.builder().nickname(accountSignUpForm.getNickname())
				.email(accountSignUpForm.getEmail())
				.password(passwordEncoder.encode(accountSignUpForm.getPassword()))
				.role(accountSignUpForm.getAccountCode().equals(AccountCode.ACCOUNT) ? "ROLE_ACCOUNT" : "ROLE_VENDOR")
				.registerTime(LocalDateTime.now()).build();
		
		return accountRepository.save(account);
	}
	
	public void login(Account account) {
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority(account.getRole()));
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserAccount(account), account.getPassword(), roles);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		if(!accountRepository.existsByEmail(username) && !accountRepository.existsByNickname(username)) {
			throw new UsernameNotFoundException(username +" is not exists.");
		}
		
		Account account = accountRepository.findByNickname(username).orElseGet(()->{
			return accountRepository.findByEmail(username).get();
		});
		
		return new UserAccount(account);
	}
	
	public Account findByNickname(String nickname) {
		return accountRepository.findByNickname(nickname).orElseThrow(()-> {
			throw new IllegalArgumentException(nickname + " is not found");
		});
	}
	
	public Account findById(Long id) {
		return accountRepository.findById(id).orElseThrow(()-> {
			throw new IllegalArgumentException(String.format("Account[Id= %d] is not found", id));
		});
	}
	
	public Account updateById(Account account, ProfileForm profileForm) {		
		account.setAddress(profileForm.getAddress());
		account.setEmail(profileForm.getEmail());
		account.setName(profileForm.getName());
		account.setNickname(profileForm.getNickname());
		account.setNumber(profileForm.getNumber());
		account.setPassword(passwordEncoder.encode(profileForm.getPassword()));
		account.setRegistrationNumber(profileForm.getRegistrationNumber());
		
		return accountRepository.save(account);
	}

}

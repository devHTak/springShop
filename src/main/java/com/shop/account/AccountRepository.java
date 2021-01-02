package com.shop.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	public Optional<Account> findByEmail(String email);
	
	public Optional<Account> findByNickname(String nickname);
	
	public boolean existsByNickname(String nickname);
	
	public boolean existsByEmail(String email);
	
	public int countByNickname(String nickname);
	
	public int countByEmail(String email);	

}

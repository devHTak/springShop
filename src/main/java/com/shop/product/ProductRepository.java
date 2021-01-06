package com.shop.product;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.account.Account;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	public Set<Product> findByAccount(Account account);
	
	public Set<Product> findByNameContainingOrDescriptionContaining(String name, String description);

	public Product findByName(String name);
	
	public Set<Product> findTop6ByOrderBySellCountDesc();
	
	public Set<Product> findTop6ByOrderByRegisterDateDesc();
}

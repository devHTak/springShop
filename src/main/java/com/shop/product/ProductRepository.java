package com.shop.product;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.account.Account;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	public Page<Product> findByAccount(Account account, Pageable pageable);
	
	public Page<Product> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);

	public Product findByName(String name);
	
	public Set<Product> findTop6ByOrderBySellCountDesc();
	
	public Set<Product> findTop6ByOrderByRegisterDateDesc();
}

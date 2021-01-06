package com.shop.orders;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.account.Account;

public interface OrdersRepository extends JpaRepository<Orders, Long>{
	
	public Set<Orders> findByCustomer(Account account);
	
	public Page<Orders> findByCustomerAndStatus(Account account, OrdersStatus status, Pageable pageable);
		
	public Page<Orders> findByCustomerAndStatusNotIn(Account account, List<OrdersStatus> status, Pageable pageable);
	
	public Page<Orders> findByVendor(Account account, Pageable pageable);
}

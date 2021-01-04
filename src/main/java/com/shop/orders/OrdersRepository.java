package com.shop.orders;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.account.Account;

public interface OrdersRepository extends JpaRepository<Orders, Long>{
	
	public Set<Orders> findByCustomer(Account account);
	
	public Set<Orders> findByCustomerAndStatus(Account account, OrdersStatus status, Sort sort);
		
	public Set<Orders> findByCustomerAndStatusNotIn(Account account, List<OrdersStatus> status, Sort sort);
	
	public Set<Orders> findByVendor(Account account, Sort sort);
}

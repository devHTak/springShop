package com.shop.orders;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.shop.account.Account;
import com.shop.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Orders {

	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Account customer;
	
	@ManyToOne
	private Account vendor;
	
	private int quantity;
	private int price;
	private int totalPrice;
	
	@Enumerated(EnumType.STRING)
	private OrdersStatus status;
	
	private LocalDateTime orderDate;
}

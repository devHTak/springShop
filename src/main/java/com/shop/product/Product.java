package com.shop.product;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.shop.account.Account;

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
public class Product {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	private Account account;
	
	private String name;
	@Lob
	private String description;
	private int price;
	private int stock;
	private int sellCount;
	private LocalDateTime registerDate;

}

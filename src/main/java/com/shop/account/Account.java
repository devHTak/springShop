package com.shop.account;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.shop.orders.Orders;
import com.shop.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {
	
	@Id @GeneratedValue
	private Long id;
	
	@NotBlank
	private String nickname;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
		
	private String name;
	private String number;
	
	@Embedded
	private Address address;
	private LocalDateTime registerTime;
	
	private String role;
	
	private String registrationNumber;
	
	@OneToMany(mappedBy = "account")
	@Default
	private Set<Product> products = new HashSet<>();
	
	@OneToMany(mappedBy = "customer")
	@Default
	private Set<Orders> orders = new HashSet<>();
	
	@OneToMany(mappedBy = "vendor")
	@Default
	private Set<Orders> sellProducts = new HashSet<>();
	
	public void addProduct(Product product) {
		if(!products.contains(product))
			products.add(product);
		product.setAccount(this);
	}
	
	public void deleteProduct(Product product) {
		if(products.contains(product))
			products.remove(product);
		product.setAccount(null);
	}
	
	public void addOrder(Orders order) {
		if(!orders.contains(order))
			orders.add(order);
		order.setCustomer(this);
	}
	
	public void deleteOrder(Orders order) {
		if(orders.contains(order))
			orders.remove(order);
		order.setCustomer(null);
	}
	
	public void addSellProduct(Orders sellProduct) {
		if(!sellProducts.contains(sellProduct))
			sellProducts.add(sellProduct);
		sellProduct.setVendor(this);
	}
	
	public void deleteSellProduct(Orders sellProduct) {
		if(sellProducts.contains(sellProduct))
			sellProducts.remove(sellProduct);
		sellProduct.setVendor(null);
	}
	
}

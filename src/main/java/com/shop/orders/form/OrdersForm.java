package com.shop.orders.form;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OrdersForm {
	
	@Min(1)
	private int quantity;
	
	@Min(1)
	private int price;

	private int stock;
}

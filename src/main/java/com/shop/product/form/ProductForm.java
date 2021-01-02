package com.shop.product.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductForm {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@Min(1)
	private int price;
	
	@Min(1)
	private int stock;
}

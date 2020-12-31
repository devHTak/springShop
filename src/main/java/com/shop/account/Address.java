package com.shop.account;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Address {
	
	private String zipCode;
	
	private String address;

}

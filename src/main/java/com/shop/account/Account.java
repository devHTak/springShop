package com.shop.account;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

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
	
}

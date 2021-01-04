package com.shop.orders.form;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrdersFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return OrdersForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		OrdersForm ordersForm = (OrdersForm)target;
		
		if(ordersForm.getQuantity() <= 0) {
			errors.rejectValue("quantity", "quantity", "1개 이상 선택해야 합니다.");
		}
		
		if(ordersForm.getQuantity() > ordersForm.getStock()) {
			errors.rejectValue("quantity", "quantity", "재고보다 많이 선택할 수 없습니다.");
		}
	}
	
	

}

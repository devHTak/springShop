package com.shop.main;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.account.Account;
import com.shop.account.CurrentUser;
import com.shop.orders.form.OrdersForm;
import com.shop.product.Product;
import com.shop.product.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final ProductService productService;
	
	@GetMapping("/")
	public String main(@CurrentUser Account account, Model model) {
		Set<Product> bestProducts = productService.findTop6ByOrderBySellCountDesc();
		Set<Product> latestProducts = productService.findTop6ByOrderByRegisterDateDesc();
		
		model.addAttribute("ordersForm", new OrdersForm());
		model.addAttribute("account", account);
		model.addAttribute("bestProducts", bestProducts);
		model.addAttribute("latestProducts", latestProducts);
		return "index";
	}
	
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
}

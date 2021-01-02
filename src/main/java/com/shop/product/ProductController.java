package com.shop.product;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.account.Account;
import com.shop.account.CurrentUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@GetMapping("/products")
	public String retrieveProductByName(@CurrentUser Account account, @RequestParam String name, Model model) {
		
		Set<Product> products = productService.findSearch(name);
		
		model.addAttribute("products", products);
		model.addAttribute("account", account);
		model.addAttribute("type", "FIND");
		model.addAttribute("keyword", name);
		return "product/products";
	}
	

}

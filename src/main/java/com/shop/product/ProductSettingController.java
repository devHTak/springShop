package com.shop.product;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shop.account.Account;
import com.shop.account.CurrentUser;
import com.shop.orders.form.OrdersForm;
import com.shop.product.form.ProductForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductSettingController {
	
	private final ProductService productService;

	@GetMapping("/accounts/{accountId}/products")
	public String getProducts(@CurrentUser Account account, @PathVariable Long accountId, Model model) {
		Set<Product> products = productService.findByAccount(account);
		
		model.addAttribute("products", products);
		model.addAttribute("account", account);
		model.addAttribute("type", "VENDOR");
		model.addAttribute("ordersForm", new OrdersForm());
		return "product/products";
	}
	
	@GetMapping("/accounts/{accountId}/products/new")
	public String getNewProduct(@CurrentUser Account account, @PathVariable Long accountId, Model model) {
		model.addAttribute("productForm", new ProductForm());
		model.addAttribute("account", account);
		model.addAttribute("type", "NEW");
		
		return "product/new";
	}
	
	@PostMapping("/accounts/{accountId}/products/new")
	public String doNewProduct(@CurrentUser Account account, @PathVariable Long accountId, @Valid @ModelAttribute ProductForm productForm, Errors errors, Model model) {
		if(errors.hasErrors()) {
			model.addAttribute("account", account);
			return "product/new";
		}
		
		productService.saveProduct(account, productForm);
		return "redirect:/accounts/" + accountId + "/products";		
	}
	
	@GetMapping("/accounts/{accountId}/products/{productId}")
	public String getOneProduct(@CurrentUser Account account, @PathVariable Long accountId, @PathVariable Long productId, Model model) {
		Product product = productService.findById(productId);
		
		model.addAttribute("account", account);
		model.addAttribute("product", product);
		model.addAttribute("productForm", new ProductForm());
		model.addAttribute("type", "UPDATE");
		return "product/new";
	}
	
	@PostMapping("/accounts/{accountId}/products/{productId}")
	public String doUpdateProduct(@CurrentUser Account account, @PathVariable Long accountId, @PathVariable Long productId, @Valid @ModelAttribute ProductForm productForm, Errors errors, Model model) {
		Product product = productService.findById(productId);
		if(errors.hasErrors()) {
			model.addAttribute("account", account);
			model.addAttribute("product", product);
			model.addAttribute("type", "UPDATE");
			return "product/new";
		}
		
		productService.updateProduct(product, productForm);
		return "redirect:/accounts/" + accountId + "/products";
	}
}
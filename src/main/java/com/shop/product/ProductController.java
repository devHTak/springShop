package com.shop.product;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.account.Account;
import com.shop.account.CurrentUser;
import com.shop.orders.Orders;
import com.shop.orders.OrdersService;
import com.shop.orders.form.OrdersForm;
import com.shop.orders.form.OrdersFormValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	private final OrdersService ordersService;
	private final OrdersFormValidator ordersFormValidator;
	
	@InitBinder("ordersForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(ordersFormValidator);
	}
	
	// 페이지
	@GetMapping("/products")
	public String retrieveProductByName(@CurrentUser Account account, @PageableDefault(size = 6, page = 0, sort = "registerDate") Pageable pageable, @RequestParam String name, Model model) {
		
		Page<Product> products = productService.findSearch(name, pageable);
		
		model.addAttribute("products", products);
		model.addAttribute("account", account);
		model.addAttribute("type", "FIND");
		model.addAttribute("keyword", name);
		model.addAttribute("ordersForm", new OrdersForm());
		return "product/products";
	}
	
	@PostMapping("/products/{productId}/carts")
	public String insertCartProduct(@CurrentUser Account account, @PathVariable Long productId, @Valid @ModelAttribute OrdersForm ordersForm, Errors errors, RedirectAttributes attribute, Model model) {
		if(errors.hasErrors()) {
			model.addAttribute("account", account);
			return "index";
		}
		
		Orders order = ordersService.saveToCart(account, productId, ordersForm);
		
		attribute.addFlashAttribute("successMessage", "상품을 카트에 담았습니다.");
		return "redirect:/carts";
	}
	
	
	@PostMapping("/products/{productId}/orders")
	public String insertOrderProduct(@CurrentUser Account account, @PathVariable Long productId, @Valid @ModelAttribute OrdersForm ordersForm, Errors errors, RedirectAttributes attribute, Model model) {
		if(errors.hasErrors()) {
			model.addAttribute("account", account);
			return "index";
		}
		
		Orders order = ordersService.saveToOrder(account, productId, ordersForm);
		
		attribute.addFlashAttribute("successMessage", "상품을 주문하였습니다.");
		return "redirect:/orders";
	}


}

package com.shop.product;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.shop.orders.OrdersService;
import com.shop.orders.Orders;
import com.shop.orders.form.OrdersForm;
import com.shop.orders.form.OrdersFormValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	private final OrdersService orderService;
	private final OrdersFormValidator ordersFormValidator;
	
	@InitBinder("ordersForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(ordersFormValidator);
	}
	
	@GetMapping("/products")
	public String retrieveProductByName(@CurrentUser Account account, @RequestParam String name, Model model) {
		
		Set<Product> products = productService.findSearch(name);
		
		model.addAttribute("products", products);
		model.addAttribute("account", account);
		model.addAttribute("type", "FIND");
		model.addAttribute("keyword", name);
		model.addAttribute("ordersForm", new OrdersForm());
		return "product/products";
	}
	
	@PostMapping("/products/{productId}/carts")
	public String insertCartProduct(@CurrentUser Account account, @PathVariable Long productId, @ModelAttribute OrdersForm ordersForm, RedirectAttributes attribute, Model model) {
		Orders order = orderService.saveToCart(account, productId, ordersForm);
		
		attribute.addFlashAttribute("successMessage", "상품을 카트에 담았습니다.");
		return "redirect:/carts";
	}
	
	
	@PostMapping("/products/{productId}/orders")
	public String insertOrderProduct(@CurrentUser Account account, @PathVariable Long productId, @ModelAttribute OrdersForm ordersForm, RedirectAttributes attribute, Model model) {
		Orders order = orderService.saveToOrder(account, productId, ordersForm);
		
		attribute.addFlashAttribute("successMessage", "상품을 주문하였습니다.");
		return "redirect:/orders";
	}


}

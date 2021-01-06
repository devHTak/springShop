package com.shop.orders;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.account.Account;
import com.shop.account.CurrentUser;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrdersController {
	
	private final OrdersService orderService;
	
	// 페이지
	@GetMapping("/carts")
	public String getCartPage(@CurrentUser Account account, @PageableDefault(size = 6, page = 0, sort = "orderDate", direction = Direction.DESC ) Pageable pageable,  Model model) {
		Page<Orders> orders = orderService.findForCartByCustomer(account, OrdersStatus.CART, pageable);
		model.addAttribute("orders", orders);
		model.addAttribute("account", account);
		model.addAttribute("type", "cart");
		return "order/orders";
	}
	
	// 페이지
	@GetMapping("/orders")
	public String getOrderPage(@CurrentUser Account account, @PageableDefault(size = 6, page = 0, sort = "orderDate", direction = Direction.DESC ) Pageable pageable, Model model) {
		List<OrdersStatus> orderStatus = new ArrayList<>();
		orderStatus.add(OrdersStatus.CART); orderStatus.add(OrdersStatus.CANCEL);
		
		Page<Orders> orders = orderService.findForOrderByCustomer(account, orderStatus, pageable);
		model.addAttribute("orders", orders);
		model.addAttribute("account", account);
		model.addAttribute("type", "order");
		return "order/orders";		
	}
	
	@GetMapping("/sold")
	public String getSoldPage(@CurrentUser Account account, @PageableDefault(size = 6, page = 0, sort = "orderDate", direction = Direction.DESC) Pageable pageable, Model model) {
		Page<Orders> orders = orderService.findForSoldByVendor(account, pageable);
		model.addAttribute("orders", orders);
		model.addAttribute("account", account);
		model.addAttribute("type", "sold");
		return "order/orders";
	}
	
	@GetMapping("/orders/{orderId}/{status}")
	public String changeOrderStatus(@CurrentUser Account account, @PathVariable Long orderId, @PathVariable String status, @RequestParam String type, RedirectAttributes attribute) {
		
		if(orderService.updateOrderStatus(account, orderId, status)) {
			attribute.addFlashAttribute("successMessage", status + "로 상태 변경하였습니다.");
		} else {
			attribute.addFlashAttribute("failMessage", "상태 변경에 실패하였습니다.");
		}
		
		if(type.equals("cart")) {
			return "redirect:/carts";
		} else if(type.equals("order")) {
			return "redirect:/orders";
		} else {
			return "redirect:/sold";
		}
	}

}

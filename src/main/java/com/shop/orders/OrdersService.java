package com.shop.orders;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.account.Account;
import com.shop.orders.form.OrdersForm;
import com.shop.product.Product;
import com.shop.product.ProductService;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class OrdersService {
	
	private final ProductService productService;
	private final OrdersRepository orderRepository;
	
	public Page<Orders> findForCartByCustomer(Account account, OrdersStatus orderStatus, Pageable pageable) {
		return orderRepository.findByCustomerAndStatus(account, orderStatus, pageable); 
	}
	
	public Page<Orders> findForOrderByCustomer(Account account, List<OrdersStatus> orderStatus, Pageable pageable) {
		return orderRepository.findByCustomerAndStatusNotIn(account, orderStatus, pageable);
	}
	
	public Page<Orders> findForSoldByVendor(Account account, Pageable pageable) {
		return orderRepository.findByVendor(account, pageable);
	}

	public Orders saveToCart(Account account, Long productId, OrdersForm ordersForm) {
		Product product = productService.findById(productId);
		Orders order = Orders.builder()
				.price(ordersForm.getPrice())
				.quantity(ordersForm.getQuantity())
				.totalPrice(ordersForm.getPrice() * ordersForm.getQuantity())
				.product(product)
				.status(OrdersStatus.CART).build();
		
		account.addOrder(order);
		product.getAccount().addSellProduct(order);
		
		return orderRepository.save(order);
	}
	
	public Orders saveToOrder(Account account, Long productId, OrdersForm ordersForm) {
		Product product = productService.findById(productId);
		product.setSellCount(product.getSellCount() + 1);
		product.setStock(product.getStock() - ordersForm.getQuantity());
		Orders order = Orders.builder()
				.price(ordersForm.getPrice())
				.quantity(ordersForm.getQuantity())
				.totalPrice(ordersForm.getPrice() * ordersForm.getQuantity())
				.product(product)
				.status(OrdersStatus.ORDER)
				.orderDate(LocalDateTime.now()).build();
		
		account.addOrder(order);
		product.getAccount().addSellProduct(order);
		
		return orderRepository.save(order);
	}
	
	public boolean updateOrderStatus(Account account, Long ordersId, String status) {
		Orders order = orderRepository.findById(ordersId).orElseThrow(()->{
			throw new IllegalArgumentException(String.format("Order[id= %d] is not found", ordersId));
		});
		
		if(status.equals("cancel")) {
			if((order.getStatus().equals(OrdersStatus.CART) || order.getStatus().equals(OrdersStatus.ORDER))
					&& account.getRole().equals("ROLE_ACCOUNT")) {
				order.setStatus(OrdersStatus.CANCEL);
				return true;
			}
		} else if(status.equals("cart")) {
			if(order.getStatus().equals(OrdersStatus.CANCEL) && account.getRole().equals("ROLE_ACCOUNT") ) {
				order.setStatus(OrdersStatus.CART);
				return true;
			}
		} else if(status.equals("order")) {
			if((order.getStatus().equals(OrdersStatus.CANCEL) || order.getStatus().equals(OrdersStatus.CART)) 
					&& account.getRole().equals("ROLE_ACCOUNT") ) {
				order.setStatus(OrdersStatus.ORDER);
				return true;
			}
		} else if(status.equals("delivery")) {
			if(order.getStatus().equals(OrdersStatus.ORDER) && account.getRole().equals("ROLE_VENDOR")) {
				order.setStatus(OrdersStatus.DELIVERY);
				return true;
			}
		} else if(status.equals("complete")) {
			if(order.getStatus().equals(OrdersStatus.DELIVERY) && account.getRole().equals("ROLE_VENDOR")) {
				order.setStatus(OrdersStatus.COMPLETE);
				return true;
			}
		}
		
		return false;
	}
}

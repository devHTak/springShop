package com.shop.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.shop.account.Account;
import com.shop.account.AccountRepository;
import com.shop.account.AccountService;
import com.shop.account.WithMockAccount;
import com.shop.product.Product;
import com.shop.product.ProductRepository;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class OrdersControllerTest {
	
	@Autowired MockMvc mockMvc;
	@Autowired OrdersService ordersService;
	@Autowired OrdersRepository ordersRepository;
	@Autowired AccountService accountService;
	@Autowired AccountRepository accountRepository;
	@Autowired ProductRepository productRepository;
	@Autowired PasswordEncoder passwordEncoder;
	
	@BeforeEach
	public void beforeEach() {
		Account account1 = Account.builder()
				.email("test11@test.com")
				.name("test1")
				.nickname("test1")
				.password(passwordEncoder.encode("test1234"))
				.registerTime(LocalDateTime.now())
				.role("ROLE_VENDOR").build();
		accountRepository.save(account1);
		
		Account account2 = Account.builder()
				.email("test22@test.com")
				.name("test2")
				.nickname("test2")
				.password(passwordEncoder.encode("test1234"))
				.registerTime(LocalDateTime.now())
				.role("ROLE_ACCOUNT").build();
		accountRepository.save(account2);
		
		Product product = Product.builder()
				.account(account1)
				.description("test1234")
				.name("test")
				.price(1000)
				.stock(10)
				.sellCount(0)
				.registerDate(LocalDateTime.now()).build();
		
		productRepository.save(product);
	}
	
	@AfterEach
	public void afterEach() {
		ordersRepository.deleteAll();
		productRepository.deleteAll();
		accountRepository.deleteAll();
	}
	
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("카트 넣기 - 성공")
	@Test
	public void insertCartSuccessTest() throws Exception {
		Product product = productRepository.findByName("test");
		
		mockMvc.perform(post("/products/{productId}/carts/", product.getId())
				.param("quantity", "1")
				.param("price", product.getPrice() + "")
				.param("stock", product.getStock() + ""))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("successMessage"))
			.andExpect(redirectedUrl("/carts"));		
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("카트 넣기 - 실패")
	@Test
	public void insertCartFailTest() throws Exception {
		Product product = productRepository.findByName("test");
		
		mockMvc.perform(post("/products/{productId}/carts/", product.getId())
				.param("quantity", "0")
				.param("price", product.getPrice() + "")
				.param("stock", product.getStock() + ""))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasErrors())
			.andExpect(model().attributeExists("account"))
			.andExpect(view().name("index"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("주문 넣기 - 성공")
	@Test
	public void insertOrderSuccessTest() throws Exception {
		Product product = productRepository.findByName("test");
		
		mockMvc.perform(post("/products/{productId}/orders/", product.getId())
				.param("quantity", "1")
				.param("price", product.getPrice() + "")
				.param("stock", product.getStock() + ""))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("successMessage"))
			.andExpect(redirectedUrl("/orders"));		
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("주문 넣기 - 실패")
	@Test
	public void insertOrderFailTest() throws Exception {
		Product product = productRepository.findByName("test");
		
		mockMvc.perform(post("/products/{productId}/orders/", product.getId())
				.param("quantity", "2")
				.param("price", product.getPrice() + "")
				.param("stock", "1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasErrors())
			.andExpect(model().attributeExists("account"))
			.andExpect(view().name("index"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("카트 조회")
	@Test
	public void retrieveCartPageTest() throws Exception {
		mockMvc.perform(get("/carts/"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("orders"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attribute("type", "cart"))
			.andExpect(view().name("order/orders"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("주문 조회")
	@Test
	public void retrieveOrderPageTest() throws Exception {
		mockMvc.perform(get("/orders/"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("orders"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attribute("type", "order"))
			.andExpect(view().name("order/orders"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("판매 조회")
	@Test
	public void retrieveSoldPageTest() throws Exception {
		mockMvc.perform(get("/sold/"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("orders"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attribute("type", "sold"))
			.andExpect(view().name("order/orders"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("실패 변경 - 성공")
	@Test
	public void updateCancelSuccessTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.CART, "test", "test1");
		mockMvc.perform(get("/orders/{orderId}/cancel", order.getId())
				.queryParam("type", "cart"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("successMessage"))
			.andExpect(redirectedUrl("/carts"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.CANCEL);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("실패 변경 - 실패")
	@Test
	public void updateCancelFailTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.DELIVERY, "test", "test1");
		mockMvc.perform(get("/orders/{orderId}/cancel", order.getId())
				.queryParam("type", "cart"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("failMessage"))
			.andExpect(redirectedUrl("/carts"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.DELIVERY);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("카트 변경 - 성공")
	@Test
	public void updateCartSuccessTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.CANCEL, "test", "test1");
		mockMvc.perform(get("/orders/{orderId}/cart", order.getId())
				.queryParam("type", "cart"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("successMessage"))
			.andExpect(redirectedUrl("/carts"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.CART);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("카트 변경 - 실패")
	@Test
	public void updateCartFailTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.ORDER, "test", "test1");
		mockMvc.perform(get("/orders/{orderId}/cart", order.getId())
				.queryParam("type", "order"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("failMessage"))
			.andExpect(redirectedUrl("/orders"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.ORDER);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("주문 변경 - 성공")
	@Test
	public void updateOrderSuccessTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.CART, "test", "test1");
		mockMvc.perform(get("/orders/{orderId}/order", order.getId())
				.queryParam("type", "cart"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("successMessage"))
			.andExpect(redirectedUrl("/carts"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.ORDER);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("주문 변경 - 실패")
	@Test
	public void updateOrderFailTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.COMPLETE, "test", "test1");
		mockMvc.perform(get("/orders/{orderId}/order", order.getId())
				.queryParam("type", "order"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("failMessage"))
			.andExpect(redirectedUrl("/orders"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.COMPLETE);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("배송중 변경 - 성공")
	@Test
	public void updateDeliverySuccessTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.ORDER, "test2", "test");
		mockMvc.perform(get("/orders/{orderId}/delivery", order.getId())
				.queryParam("type", "sold"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("successMessage"))
			.andExpect(redirectedUrl("/sold"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.DELIVERY);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_ACCOUNT")
	@DisplayName("배송중 변경 - 실패")
	@Test
	public void updateDeliveryFailTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.ORDER, "test", "test1");
		mockMvc.perform(get("/orders/{orderId}/delivery", order.getId())
				.queryParam("type", "order"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("failMessage"))
			.andExpect(redirectedUrl("/orders"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.ORDER);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("완료 변경 - 성공")
	@Test
	public void updateCompleteSuccessTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.DELIVERY, "test2", "test");
		mockMvc.perform(get("/orders/{orderId}/complete", order.getId())
				.queryParam("type", "sold"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("successMessage"))
			.andExpect(redirectedUrl("/sold"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.COMPLETE);
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("완료 변경 - 실패")
	@Test
	public void updateCompleteFailTest() throws Exception {
		Orders order = this.insertOrder(OrdersStatus.ORDER, "test2", "test");
		mockMvc.perform(get("/orders/{orderId}/complete", order.getId())
				.queryParam("type", "sold"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("failMessage"))
			.andExpect(redirectedUrl("/sold"));
		
		Orders byId = ordersRepository.findById(order.getId()).get();
			
		assertEquals(byId.getStatus(), OrdersStatus.ORDER);
	}
	
	
	
	private Orders insertOrder(OrdersStatus status, String customerName, String vendorName) {
		Account customer = accountService.findByNickname(customerName);
		Account vendor = accountService.findByNickname(vendorName);
		Product product = productRepository.findByName("test");
		
		Orders order = Orders.builder()
				.orderDate(LocalDateTime.now())
				.price(product.getPrice())
				.product(product)
				.quantity(1)
				.status(status)
				.totalPrice(product.getPrice() * 1).build();
		
		customer.addOrder(order);
		vendor.addSellProduct(order);
		return ordersRepository.save(order);
	}
}

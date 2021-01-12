package com.shop.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.web.servlet.MockMvc;

import com.shop.account.Account;
import com.shop.account.AccountRepository;
import com.shop.account.AccountService;
import com.shop.account.WithMockAccount;

@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ProductSettingControllerTest {
	
	@Autowired MockMvc mockMvc;
	@Autowired AccountService accountService;
	@Autowired AccountRepository accountRepository;
	@Autowired ProductService productService;
	@Autowired ProductRepository productRepository;
	
	@BeforeEach
	public void beforeEach() {
		Account account = accountService.findByNickname("test");
		for(int i = 0; i < 5; i++) {
			Product product = Product.builder()
					.description("testtest" + i)
					.name("test" + i)
					.price(1000 * (i+1))
					.stock(100 * (i+1))
					.sellCount(0)
					.registerDate(LocalDateTime.now()).build();
			
			account.addProduct(product);
			
			productRepository.save(product);
		}
	}
	
	@AfterEach
	public void afterEach() {
		productRepository.deleteAll();
		accountRepository.deleteAll();			
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("협력 업체에서 등록한 상품 조회")
	@Test
	public void getProductsTest() throws Exception {
		Account account = accountService.findByNickname("test");
		
		mockMvc.perform(get("/accounts/{accountId}/products", account.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("products"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("type"))
			.andExpect(view().name("product/products"));		
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("신규 상품 등록 요청 페이지 조회")
	@Test
	public void getNewProductPageTest() throws Exception {
		Account account = accountService.findByNickname("test");
		
		mockMvc.perform(get("/accounts/{accountId}/products/new", account.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("productForm"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("type"))
			.andExpect(view().name("product/new"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("신규 상품 등록 요청 - 성공")
	@Test
	public void doNewProductSuccessTest() throws Exception {
		Account account = accountService.findByNickname("test");
		
		mockMvc.perform(post("/accounts/{accountId}/products/new", account.getId())
				.param("description", "testtest")
				.param("name", "test")
				.param("price", "1000")
				.param("stock", "100"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/accounts/" + account.getId() + "/products"));
		
		Product product = productRepository.findByName("test");
		assertEquals(account.getNickname(), product.getAccount().getNickname());		
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("신규 상품 등록 요청 - 실패")
	@Test
	public void doNewProductFailTest() throws Exception {
		Account account = accountService.findByNickname("test");
		
		mockMvc.perform(post("/accounts/{accountId}/products/new", account.getId())
				.param("description", "testtest")
				.param("price", "1000")
				.param("stock", "100"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasErrors())
			.andExpect(model().attributeExists("account"))
			.andExpect(view().name("product/new"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("상품 수정 화면")
	@Test
	public void getProductPage() throws Exception {
		Account account = accountService.findByNickname("test");
		Product product = productRepository.findByName("test0");
		
		mockMvc.perform(get("/accounts/{accountId}/products/{productId}", account.getId(), product.getId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("productForm"))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("type"))
			.andExpect(view().name("product/new"));
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("상품 수정 요청 - 성공")
	@Test
	public void updateProductSuccessTest() throws Exception {
		Account account = accountService.findByNickname("test");
		Product product = productRepository.findByName("test0");
		
		mockMvc.perform(post("/accounts/{accountId}/products/{productId}", account.getId(), product.getId())
				.param("description", "testtest")
				.param("name", "test")
				.param("price", "1000")
				.param("stock", "100"))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/accounts/" + account.getId() + "/products"));
		
		Product byName = productRepository.findByName("test");
		assertEquals(byName.getId(), product.getId());		
	}
	
	@WithMockAccount(nickname = "test", role = "ROLE_VENDOR")
	@DisplayName("상품 수정 요청 - 실패")
	@Test
	public void updateProductFailTest() throws Exception {
		Account account = accountService.findByNickname("test");
		Product product = productRepository.findByName("test0");
		
		mockMvc.perform(post("/accounts/{accountId}/products/{productId}", account.getId(), product.getId())
				.param("description", "testtest")
				.param("price", "1000")
				.param("stock", "100"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasErrors())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("product"))
			.andExpect(model().attributeExists("type"))
			.andExpect(view().name("product/new"));
	}
}

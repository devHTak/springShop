package com.shop.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
public class ProductControllerTest {
	
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
	@DisplayName("상품 찾기")
	@Test
	public void getProductsTest() throws Exception {
		mockMvc.perform(get("/products")
				.param("name", "test"))
			.andDo(print())
			.andExpect(model().attributeExists("products"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("type"))
			.andExpect(model().attributeExists("keyword"))
			.andExpect(view().name("product/products"));
	}
	

}

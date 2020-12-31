package com.shop.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AccountControllerTest {
	
	@Autowired private MockMvc mockMvc;
	@Autowired private AccountRepository accountRepository;
	@Autowired private AccountService accountService;
	
	@AfterEach
	public void afterEach() {
		accountRepository.deleteAll();
	}
	
	@DisplayName("회원 가입 화면 확인")
	@Test
	public void signUpTest() throws Exception {
		mockMvc.perform(get("/accounts"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(model().attributeExists("accountSignUpForm"));
	}
	
	@DisplayName("회원 가입 - 성공")
	@Test
	public void accountSignUpSuccessTest() throws Exception {
		mockMvc.perform(post("/accounts")
					.param("nickname", "test")
					.param("email", "test@test.com")
					.param("password", "test1234")
					.param("accountCode", "ACCOUNT")
					.with(csrf()))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"))
			.andExpect(authenticated());
		
		// DB 저장 확인 및 ACCOUNT Role 확인
		Account account = accountService.findByNickname("test");
		
		assertEquals("test@test.com", account.getEmail());
		assertEquals("ROLE_ACCOUNT", account.getRole());
	}
	
	@DisplayName("회원 가입 - 실패")
	@Test
	public void accountSignUpFailTest() throws Exception {
		mockMvc.perform(post("/accounts")
					.param("nickname", "test")
					.param("email",  "test@test.com")
					.param("password", "test")
					.param("accountCode", "ACCOUNT")
					.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasErrors())
			.andExpect(view().name("account/sign-up"))
			.andExpect(unauthenticated());
	}
	
	@DisplayName("업체 가입 - 성공")
	@Test
	public void vendorSignUpSuccessTest() throws Exception {
		mockMvc.perform(post("/accounts")
					.param("nickname", "test")
					.param("email", "test@test.com")
					.param("password", "test1234")
					.param("accountCode", "VENDOR")
					.with(csrf()))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated());
		
		// DB 저장 확인 및 ACCOUNT Role 확인
		Account vendor = accountService.findByNickname("test");
		
		assertEquals("test@test.com", vendor.getEmail());
		assertEquals("ROLE_VENDOR", vendor.getRole());
	}
	
	@DisplayName("업체 가입 - 실패")
	@Test
	public void vendorSignUpFailTest() throws Exception {
		mockMvc.perform(post("/accounts")
					.param("nickname", "test")
					.param("email",  "test@test.com")
					.param("password", "test")
					.param("accountCode", "VENDOR")
					.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().hasErrors())
			.andExpect(view().name("account/sign-up"))
			.andExpect(unauthenticated());
	}

}

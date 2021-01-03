package com.shop.product;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.shop.account.Account;
import com.shop.product.form.ProductForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;
	
	
	public Set<Product> findByAccount(Account account) {
		return productRepository.findByAccount(account);
	}
	
	public Product saveProduct(Account account, ProductForm productForm) {
		Product product = Product.builder()
				.description(productForm.getDescription())
				.name(productForm.getName())
				.price(productForm.getPrice())
				.stock(productForm.getStock())
				.sellCount(0)
				.registerDate(LocalDateTime.now()).build();
		
		account.addProduct(product);
		
		return productRepository.save(product);
	}
	
	public Product updateProduct(Product product, ProductForm productForm) {
		product.setName(productForm.getName());
		product.setDescription(productForm.getDescription());
		product.setPrice(productForm.getPrice());
		product.setStock(productForm.getStock());
		
		return productRepository.save(product);
	}
	
	public Product findById(Long id) {
		return productRepository.findById(id).orElseThrow(()->{
			throw new IllegalArgumentException(String.format("Product[Id = %d] is not found", id));
		});
	}
	
	public Set<Product> findSearch(String name) {
		return productRepository.findByNameContainingOrDescriptionContaining(name, name);
	}

}

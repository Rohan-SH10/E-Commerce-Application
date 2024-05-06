package com.ecommerce.retail_electronicsapp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecommerce.retail_electronicsapp.entity.Product;
import com.ecommerce.retail_electronicsapp.enums.AvailabilityStatus;
import com.ecommerce.retail_electronicsapp.exceptions.AccountAceessRequestDeniedException;
import com.ecommerce.retail_electronicsapp.exceptions.IllegalAccessRequestExcpetion;
import com.ecommerce.retail_electronicsapp.exceptions.ProductNotFoundException;
import com.ecommerce.retail_electronicsapp.repository.ProductRepository;
import com.ecommerce.retail_electronicsapp.repository.SellerRepository;
import com.ecommerce.retail_electronicsapp.requestdto.ProductRequest;
import com.ecommerce.retail_electronicsapp.requestdto.SearchFilters;
import com.ecommerce.retail_electronicsapp.responsedto.ProductResponse;
import com.ecommerce.retail_electronicsapp.service.ProductService;
import com.ecommerce.retail_electronicsapp.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

	private SellerRepository sellerRepo;
	private ProductRepository productRepo;
	private ResponseStructure<ProductResponse> respStruct;
	private ResponseStructure<List<ProductResponse>> respListStruct;
	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!authentication.isAuthenticated()) throw new IllegalAccessRequestExcpetion("user not authenticated");
		
//		User user = userRepo.findByUsername(authentication.getName()).get();
		return sellerRepo.findByUsername(authentication.getName()).map(seller ->{
			Product uniqueProduct = productRepo.save(mapToProduct(productRequest,new Product()));
			seller.getProducts().add(uniqueProduct);
			sellerRepo.save(seller);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(respStruct.setStatusCode(HttpStatus.CREATED.value())
							.setMessage("product added successfully")
							.setData(mapToProductResponse(uniqueProduct)));
		}).orElseThrow(()->new AccountAceessRequestDeniedException("user is not found/invalid"));
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> updateProduct(int productId, ProductRequest productRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!authentication.isAuthenticated()) throw new IllegalAccessRequestExcpetion("user not authenticated");
		
//		User user = userRepo.findByUsername(authentication.getName()).get();
		return sellerRepo.findByUsername(authentication.getName()).map(seller ->{
			return productRepo.findById(productId).map(product ->{
				Product uniqueProduct = productRepo.save(mapToProduct(productRequest,product));
				return ResponseEntity.status(HttpStatus.ACCEPTED)
						.body(respStruct.setStatusCode(HttpStatus.ACCEPTED.value())
								.setMessage("product added successfully")
								.setData(mapToProductResponse(uniqueProduct)));
			}).orElseThrow(()-> new ProductNotFoundException("product not found, please enter a valid Id"));
		}).orElseThrow(()->new AccountAceessRequestDeniedException("user is not found/invalid"));
	}
	
	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponse>>> fetchSellerAllProducts() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!authentication.isAuthenticated()) throw new IllegalAccessRequestExcpetion("user not authenticated");
		
//		User user = userRepo.findByUsername(authentication.getName()).get();
		return sellerRepo.findByUsername(authentication.getName()).map(seller ->{
			
				List<ProductResponse> products=new ArrayList<>();
				for(Product product:seller.getProducts()) products.add(mapToProductResponse(product));
				return ResponseEntity.status(HttpStatus.ACCEPTED)
						.body(respListStruct.setStatusCode(HttpStatus.ACCEPTED.value())
								.setMessage("product added successfully")
								.setData(products));
		}).orElseThrow(()->new AccountAceessRequestDeniedException("user is not found/invalid"));
	}

	@Override
	public ResponseEntity<ResponseStructure<ProductResponse>> fetchProduct(int productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!authentication.isAuthenticated()) throw new IllegalAccessRequestExcpetion("user not authenticated");
		
		return sellerRepo.findByUsername(authentication.getName()).map(seller ->{
			return productRepo.findById(productId).map(product ->{
				
				return ResponseEntity.status(HttpStatus.ACCEPTED)
						.body(respStruct.setStatusCode(HttpStatus.ACCEPTED.value())
								.setMessage("product added successfully")
								.setData(mapToProductResponse(product)));
			}).orElseThrow(()-> new ProductNotFoundException("product not found, please enter a valid Id"));
		}).orElseThrow(()->new AccountAceessRequestDeniedException("user is not found/invalid"));
	}
	
	private Product mapToProduct(ProductRequest productRequest, Product product) {
		int quantity=productRequest.getProductQuantity();
		if(quantity>10) 
			product.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
		else if(quantity<10 && quantity>0) 
			product.setAvailabilityStatus(AvailabilityStatus.LOW_STOCK);
		else if(quantity==0) 
			product.setAvailabilityStatus(AvailabilityStatus.OUT_OF_STOCK);
		
		product.setProductName(productRequest.getProductName())
			.setProductDescription(productRequest.getProductDescription())
			.setCategory(productRequest.getCategory())
			.setProductQuantity(productRequest.getProductQuantity())
			.setProductPrice(productRequest.getProductPrice());
		return product;
	}
	

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder().productId(product.getProductId())
									.productName(product.getProductName())
									.productDescription(product.getProductDescription())
									.productPrice(product.getProductPrice())
									.productQuantity(product.getProductQuantity())
									.availabilityStatus(product.getAvailabilityStatus())
									.category(product.getCategory())
									.imageLinks(null)
									.coverImageLink(null)
									.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ProductResponse>>> findAllProducts(SearchFilters filters) {
		ProductSpecification ps = new ProductSpecification(filters);
		System.out.println(ps.buildSpecification().toString());
		Specification<Product> specification = ps.buildSpecification();
		List<Product> products = productRepo.findAll(specification);
		List<ProductResponse> prodResponses= new ArrayList<>();
		products.forEach(product->prodResponses.add(mapToProductResponse(product)));
		return ResponseEntity.ok().body(respListStruct.setStatusCode(HttpStatus.OK.value()).setMessage("Product found").setData(prodResponses));
	}
}

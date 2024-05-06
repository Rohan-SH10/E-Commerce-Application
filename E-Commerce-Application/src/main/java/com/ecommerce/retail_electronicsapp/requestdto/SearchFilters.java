package com.ecommerce.retail_electronicsapp.requestdto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ToString
@Getter
@Setter
public class SearchFilters {

	int minPrice;
	int maxPrice;
	String availability;
	String category;
	int rating;
	int discount;
	
	
}
//
//@RequestParam( required = false) Integer minPrice
//,@RequestParam(required = false) Integer maxPrice
//,@RequestParam(required = false) String availability,
//@RequestParam(required = false) String category,
//@RequestParam(required = false) Integer rating,
//@RequestParam(required = false) Integer discount
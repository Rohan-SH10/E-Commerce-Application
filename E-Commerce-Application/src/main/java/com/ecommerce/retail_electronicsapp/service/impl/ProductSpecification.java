package com.ecommerce.retail_electronicsapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.retail_electronicsapp.entity.Product;
import com.ecommerce.retail_electronicsapp.requestdto.SearchFilters;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductSpecification {

	SearchFilters filter;
	
	public Specification<Product> buildSpecification(){
		return (root,query,criteriaBuilder)->{
			List<Predicate> predicates = new ArrayList<>();
			
			 if (filter.getMinPrice() != 0) {
	                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("productPrice"), filter.getMinPrice()));
	            }
	            if (filter.getMaxPrice() != 0) {
	                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("productPrice"), filter.getMaxPrice()));
	            }
	            if (filter.getAvailability() != null) {
	                predicates.add(criteriaBuilder.equal(root.get("availabilityStatus"), filter.getAvailability()));
	            }
	            if (filter.getCategory() != null) {
	                predicates.add(criteriaBuilder.equal(root.get("category"), filter.getCategory()));
	            }
//	            if (filter.getRating() != 0) {
//	                predicates.add(criteriaBuilder.equal(root.get("rating"), filter.getRating()));
//	            }
//	            if (filter.getDiscount() != 0) {
//	                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), filter.getDiscount()));
//	            }
	            
	            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}

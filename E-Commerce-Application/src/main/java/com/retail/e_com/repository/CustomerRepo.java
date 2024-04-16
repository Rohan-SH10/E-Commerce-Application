package com.retail.e_com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Integer>{

}

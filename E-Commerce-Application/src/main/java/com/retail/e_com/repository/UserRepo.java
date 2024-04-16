package com.retail.e_com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.model.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	boolean existsByEmail(String email);

}

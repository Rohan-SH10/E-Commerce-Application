package com.ecommerce.retail_electronicsapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecommerce.retail_electronicsapp.entity.Image;
import com.ecommerce.retail_electronicsapp.enums.ImageType;

public interface ImageRepository extends MongoRepository<Image, String>{

	boolean existsByProductIdAndImageType(int productId, ImageType cover);

	Optional<Image> findByProductIdAndImageType(int productId, ImageType cover);

	List<Image> findAllByProductId(int productId);

}

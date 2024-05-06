package com.ecommerce.retail_electronicsapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.retail_electronicsapp.entity.Image;
import com.ecommerce.retail_electronicsapp.utility.SimpleResponseStructure;

public interface ImageService {

	ResponseEntity<SimpleResponseStructure> addImage(int productId, String imageType, MultipartFile image) throws IOException;

	ResponseEntity<byte[]> findImages(String imageId);
	

}

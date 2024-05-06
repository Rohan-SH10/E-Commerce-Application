package com.ecommerce.retail_electronicsapp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.retail_electronicsapp.entity.Image;
import com.ecommerce.retail_electronicsapp.service.ImageService;
import com.ecommerce.retail_electronicsapp.utility.SimpleResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/re-v1")
public class ImageController {

	private ImageService imageService;
	@PostMapping("/product/{productId}/add-image")
	public ResponseEntity<SimpleResponseStructure> addImage(@PathVariable int productId,  String imageType,MultipartFile image) throws IOException{
		System.out.println("Inside add image");
		return imageService.addImage(productId,imageType,image);
	}
	
	@PostMapping("/find-image/{imageId}")
	public ResponseEntity<byte[]> findImages(@PathVariable String imageId){
		return imageService.findImages(imageId);
	}
}

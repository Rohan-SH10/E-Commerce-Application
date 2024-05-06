package com.ecommerce.retail_electronicsapp.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.ecommerce.retail_electronicsapp.enums.ImageType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "images")
@Getter
@AllArgsConstructor 
@NoArgsConstructor
public class Image {

	@MongoId
	private String imageId;
	private ImageType imageType;
	private byte[] imagesBytes;
	private int productId;
	private String contentType;
	public Image setImageId(String imageId) {
		this.imageId = imageId;
		return this;
	}
	public Image setImageType(ImageType imageType) {
		this.imageType = imageType;
		return this;
	}
	public Image setImagesBytes(byte[] imagesBytes) {
		this.imagesBytes = imagesBytes;
		return this;
	}
	public Image setProductId(int productId) {
		this.productId = productId;
		return this;
	}
	public Image setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	
	
}
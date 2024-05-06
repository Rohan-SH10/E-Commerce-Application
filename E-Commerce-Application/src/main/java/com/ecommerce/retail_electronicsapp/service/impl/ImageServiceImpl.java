package com.ecommerce.retail_electronicsapp.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.retail_electronicsapp.entity.Image;
import com.ecommerce.retail_electronicsapp.enums.ImageType;
import com.ecommerce.retail_electronicsapp.repository.ImageRepository;
import com.ecommerce.retail_electronicsapp.repository.ProductRepository;
import com.ecommerce.retail_electronicsapp.service.ImageService;
import com.ecommerce.retail_electronicsapp.utility.SimpleResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService{

	private SimpleResponseStructure simpleResponseStructure;
	private ImageRepository imageRepository;
	private ProductRepository productRepository;
	@Override
	public ResponseEntity<SimpleResponseStructure> addImage(int productId, String imageType, MultipartFile image) throws IOException  {

		if(!productRepository.existsById(productId))
			throw new RuntimeException();


		if(imageType.equalsIgnoreCase("cover")) {
			System.out.println("Its cover");
			if(imageRepository.existsByProductIdAndImageType(productId, ImageType.COVER)) {
				System.out.println("cover exists");
				return imageRepository.findByProductIdAndImageType(productId,ImageType.COVER).map(im->{
					im.setImageType(ImageType.OTHER);
					imageRepository.save(im);
					Image i = new Image();
					try {
						i.setImagesBytes(image.getBytes()).setImageType(ImageType.COVER).setProductId(productId).setContentType(image.getContentType());
					} catch (IOException e) {
						e.printStackTrace();
					}
					i=imageRepository.save(i);
					return ResponseEntity.ok(simpleResponseStructure.setMessage("Image Added with id : "+i.getImageId()).setStatus(HttpStatus.OK.value()));
				}).orElseThrow(()-> new RuntimeException());			
			}
			else {
				System.out.println("cover doesnt exists");
				Image i = new Image();
				i.setImagesBytes(image.getBytes()).setImageType(ImageType.COVER).setProductId(productId).setContentType(image.getContentType());
				i=imageRepository.save(i);
				return ResponseEntity.ok(simpleResponseStructure.setMessage("Image Added with id : "+i.getImageId()).setStatus(HttpStatus.OK.value()));
			}
		}
		if(imageType.equalsIgnoreCase("other")) {
			System.out.println("Other type");
			Image i = new Image();
			i.setImagesBytes(image.getBytes()).setImageType(ImageType.OTHER).setProductId(productId).setContentType(image.getContentType());
			i=imageRepository.save(i);
			return ResponseEntity.ok(simpleResponseStructure.setMessage("Image Added with id : "+i.getImageId()+" "+i.getImageType()).setStatus(HttpStatus.OK.value()));
		}

		else {
			throw new RuntimeException("Image is of neither Types");
		}

	}
	@Override
	public ResponseEntity<byte[]> findImages(String imageId) {
		return imageRepository.findById(imageId).map(image->{
			return ResponseEntity.ok().contentLength(image.getImagesBytes().length).contentType(MediaType.ALL.valueOf(image.getContentType())).body(image.getImagesBytes());
		}).orElseThrow();
		
	}

}

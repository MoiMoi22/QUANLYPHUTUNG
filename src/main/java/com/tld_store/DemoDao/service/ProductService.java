package com.tld_store.DemoDao.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import dao.ProductDAO;
import dto.Product;
import exception.CustomException;

@Service
public class ProductService {
	@Autowired
	ProductDAO productDao;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private InputService inputService;
    @Autowired
    private UploadImgService imgService;
    
    public Product findProductById(String id) {
    	return productDao.findById(id);
    }
    
	public int insertProduct(MultipartFile profileImage, Product product) {
		
		inputService.isValidProduct(product);
		
		product.setProductName(inputService.formatName(product.getProductName()));
		product.setProductId(inputService.formatID(product.getProductName()));
		
		String productId = productDao.insertProduct(product);
		String imageUrl = "";
		int result = -1;
		
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			Map uploadResult;
			imgService.checkFileUpload(profileImage);
			try {

				uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Products"));
			    imageUrl = (String) uploadResult.get("secure_url");  // Lấy URL ảnh
			    
			    productDao.updateUrl(productId, imageUrl);
			} 
			catch (Exception e) {
				throw new CustomException(e.getMessage());
			}		
		}
		
		result = 1;
		
		return result;
	}
	
	public int updateProduct(MultipartFile profileImage, Product product) {
		inputService.isValidProduct(product);
		
		product.setProductName(inputService.formatName(product.getProductName()));
		
		String imageUrl = "";
		int result = -1;
		
		productDao.update(product);
				
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			Map uploadResult;
			imgService.checkFileUpload(profileImage);

			try {

				uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Products"));
			    imageUrl = (String) uploadResult.get("secure_url");  // Lấy URL ảnh
			    
			    productDao.updateUrl(product.getProductId(), imageUrl);
			} 
			catch (Exception e) {
				throw new CustomException(e.getMessage());
			}		
		}
		
		result = 1;
		
		return result;
	}
	
	public int deleteProduct(Product product) {
		 return productDao.delete(product);
	}
    
    public ArrayList<Product> getAllProducts(){
    	return (ArrayList<Product>)productDao.getAll();
    }
    
	public ArrayList<Product> getTop10Products(int numPage, AtomicBoolean isFinal){
		ArrayList<Product> arr = (ArrayList<Product>)productDao.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Product>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Product> getProductsByName(String searchName)
	{
		ArrayList<Product> arr = productDao.findAllProductsByName(searchName.trim());
		return arr;
	} 
	
	public ArrayList<Product> getTop10ProductsByName(String productName,int numPage, AtomicBoolean isFinal){
		ArrayList<Product> arr = (ArrayList<Product>)productDao.findTop10ProductsByName( productName, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Product>( arr.subList(0, Math.min(arr.size(), 10)));
	}
    
}

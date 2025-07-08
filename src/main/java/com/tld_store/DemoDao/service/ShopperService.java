package com.tld_store.DemoDao.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import dao.ShopperDAO;
import dto.Shopper;
import exception.CustomException;


@Service
public class ShopperService {
	@Autowired
	ShopperDAO shopperDao;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private UploadImgService imgService;
    @Autowired
    private InputService inputService;
    
    public Shopper findShopperById(int id) {
    	return shopperDao.findById(id);
    }
    
	public ArrayList<Shopper> getAllShoppers()
	{
		ArrayList<Shopper> arr = (ArrayList<Shopper>)shopperDao.getAll();
		return arr;
	}
	
	public ArrayList<Shopper> getTop10Shoppers(int numPage, AtomicBoolean isFinal){
		ArrayList<Shopper> arr = (ArrayList<Shopper>)shopperDao.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Shopper>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Shopper> getShoppersByFullName(String searchName, int numPage, AtomicBoolean isFinal)
	{
		ArrayList<Shopper> arr = shopperDao.getTop10ShopperByFullName(searchName.trim(), numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Shopper>( arr.subList(0, Math.min(arr.size(), 10)));
	} 
	
	public int insertShopper(MultipartFile profileImage, Shopper shopper) {
		
		inputService.isValidPeopleInput(shopper);
		
		shopperDao.insert(shopper);
		
		String imageUrl = "";
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			Map uploadResult;
			imgService.checkFileUpload(profileImage);
			try {

				uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Shoppers"));
			    imageUrl = (String) uploadResult.get("secure_url"); 

			    shopper.setUrl(imageUrl);
				shopperDao.updateUrl(shopper.getPhoneNum(), imageUrl);
			} 
			catch (IOException e) {
				throw new CustomException("Lỗi lưu ảnh");
			}		
		}
		 return 1;
	}
	
	public int updateShopper(MultipartFile profileImage, Shopper shopper) {
		
		inputService.isValidPeopleInput(shopper);
		
		shopperDao.update(shopper);
		String imageUrl = "";
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			imgService.checkFileUpload(profileImage);

			Map uploadResult;
			try {
				
				uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Shoppers"));
			    imageUrl = (String) uploadResult.get("secure_url");  // Lấy URL ảnh
				
			    shopper.setUrl(imageUrl);
				shopperDao.updateUrl(shopper.getPhoneNum(), imageUrl);
			} 
			catch (IOException e) {
				throw new CustomException("Lỗi đọc file");
			}		
		}		
		 return 1;
	}
	
	public int deleteShopper(Shopper emp) {
		 return shopperDao.delete(emp);
	}
	
    public Shopper findShopperByPhoneNum(String phoneNum) {
    	return shopperDao.getShopperByPhoneNum(phoneNum);
    } 
	
}

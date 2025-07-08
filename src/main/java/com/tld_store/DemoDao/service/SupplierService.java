package com.tld_store.DemoDao.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import dao.CombinedDAO;
import dao.SupplierDAO;
import dto.Address;
import dto.Supplier;
import exception.CustomException;


@Service
public class SupplierService {
	@Autowired
	SupplierDAO supplierDao;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private CombinedDAO combinedDao;
    @Autowired
    private UploadImgService imgService;
    @Autowired
    private InputService inputService;
    
    public Supplier findSupplierById(int id) {
    	return supplierDao.findById(id);
    }
    
	public ArrayList<Supplier> getAllSuppliers()
	{
		ArrayList<Supplier> arr = (ArrayList<Supplier>)supplierDao.getAll();
		return arr;
	}
	
	public Supplier getSupplierByPhone(String phoneNum) {
		return supplierDao.getSupplierByPhoneNum(phoneNum);
	}
	

	public ArrayList<Supplier> getTop10Suppliers(int numPage, AtomicBoolean isFinal){
		ArrayList<Supplier> arr = (ArrayList<Supplier>)supplierDao.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<>(arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Supplier> getSuppliersByFullName(String searchName, int numPage, AtomicBoolean isFinal)
	{
		ArrayList<Supplier> arr = supplierDao.getTop10SupplierByName(searchName.trim(), numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<>( arr.subList(0, Math.min(arr.size(), 10)));
	} 
	
	public int insertSupplier(MultipartFile profileImage, Supplier supplier, Address address) {
		inputService.isValidSupplierInput(supplier);
		
		String imageUrl = "";
		
		int supplierId = combinedDao.insertSupplier(supplier, address);
		
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			Map uploadResult;
			imgService.checkFileUpload(profileImage);

			try {
				uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Suppliers"));
			    imageUrl = (String) uploadResult.get("secure_url"); 
			    // Lấy URL ảnh
			    supplierDao.updateUrl(supplierId, imageUrl);
			} 
			catch (Exception e) {
				throw new CustomException(e.getMessage());
			}		
		}
		 return 1;
	}
	
	public int updateSupplier(MultipartFile profileImage, Supplier supplier, Address address) {
		imgService.checkFileUpload(profileImage);
		inputService.isValidSupplierInput(supplier);
		
		String imageUrl = "";
		
		if(address.getAddressId() == -1)
		{
			supplierDao.update(supplier);
		}
		else {
			combinedDao.updateSupplier(supplier, address);
		}
		if(profileImage != null && !profileImage.isEmpty()) {
		    // Upload ảnh lên Cloudinary
			Map uploadResult;
			imgService.checkFileUpload(profileImage);

			try {
				uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), 
				        ObjectUtils.asMap("resource_type", "auto", "folder", "TTCS/Suppliers"));
			    imageUrl = (String) uploadResult.get("secure_url");  // Lấy URL ảnh
			    
				supplierDao.updateUrl( supplier.getSupplierId(), imageUrl);
			} 
			catch (Exception e) {
				throw new CustomException(e.getMessage());
			}		
		}
		 return 1;
	}
	
	public int deleteSupplier(Supplier emp) {
		 return supplierDao.delete(emp);
	}
}

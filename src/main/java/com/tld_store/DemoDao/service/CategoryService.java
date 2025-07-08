package com.tld_store.DemoDao.service;

import java.util.ArrayList;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tld_store.DemoDao.imp.CategoryDAOImp;

import dao.CategoryDAO;
import dto.Product;
import dto.ProductCategory;

@Service
public class CategoryService {
	@Autowired
	private CategoryDAO cateDao;
	@Autowired
	private InputService inputService;
	
	public CategoryService()
	{
		cateDao = new CategoryDAOImp();
	}
	
	public ProductCategory findCategoryById(String id) {
		return cateDao.findById(id);
	}
	
	public ArrayList<ProductCategory> getAllCategories()
	{
		ArrayList<ProductCategory> arr = (ArrayList<ProductCategory>)cateDao.getAll();
		return arr;
	}
	
	public ArrayList<ProductCategory> getTop10Categories(int numPage, AtomicBoolean isFinal){
		ArrayList<ProductCategory> arr = (ArrayList<ProductCategory>)cateDao.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<>(arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public int insertCategory(ProductCategory cate) {
		inputService.isValidNameInput(cate.getName());
		
		cate.setName(inputService.formatName(cate.getName()));
		
		cate.setCategoryId(inputService.formatID(cate.getName()));
		 return cateDao.insert(cate);
	}
	
	public int updateCategory(ProductCategory cate) {
		inputService.isValidNameInput(cate.getName());
		
		cate.setName(inputService.formatName(cate.getName()));
		 return cateDao.update(cate);
	}
	
	public int deleteCategory(ProductCategory cate) {
		 return cateDao.delete(cate);
	}
	
	public ArrayList<ProductCategory> getTop10CategoriesByName(String categoryName, int numPage, AtomicBoolean isFinal){
		ArrayList<ProductCategory> arr = (ArrayList<ProductCategory>)cateDao.getProductCategoriesByName(categoryName.trim(), numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<ProductCategory>( arr.subList(0, Math.min(arr.size(), 10)));
	}

}

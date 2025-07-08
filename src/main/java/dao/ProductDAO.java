package dao;

import java.util.ArrayList;

import dto.Product;

public interface ProductDAO extends DAO<Product, String>{

	ArrayList<Product> findAllProductsByCategoryId(int categoryId);
	
	ArrayList<Product> findAllProductsByName(String productName);
	
	ArrayList<Product> findTop10ProductsByName(String productName, int numPage);
	
	int updateUrl(String id, String url);

	String insertProduct(Product t);

}

package dao;

import java.util.List;

import dto.ProductCategory;

public interface CategoryDAO extends DAO<ProductCategory, String> {
	List<ProductCategory> getProductCategoriesByName(String name, int pageNumber);
}

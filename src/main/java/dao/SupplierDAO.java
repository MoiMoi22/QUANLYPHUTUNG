package dao;

import java.util.ArrayList;

import dto.Supplier;

public interface SupplierDAO extends DAO<Supplier, Integer> {
	
	Supplier getSupplierByAddressId(int addressId);
	
	ArrayList<Supplier> getTop10SupplierByName(String name, int numPage);

	int updateUrl(int id, String token);

	Supplier getSupplierByPhoneNum(String phoneNum); 

}

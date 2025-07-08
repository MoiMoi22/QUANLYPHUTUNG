package dao;

import java.util.ArrayList;

import dto.District;

public interface DistrictDAO extends DAO<District, Integer> {
	
	ArrayList<District> getAllDistrictByProvinceById(int provinceId);
	
}

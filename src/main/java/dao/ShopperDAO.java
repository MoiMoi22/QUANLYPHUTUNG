package dao;

import java.util.ArrayList;

import dto.Shopper;

public interface ShopperDAO extends DAO<Shopper, Integer> {
	
	Shopper getShopperByOrderId(Integer orderId);
	
	ArrayList<Shopper> getTop10ShopperByFullName(String fullname, int numPage);
	
	Shopper getShopperByPhoneNum(String phoneNum);
	
	int updateUrl(String phoneNum, String url);
}

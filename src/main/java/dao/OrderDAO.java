package dao;

import java.util.ArrayList;

import dto.Order;

public interface OrderDAO extends DAO<Order, Integer>{
	
	ArrayList<Order> get10OrdersByShopperId(Integer shopperId, int numPage);
	
	ArrayList<Order> getAllOrdersByAddressId(Integer addressId);
	
	ArrayList<Order> get10OrdersBySalerId(Integer saleId, int numPage);
	
	ArrayList<Order> get10OrdersByAccountantId(Integer accountId, int numPage);
	
	ArrayList<Order> get10OrdersByWareHouseStaffId(Integer warehouseStaffId, int numPage);
	
	ArrayList<Integer> getAddressIdOfOrderByShopperId(Integer shopperId);
	
	ArrayList<Order> getAllOrderByStatus(String status);
	
	ArrayList<Order> get10OrderByStatuses(int numPage);
	
	ArrayList<Order> get10OrderByStatus(String status, int numPage);

	ArrayList<Order> get10OrderByShopperName(String name, int numPage);
	
	ArrayList<Order> get10OrderByShopperNameByStatuses(String name, int numPage);
	
	ArrayList<Order> get10OrderByShopperNameWithStatus(String name, String status, int numPage);
	
	ArrayList<Order> getOrdersByExcludedStatuses();

	ArrayList<Order> getAllOrderByStatuses();

	ArrayList<Order> getAllOrderByShopperNameByStatuses(String name);
	
}

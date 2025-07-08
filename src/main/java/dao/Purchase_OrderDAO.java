package dao;

import java.util.ArrayList;

import dto.Purchase_Order;

public interface Purchase_OrderDAO extends DAO<Purchase_Order, Integer> {

	ArrayList<Purchase_Order> get10Purchase_OrderBySupplierId(int supplierId, int numPage);
	
	ArrayList<Purchase_Order> get10Purchase_OrderByAccountantId(int accountantId, int numPage);
	
	ArrayList<Purchase_Order> get10Purchase_OrderByWarehouseStaffId(int warehouseStaffId, int numPage);
	
	ArrayList<Purchase_Order> get10Purchase_OrderBySupplierName(String name, int numPage);
	
	ArrayList<Purchase_Order> get10Purchase_OrderByStatuses(int numPage);
	
	ArrayList<Purchase_Order> get10Purchase_OrderBySupplierNameWithStatuses(String name, int numPage);

	ArrayList<Purchase_Order> getAllPurchase_OrderByStatuses();

	ArrayList<Purchase_Order> getAllPurchase_OrderBySupplierNameWithStatuses(String name);
	
}

package com.tld_store.DemoDao.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dao.CombinedDAO;
import dao.PurchaseOrderItemDAO;
import dao.Purchase_OrderDAO;
import dto.Employee;
import dto.Product;
import dto.Purchase_Order;
import dto.Purchase_Order_Item;
import exception.CustomException;

@Service
public class PurchaseService {
	@Autowired
	Purchase_OrderDAO poDAO;
	@Autowired
	PurchaseOrderItemDAO itemPoDAO;
	@Autowired
	CombinedDAO combineDAO;
	@Autowired
	EmployeeService empService;
	
	public Purchase_Order getPurchaseOrderByOrderId(int orderId) {
		Purchase_Order order = poDAO.findById(orderId);
		return order;
	}
	
	public ArrayList<Purchase_Order> getTop10Orders(int numPage, AtomicBoolean isFinal){
		ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Purchase_Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Purchase_Order> getTop10OrdersByAccountantId(int accountantId, int numPage, AtomicBoolean isFinal){
		ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.get10Purchase_OrderByAccountantId(accountantId, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Purchase_Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Purchase_Order> getTop10OrdersBySupplierId(int supplierId, int numPage, AtomicBoolean isFinal){
		ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.get10Purchase_OrderBySupplierId(supplierId, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Purchase_Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Purchase_Order> getTop10OrdersByWarehouseId(int warehouseId, int numPage, AtomicBoolean isFinal){
		ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.get10Purchase_OrderByWarehouseStaffId(warehouseId, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Purchase_Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Purchase_Order_Item> getAllItemByOrderId(int orderId){
		ArrayList<Purchase_Order_Item> itemList = itemPoDAO.getAllPurchaseOrderItemByPurchaseOrderId(orderId);
		return itemList;
	}
	
	public void handleRequestOrder(Map<String, Object> request) {
    	Purchase_Order order = new Purchase_Order();
    	ArrayList<Purchase_Order_Item> arr = new ArrayList<>();
    	
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy username
	        username = authentication.getName();
//	        String role = authentication.getAuthorities().stream()
//	                .map(grantedAuthority -> grantedAuthority.getAuthority())
//	                .findFirst()
//	                .orElse("ROLE_USER");
//	        if(!role.equals("ROLE_ACCOUNTANT")) {
//		    	throw new CustomException("Không có quyền tạo đơn");
//	        }
	    }
	    else {
	    	throw new CustomException("Không có quyền tạo đơn");
	    }
	    Employee emp = empService.findEmpByUsername(username);
    	
		order.setPeopleId((int)request.get("supplierId"));
		order.setAccountantId(emp.getId());
		order.setRemainingDebt((int) request.get("totalCost"));
		order.setStatus("PENDING");
		order.setTotalCost((int) request.get("totalCost"));
		addPurchaseOrderItems(arr, (ArrayList<Map<String, Object>>) request.get("parts"));
		
		combineDAO.insertPurchaseOrder(order, arr);
		
	}
	
    public void addPurchaseOrderItems(ArrayList<Purchase_Order_Item> arr, ArrayList<Map<String, Object>> requestArr) {
    	Product p = new Product();
    	for(Map<String, Object> m: requestArr) {
        	Purchase_Order_Item item = new Purchase_Order_Item();
    		item.setPricePerUnit((int) m.get("price"));
    		item.setProductId((String) m.get("productId"));
    		item.setQuantity((int)m.get("quantity"));
    		arr.add(item);
    	}
    }
    
    public ArrayList<Purchase_Order> get10PurchaseOrderBySupplierName(String name, int numPage, AtomicBoolean isFinal){
    	ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.get10Purchase_OrderBySupplierName(name, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Purchase_Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
    
	public ArrayList<Purchase_Order> getTop10OrdersWithStatuses(int numPage, AtomicBoolean isFinal){
		ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.get10Purchase_OrderByStatuses(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Purchase_Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
    public ArrayList<Purchase_Order> get10PurchaseOrderBySupplierNameWithStatuses(String name, int numPage, AtomicBoolean isFinal){
    	ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.get10Purchase_OrderBySupplierNameWithStatuses(name, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Purchase_Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
    
	public void acceptPurchaseOrder(int orderId, int warehouseStaffId) {
		Purchase_Order order = poDAO.findById(orderId);
		order.setWarehouseStaffId(warehouseStaffId);
		
		ArrayList<Purchase_Order_Item> items = itemPoDAO.getAllPurchaseOrderItemByPurchaseOrderId(orderId);
		
		combineDAO.acceptPurchaseOrder(order, items);
	}
	
	public void denyOrder(int orderId, int warehouseStaffId) {
		Purchase_Order order = poDAO.findById(orderId);
		order.setWarehouseStaffId(warehouseStaffId);
		
		combineDAO.denyPurchaseOrder(order);
	}
	
	public ArrayList<Purchase_Order> getAllOrdersWithStatuses(){
		ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.getAllPurchase_OrderByStatuses();
		return arr;
	}
	
	public ArrayList<Purchase_Order> getAllOrdersByShopperNameWithStatuses(String name){
		ArrayList<Purchase_Order> arr = (ArrayList<Purchase_Order>)poDAO.getAllPurchase_OrderBySupplierNameWithStatuses(name);
		return arr;
	}
	
}

package com.tld_store.DemoDao.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.AddressDAO;
import dao.CombinedDAO;
import dao.Item_OrderDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.PurchaseOrderItemDAO;
import dto.Address;
import dto.Employee;
import dto.Item_Order;
import dto.Order;
import dto.Product;
import dto.Purchase_Order_Item;
import dto.Shopper;
import exception.CustomException;

@Service
public class OrderService {
	@Autowired
	OrderDAO orderDAO;
	@Autowired
	Item_OrderDAO itemOrderDAO;
	@Autowired
	AddressDAO addressDAO;
	@Autowired
	ProductDAO productDAO;
	@Autowired
	CombinedDAO combineDAO;
	@Autowired
	EmployeeService empService;
	@Transactional
	public void addOrderWithItems(Order order, ArrayList<Item_Order> itemList) {
		combineDAO.insertOrder(order, itemList);
	}
	
	@Transactional
	public void addOrderWithItems(Order order, Address address, ArrayList<Item_Order> itemList) {
		combineDAO.insertOrder(order, itemList, address);
	}
	
	public void handleRequestOrder(Map<String, Object> request) {
    	ObjectMapper mapper = new ObjectMapper();
    	Map<String, Object> shopper = mapper.convertValue(request.get("customer"), Map.class);
    	
		int addressId = Integer.parseInt(String.valueOf(shopper.get("addressId")));
		System.out.println(addressId);
    	Order order = new Order();
    	Address address = null;
    	ArrayList<Item_Order> arr = new ArrayList<>();
    	if(addressId == -1)
    	{
    		address = new Address();
    		address.setAddressName(String.valueOf(shopper.get("street")));
    		address.setCommuneWardId(Integer.parseInt(String.valueOf(shopper.get("commune"))));
    	}
    	else {
    		order.setShippingAddressId(addressId);
    	}
   		order.setDiscount((int)request.get("discount"));
		order.setPeopleId((int)shopper.get("shopperId"));
		order.setRemainingDebt((int) request.get("totalCost"));
		order.setStatus("PENDING");
		order.setTotalCost((int) request.get("totalCost"));
		addItem_Orders(arr, (ArrayList<Map<String, Object>>) request.get("parts"));
		
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = "";
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Lấy username
	        username = authentication.getName();
	    }
	    else {
	    	throw new CustomException("Không có quyền tạo đơn");
	    }
	    Employee emp = empService.findEmpByUsername(username);
	    order.setSalesId(emp.getId());
	    order.setStatus("PENDING");
		if(address == null)
		{
			addOrderWithItems(order, arr);
		}
		else {
			addOrderWithItems(order, address, arr);
		}
		
	}
    public void addItem_Orders(ArrayList<Item_Order> arr, ArrayList<Map<String, Object>> requestArr) {
    	Product p = new Product();
    	for(Map<String, Object> m: requestArr) {
        	p = productDAO.findById((String)m.get("productId"));
    		Item_Order item = new Item_Order();
    		item.setDiscount((int)m.get("discount"));
    		item.setPricePerUnit(p.getPrice());
    		item.setProductId(p.getProductId());
    		item.setQuantity((int)m.get("quantity"));
    		arr.add(item);
    	}
    }
    
	
	@Transactional
	public void deleteOrderWithItems(Order order) {
		ArrayList<Item_Order> itemList = itemOrderDAO.getAllItemOrderByOrderId(order.getOrderId());
		try {
			for (Item_Order item: itemList) {
				itemOrderDAO.delete(item);
			}
			orderDAO.delete(order);
		}catch (Exception e) {
			throw new CustomException("Loi xóa order va item");
		}
	}
	
	public Order getOrderByOrderId(int orderId) {
		Order order = orderDAO.findById(orderId);
		return order;
	}
	
	public ArrayList<Order> getTop10Orders(int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.get10(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Order> getTop10OrdersByShopper(int shopperId, int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.get10OrdersByShopperId(shopperId, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Order> getTop10OrdersByAccountantId(int accountantId, int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.get10OrdersByAccountantId(accountantId, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Order> getTop10OrdersBySaleId(int saleId, int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.get10OrdersBySalerId(saleId, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Item_Order> getAllItemByOrderId(int orderId){
		ArrayList<Item_Order> itemList = itemOrderDAO.getAllItemOrderByOrderId(orderId);
		return itemList;
	}
	
	public ArrayList<Integer> getAllAddressIdByShopperId(int shopperId){
		ArrayList<Integer> itemList = orderDAO.getAddressIdOfOrderByShopperId(shopperId);
		return itemList;
	}
	
	public void updateStatus(int orderId, int warehouseId) {
		Order order = getOrderByOrderId(orderId);
		order.setWarehouseStaffId(warehouseId);
		order.setStatus("COMPLETED");
		orderDAO.update(order);
	}
	
	public void updateOrder(Order order) {
		orderDAO.update(order);
	}
	
	public ArrayList<Order> getAllOrderByStatus(String status){
		ArrayList<Order> orders = orderDAO.getAllOrderByStatus(status);
		return orders;
	}
	
	public ArrayList<Order> getAllOrderByStatuses(){
		ArrayList<Order> orders = orderDAO.getOrdersByExcludedStatuses();
		return orders;
	}
	
	public ArrayList<Order> get10OrderByShoperName(String name, int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = orderDAO.get10OrderByShopperName(name, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Order> get10OrderByStatus(String status, int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.get10OrderByStatus(status, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Order> get10OrderByShoperNameWithStatus(String name, String status, int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = orderDAO.get10OrderByShopperNameWithStatus(name, status, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Order> get10OrderByStatuses(int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.get10OrderByStatuses(numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public ArrayList<Order> get10OrderByShoperNameWithStatuses(String name, int numPage, AtomicBoolean isFinal){
		ArrayList<Order> arr = orderDAO.get10OrderByShopperNameByStatuses(name, numPage);
		if(arr.size() < 11)
		{
			isFinal.set(true);
		}
		else {
			isFinal.set(false);
		}
		return new ArrayList<Order>( arr.subList(0, Math.min(arr.size(), 10)));
	}
	
	public void acceptOrder(int orderId, int warehouseStaffId) {
		Order order = orderDAO.findById(orderId);
		order.setWarehouseStaffId(warehouseStaffId);
		
		ArrayList<Item_Order> items = itemOrderDAO.getAllItemOrderByOrderId(orderId);
		
		combineDAO.acceptOrder(order, items);
	}
	
	public void denyOrder(int orderId,  int warehouseStaffId) {
		Order order = orderDAO.findById(orderId);
		order.setWarehouseStaffId(warehouseStaffId);

		combineDAO.denyOrder(order);
	}
	
	public ArrayList<Order> getAllOrdersWithStatuses(){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.getAllOrderByStatuses();
		return arr;
	}
	
	public ArrayList<Order> getAllOrdersByShopperNameWithStatuses(String name){
		ArrayList<Order> arr = (ArrayList<Order>)orderDAO.getAllOrderByShopperNameByStatuses(name);
		return arr;
	}
	
}

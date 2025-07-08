package dao;

import java.util.ArrayList;

import dto.Item_Order;

public interface Item_OrderDAO extends DAO<Item_Order, Integer> {
	
	ArrayList<Item_Order> getAllItemOrderByOrderId(int orderId);
	
}

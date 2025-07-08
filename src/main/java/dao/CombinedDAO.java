package dao;

import java.util.ArrayList;

import dto.Address;
import dto.Item_Order;
import dto.Order;
import dto.Purchase_Order;
import dto.Purchase_Order_Item;
import dto.Supplier;
import dto.Transaction;

public interface CombinedDAO {
	
	public void insertOrder(Order order, ArrayList<Item_Order> itemList);
	
	public void insertOrder(Order order, ArrayList<Item_Order> itemList, Address address);
	
	public void insertPurchaseOrder(Purchase_Order order, ArrayList<Purchase_Order_Item> itemList);
	
	public void insertTransaction(Transaction transaction, String transactionType);
	
	public void deleteTransaction(Transaction transaction, String transactionType);
	
	public int insertSupplier(Supplier supplier, Address address);
	
	public void updateSupplier(Supplier supplier, Address address);
	
	public void acceptOrder(Order order, ArrayList<Item_Order> itemList);
	
	public void denyOrder(Order order);
	
	public void acceptPurchaseOrder(Purchase_Order order, ArrayList<Purchase_Order_Item> itemList);
	
	public void denyPurchaseOrder(Purchase_Order order);
	
}

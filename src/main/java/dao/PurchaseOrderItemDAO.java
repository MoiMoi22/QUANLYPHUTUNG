package dao;

import java.util.ArrayList;

import dto.Purchase_Order_Item;

public interface PurchaseOrderItemDAO extends DAO<Purchase_Order_Item, Integer> {
	
	ArrayList<Purchase_Order_Item> getAllPurchaseOrderItemByPurchaseOrderId(int purchaseOrderId);

	ArrayList<Purchase_Order_Item> getAllPurchaseOrderItemByProductId(int productId);
	
}

package dto;

public class Purchase_Order_Item extends ItemTemplate {

	public Purchase_Order_Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Purchase_Order_Item(int itemId, int orderId, String productId, int quantity, float pricePerUnit) {
		super(itemId, orderId, productId, quantity, pricePerUnit);
		// TODO Auto-generated constructor stub
	}

}

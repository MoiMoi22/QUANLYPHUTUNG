package dto;

public class Item_Order extends ItemTemplate {
	private float discount;
	
	public Item_Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Item_Order(int itemId, int orderId, String productId, int quantity, float pricePerUnit, float discount) {
		super(itemId, orderId, productId, quantity, pricePerUnit);
		this.discount = discount;
		// TODO Auto-generated constructor stub
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
	
}

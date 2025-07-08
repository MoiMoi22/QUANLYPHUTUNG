package dto;

public abstract class ItemTemplate {
	private int itemId;
	private int orderId;
	private String productId;
	private int quantity;
	private float pricePerUnit;
	
	public ItemTemplate() {}

	public ItemTemplate(int itemId, int orderId, String productId, int quantity, float pricePerUnit) {
		super();
		this.itemId = itemId;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(float pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	
	

}

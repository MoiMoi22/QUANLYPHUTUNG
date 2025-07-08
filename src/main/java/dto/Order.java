package dto;

import java.sql.Date;

public class Order extends OrderTemplate {
	private float discount;
	private String cancelReason;
    private int shippingAddressId;
    private int salesId;
    
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(int orderId, int peopleId, Date orderDate, String status, float totalCost,
			float remainingDebt, int accountantId, int warehouseStaffId, float discount, String cancelReason
			,int shippingAddressId, int salesId) {
		super(orderId, peopleId, orderDate, status, totalCost, remainingDebt, accountantId, warehouseStaffId);
		this.discount = discount;
		this.cancelReason = cancelReason;
		this.shippingAddressId = shippingAddressId;
		this.salesId = salesId;
	}



	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public int getShippingAddressId() {
		return shippingAddressId;
	}

	public void setShippingAddressId(int shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public int getSalesId() {
		return salesId;
	}

	public void setSalesId(int salesId) {
		this.salesId = salesId;
	}
    
}

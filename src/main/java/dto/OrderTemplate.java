package dto;

import java.sql.Date;

public abstract class OrderTemplate {
	private int orderId;
	private int peopleId;
	private Date orderDate;
	private String status;
	private float totalCost;
	private float remainingDebt;
	private int accountantId;
	private int warehouseStaffId;
	
	public OrderTemplate() {}
	
	public OrderTemplate(int orderId, int peopleId, Date orderDate, String status,
			float totalCost, float remainingDebt, int accountantId, int warehouseStaffId) {
		super();
		this.orderId = orderId;
		this.peopleId = peopleId;
		this.orderDate = orderDate;
		this.status = status;
		this.totalCost = totalCost;
		this.remainingDebt = remainingDebt;
		this.accountantId = accountantId;
		this.warehouseStaffId = warehouseStaffId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(int peopleId) {
		this.peopleId = peopleId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

	public float getRemainingDebt() {
		return remainingDebt;
	}

	public void setRemainingDebt(float remainingDebt) {
		this.remainingDebt = remainingDebt;
	}

	public int getAccountantId() {
		return accountantId;
	}

	public void setAccountantId(int accountantId) {
		this.accountantId = accountantId;
	}

	public int getWarehouseStaffId() {
		return warehouseStaffId;
	}

	public void setWarehouseStaffId(int warehouseStaffId) {
		this.warehouseStaffId = warehouseStaffId;
	}
	
}

package dto;

import java.sql.Date;

public class Purchase_Order extends OrderTemplate {

	public Purchase_Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Purchase_Order(int orderId, int peopleId, Date orderDate, String status,
			float totalCost, float remainingDebt, int accountantId, int warehouseStaffId) {
		super(orderId, peopleId, orderDate, status, totalCost, remainingDebt, accountantId, warehouseStaffId);
		// TODO Auto-generated constructor stub
	}

}

package com.tld_store.DemoDao.imp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dao.Database;
import dao.OrderDAO;
import dto.Order;
import error_handling.ErrorMessages;
import exception.CustomException;
@Repository
public class OrderDAOImp implements OrderDAO {

    @Override
    public Order findById(Integer id){
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Order order = null;

        String sql = "SELECT * FROM Orders WHERE Order_ID = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return order;
    }

    @Override
    public List<Order> getAll(){
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM Orders";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
    }

    @Override
    public int insert(Order order){
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;

        String sql = "INSERT INTO Orders (Shopper_ID, Order_Date, Discount, Order_Total, "
                   + "Remaining_Debt, Cancel_Reason, Status, "
                   + "Shipping_Address_ID, Sales_ID, Accountant_ID, Warehouse_Staff_ID) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, order.getPeopleId());
            ps.setDate(2, order.getOrderDate());
            ps.setFloat(3, order.getDiscount());
            ps.setFloat(4, order.getTotalCost());
            ps.setFloat(5, order.getRemainingDebt());
            ps.setString(6, order.getCancelReason());
            ps.setString(7, order.getStatus());
            ps.setInt(8, order.getShippingAddressId());
            ps.setInt(9, order.getSalesId());
            ps.setNull(10, java.sql.Types.INTEGER);
            ps.setNull(11, java.sql.Types.INTEGER);

            ps.executeUpdate();
        }
		catch(SQLException e)
		{
            Database.closePreparedStatement(ps);
            Database.closeConnection(con);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);
        return result;
    }

    @Override
    public int update(Order order){
        Connection con = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;

        String sql = "UPDATE Orders SET "
        		+ "Shopper_ID = ?, "
                + "Order_Date = ?, "
                + "Discount = ?, "
                + "Order_Total = ?, "
                + "Remaining_Debt = ?, "
                + "Cancel_Reason = ?, "
                + "Status = ?, "
                + "Shipping_Address_ID = ?, "
                + "Sales_ID = ?, "
                + "Accountant_ID = ?, "
                + "Warehouse_Staff_ID = ? "
                + "WHERE Order_ID = ?";

     try {
         ps = con.prepareStatement(sql);
         ps.setInt(1, order.getPeopleId());
         ps.setDate(2, order.getOrderDate());
         ps.setFloat(3, order.getDiscount());
         ps.setFloat(4, order.getTotalCost());
         ps.setFloat(5, order.getRemainingDebt());
         ps.setString(6, order.getCancelReason());
         ps.setString(7, order.getStatus());
         ps.setInt(8, order.getShippingAddressId());
         ps.setInt(9, order.getSalesId());
         ps.setInt(10, order.getAccountantId());
         ps.setInt(11, order.getWarehouseStaffId());
         ps.setInt(12, order.getOrderId()); // Điều kiện WHERE

         ps.executeUpdate();
        }
		catch(SQLException e)
		{
         Database.closePreparedStatement(ps);
         Database.closeConnection(con);
         throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
     	Database.closePreparedStatement(ps);
     	Database.closeConnection(con);
     	return result;
    }

    @Override
    public int delete(Order order){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Orders");
			cbst.setString(2, Integer.toString(order.getOrderId()));

			cbst.execute();			
		}
		catch(SQLException e)
		{
			Database.closeCallableStatement(cbst);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeCallableStatement(cbst);
		Database.closeConnection(con);
		return result;
    }

	@Override
	public ArrayList<Order> get10OrdersByShopperId(Integer shopperId, int numPage){
 
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	  "WHERE Shopper_ID = ?\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, shopperId);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public ArrayList<Order> getAllOrdersByAddressId(Integer addressId){

		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM Orders where Shipping_Address_ID = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, addressId);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public ArrayList<Order> get10OrdersBySalerId(Integer saleId, int numPage){
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	  "WHERE Sales_ID = ?\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, saleId);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public ArrayList<Order> get10OrdersByAccountantId(Integer accountId, int numPage){
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	  "WHERE Accountant_ID = ?\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, accountId);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public ArrayList<Order> get10OrdersByWareHouseStaffId(Integer warehouseStaffId, int numPage){
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	  "WHERE Warehouse_Staff_ID = ?\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, warehouseStaffId);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public List<Order> get10(int numPage) {
	      Connection con = Database.getConnection();
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        List<Order> orders = new ArrayList<>();

			int offset = (numPage-1) * 10;
			
			//Truy vấn các employees trong 1 table
			String sql =  "SELECT *\n"
					+     "FROM Orders\n"
					+ 	  "ORDER BY Order_ID DESC\n"
					+ 	  "OFFSET ? ROWS \n"
					+     "FETCH NEXT 11 ROW ONLY";
	        try {
	            ps = con.prepareStatement(sql);
	            ps.setInt(1, offset);
	            rs = ps.executeQuery();

	            while (rs.next()) {
	                Order order = new Order(
	                    rs.getInt("Order_ID"),
	                    rs.getInt("Shopper_ID"),
	                    rs.getDate("Order_Date"),
	                    rs.getString("Status"),
	                    rs.getFloat("Order_Total"),
	                    rs.getFloat("Remaining_Debt"),
	                    rs.getInt("Accountant_ID"),
	                    rs.getInt("Warehouse_Staff_ID"),
	                    rs.getFloat("Discount"),
	                    rs.getString("Cancel_Reason"),
	                    rs.getInt("Shipping_Address_ID"),
	                    rs.getInt("Sales_ID")
	                );
	                orders.add(order);
	            }
	        }
	        catch(SQLException e)
			{
				Database.closeResultSet(rs);
				Database.closePreparedStatement(ps);
				Database.closeConnection(con);
				throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
			}
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);

	        return orders;
	}

	@Override
	public ArrayList<Integer> getAddressIdOfOrderByShopperId(Integer shopperId) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Integer> addressList = new ArrayList<>();
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT DISTINCT Shipping_Address_ID\n"
				+     "FROM Orders\n"
				+ 	  "WHERE Shopper_ID = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, shopperId);
            rs = ps.executeQuery();

            while (rs.next()) {
                int addressId = rs.getInt("Shipping_Address_ID");
                addressList.add(addressId);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return addressList;
	}

	@Override
	public ArrayList<Order> getAllOrderByStatus(String status) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	  "WHERE Status = ?\n"
				+ 	  "ORDER BY Order_ID DESC\n";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public ArrayList<Order> getOrdersByExcludedStatuses() {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    ArrayList<Order> orders = new ArrayList<>();

	    // Truy vấn với điều kiện Status != 'PENDING' và Status != 'COMPLETED'
	    String sql = "SELECT *\n"
	               + "FROM Orders\n"
	               + "WHERE Status != ? AND Status != ?\n"
	               + "ORDER BY Order_ID DESC";

	    try {
	        ps = con.prepareStatement(sql);
	        ps.setString(1, "PENDING");
	        ps.setString(2, "COMPLETED");
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            Order order = new Order(
	                rs.getInt("Order_ID"),
	                rs.getInt("Shopper_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Order_Total"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID"),
	                rs.getFloat("Discount"),
	                rs.getString("Cancel_Reason"),
	                rs.getInt("Shipping_Address_ID"),
	                rs.getInt("Sales_ID")
	            );
	            orders.add(order);
	        }
	    } catch (SQLException e) {
	        Database.closeResultSet(rs);
	        Database.closePreparedStatement(ps);
	        Database.closeConnection(con);
	        throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    }

	    Database.closeResultSet(rs);
	    Database.closePreparedStatement(ps);
	    Database.closeConnection(con);

	    return orders;
	}

	
	@Override
	public ArrayList<Order> get10OrderByStatus(String status, int numPage) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	  "WHERE Status = ?\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("Order_ID"),
                    rs.getInt("Shopper_ID"),
                    rs.getDate("Order_Date"),
                    rs.getString("Status"),
                    rs.getFloat("Order_Total"),
                    rs.getFloat("Remaining_Debt"),
                    rs.getInt("Accountant_ID"),
                    rs.getInt("Warehouse_Staff_ID"),
                    rs.getFloat("Discount"),
                    rs.getString("Cancel_Reason"),
                    rs.getInt("Shipping_Address_ID"),
                    rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public ArrayList<Order> get10OrderByShopperName(String name, int numPage) {

		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	"LEFT JOIN Shoppers\r\n"
        		+ 	"ON Orders.Shopper_ID = Shoppers.Shopper_ID\n"
        		+ 	"WHERE (Last_Name + ' ' + First_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%')\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                		rs.getInt("Order_ID"),
                        rs.getInt("Shopper_ID"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status"),
                        rs.getFloat("Order_Total"),
                        rs.getFloat("Remaining_Debt"),
                        rs.getInt("Accountant_ID"),
                        rs.getInt("Warehouse_Staff_ID"),
                        rs.getFloat("Discount"),
                        rs.getString("Cancel_Reason"),
                        rs.getInt("Shipping_Address_ID"),
                        rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;

	}

	@Override
	public ArrayList<Order> get10OrderByShopperNameWithStatus(String name, String status, int numPage) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	"LEFT JOIN Shoppers\r\n"
        		+ 	"ON Orders.Shopper_ID = Shoppers.Shopper_ID\n"
        		+ 	"WHERE (Last_Name + ' ' + First_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%') AND Status = ?\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, status);
            ps.setInt(3, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                		rs.getInt("Order_ID"),
                        rs.getInt("Shopper_ID"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status"),
                        rs.getFloat("Order_Total"),
                        rs.getFloat("Remaining_Debt"),
                        rs.getInt("Accountant_ID"),
                        rs.getInt("Warehouse_Staff_ID"),
                        rs.getFloat("Discount"),
                        rs.getString("Cancel_Reason"),
                        rs.getInt("Shipping_Address_ID"),
                        rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}

	@Override
	public ArrayList<Order> get10OrderByStatuses(int numPage) {
	      Connection con = Database.getConnection();
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        ArrayList<Order> orders = new ArrayList<>();

			int offset = (numPage-1) * 10;
			
			//Truy vấn các employees trong 1 table
			String sql =  "SELECT *\n"
					+     "FROM Orders\n"
		            + 	  "WHERE Status = 'IN DEBT' OR Status = 'PAID' \n"					
					+ 	  "ORDER BY Order_ID DESC\n"
					+ 	  "OFFSET ? ROWS \n"
					+     "FETCH NEXT 11 ROW ONLY";
	        try {
	            ps = con.prepareStatement(sql);
	            ps.setInt(1, offset);
	            rs = ps.executeQuery();

	            while (rs.next()) {
	                Order order = new Order(
	                    rs.getInt("Order_ID"),
	                    rs.getInt("Shopper_ID"),
	                    rs.getDate("Order_Date"),
	                    rs.getString("Status"),
	                    rs.getFloat("Order_Total"),
	                    rs.getFloat("Remaining_Debt"),
	                    rs.getInt("Accountant_ID"),
	                    rs.getInt("Warehouse_Staff_ID"),
	                    rs.getFloat("Discount"),
	                    rs.getString("Cancel_Reason"),
	                    rs.getInt("Shipping_Address_ID"),
	                    rs.getInt("Sales_ID")
	                );
	                orders.add(order);
	            }
	        }
	        catch(SQLException e)
			{
				Database.closeResultSet(rs);
				Database.closePreparedStatement(ps);
				Database.closeConnection(con);
				throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
			}
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);

	        return orders;
	}

	@Override
	public ArrayList<Order> get10OrderByShopperNameByStatuses(String name, int numPage) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	"LEFT JOIN Shoppers\r\n"
        		+ 	"ON Orders.Shopper_ID = Shoppers.Shopper_ID\n"
        		+ 	"WHERE (Last_Name + ' ' + First_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%') (Status = 'IN DEBT' or Status = 'PAID')\n"
				+ 	  "ORDER BY Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                		rs.getInt("Order_ID"),
                        rs.getInt("Shopper_ID"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status"),
                        rs.getFloat("Order_Total"),
                        rs.getFloat("Remaining_Debt"),
                        rs.getInt("Accountant_ID"),
                        rs.getInt("Warehouse_Staff_ID"),
                        rs.getFloat("Discount"),
                        rs.getString("Cancel_Reason"),
                        rs.getInt("Shipping_Address_ID"),
                        rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}
	
	@Override
	public ArrayList<Order> getAllOrderByStatuses() {
	      Connection con = Database.getConnection();
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        ArrayList<Order> orders = new ArrayList<>();
			
			//Truy vấn các employees trong 1 table
			String sql =  "SELECT *\n"
					+     "FROM Orders\n"
		            + 	  "WHERE Status = 'IN DEBT' OR Status = 'PAID' \n"					
					+ 	  "ORDER BY Order_ID DESC";
	        try {
	            ps = con.prepareStatement(sql);
	            rs = ps.executeQuery();

	            while (rs.next()) {
	                Order order = new Order(
	                    rs.getInt("Order_ID"),
	                    rs.getInt("Shopper_ID"),
	                    rs.getDate("Order_Date"),
	                    rs.getString("Status"),
	                    rs.getFloat("Order_Total"),
	                    rs.getFloat("Remaining_Debt"),
	                    rs.getInt("Accountant_ID"),
	                    rs.getInt("Warehouse_Staff_ID"),
	                    rs.getFloat("Discount"),
	                    rs.getString("Cancel_Reason"),
	                    rs.getInt("Shipping_Address_ID"),
	                    rs.getInt("Sales_ID")
	                );
	                orders.add(order);
	            }
	        }
	        catch(SQLException e)
			{
				Database.closeResultSet(rs);
				Database.closePreparedStatement(ps);
				Database.closeConnection(con);
				throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
			}
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);

	        return orders;
	}

	@Override
	public ArrayList<Order> getAllOrderByShopperNameByStatuses(String name) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Order> orders = new ArrayList<>();
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Orders\n"
				+ 	"LEFT JOIN Shoppers\r\n"
        		+ 	"ON Orders.Shopper_ID = Shoppers.Shopper_ID\n"
        		+ 	"WHERE (Last_Name + ' ' + First_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%') (Status = 'IN DEBT' or Status = 'PAID')\n"
				+ 	  "ORDER BY Order_ID DESC";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                		rs.getInt("Order_ID"),
                        rs.getInt("Shopper_ID"),
                        rs.getDate("Order_Date"),
                        rs.getString("Status"),
                        rs.getFloat("Order_Total"),
                        rs.getFloat("Remaining_Debt"),
                        rs.getInt("Accountant_ID"),
                        rs.getInt("Warehouse_Staff_ID"),
                        rs.getFloat("Discount"),
                        rs.getString("Cancel_Reason"),
                        rs.getInt("Shipping_Address_ID"),
                        rs.getInt("Sales_ID")
                );
                orders.add(order);
            }
        }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);

        return orders;
	}
	

}

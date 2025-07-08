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
import dao.Purchase_OrderDAO;
import dto.Purchase_Order;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class Purchase_OrderDAOImp implements Purchase_OrderDAO{

	@Override
	public Purchase_Order findById(Integer id){
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    Purchase_Order order = null;

	    String sql = "SELECT * FROM Purchase_Orders WHERE Purchase_Order_ID = ?";
	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, id);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            order = new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            );
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return order;
	}

	@Override
	public List<Purchase_Order> getAll(){
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    List<Purchase_Order> orders = new ArrayList<>();

	    String sql = "SELECT * FROM Purchase_Orders";
	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            orders.add(new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            ));
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return orders;
	}

	@Override
	public int insert(Purchase_Order t)  {
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = 1;

	    String sql = "INSERT INTO Purchase_Orders (Supplier_ID, Order_Date, Status, " +
	                 "Total_Cost, Remaining_Debt, Accountant_ID, Warehouse_Staff_ID) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, t.getPeopleId());
	        ps.setDate(2, t.getOrderDate());
	        ps.setString(3, t.getStatus());
	        ps.setFloat(4, t.getTotalCost());
	        ps.setFloat(5, t.getRemainingDebt());
	        ps.setInt(6, t.getAccountantId());
	        ps.setInt(7, t.getWarehouseStaffId());

	        ps.executeUpdate();
	    }
		catch(SQLException e)
		{
            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
        Database.closePreparedStatement(ps);
        Database.closeConnection(conn);
        return result;
	}

	@Override
	public int update(Purchase_Order t)  {
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;

	    int result = 1;
	    
	    String sql = "UPDATE Purchase_Orders SET Supplier_ID = ?, Order_Date = ?, " +
	                 "Status = ?, Total_Cost = ?, Remaining_Debt = ?, Accountant_ID = ?, Warehouse_Staff_ID = ? " +
	                 "WHERE Purchase_Order_ID = ?";
	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, t.getPeopleId());
	        ps.setDate(2, t.getOrderDate());
	        ps.setString(3, t.getStatus());
	        ps.setFloat(4, t.getTotalCost());
	        ps.setFloat(5, t.getRemainingDebt());
	        ps.setInt(6, t.getAccountantId());
	        ps.setInt(7, t.getWarehouseStaffId());
	        ps.setInt(8, t.getOrderId());

	        ps.executeUpdate();
	    }
		catch(SQLException e)
		{
            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
        Database.closePreparedStatement(ps);
        Database.closeConnection(conn);
        return result;
	}

	@Override
	public int delete(Purchase_Order t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Purchase_Orders");
			cbst.setString(2, Integer.toString(t.getOrderId()));

			cbst.execute();			
		}
		catch(SQLException e)
		{
			Database.closeCallableStatement(cbst);;
			Database.closeConnection(con);
			throw new CustomException(e.getMessage());
		}
		Database.closeCallableStatement(cbst);;
		Database.closeConnection(con);
		return result;
	}

	@Override
	public ArrayList<Purchase_Order> get10Purchase_OrderBySupplierId(int supplierId, int numPage)  {
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    ArrayList<Purchase_Order> orders = new ArrayList<>();

	    String sql = "SELECT *\n"
	    		+ "FROM Purchase_Orders\n"
	    		+ "WHERE Supplier_ID = ?\n"
	    		+ "ORDER BY Purchase_Order_ID DESC\n"
				+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
				+ "FETCH NEXT 11 ROW ONLY";;
	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, supplierId);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            orders.add(new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            ));
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return orders;
	}

	@Override
	public ArrayList<Purchase_Order> get10Purchase_OrderByAccountantId(int accountantId, int numPage){
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    ArrayList<Purchase_Order> orders = new ArrayList<>();

	    String sql = "SELECT *\n"
	    		+ "FROM Purchase_Orders\n"
	    		+ "WHERE Accountant_ID = ?\n"
	    		+ "ORDER BY Purchase_Order_ID DESC\n"
				+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
				+ "FETCH NEXT 11 ROW ONLY";;
	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, accountantId);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            orders.add(new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            ));
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return orders;
	}

	@Override
	public ArrayList<Purchase_Order> get10Purchase_OrderByWarehouseStaffId(int warehouseStaffId, int numPage){
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    ArrayList<Purchase_Order> orders = new ArrayList<>();

	    String sql = "SELECT *\n"
	    			+ "FROM Purchase_Orders\n"
	    			+ "WHERE Warehouse_Staff_ID = ?\n"
	    			+ "ORDER BY Purchase_Order_ID DESC\n"
					+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
					+ "FETCH NEXT 11 ROW ONLY";
	    try {
	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, warehouseStaffId);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            orders.add(new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            ));
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return orders;
	}

	@Override
	public List<Purchase_Order> get10(int numPage) {
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    List<Purchase_Order> orders = new ArrayList<>();

	    String sql =  "SELECT *\n"
	            + "FROM Purchase_Orders\n"
	            + "ORDER BY Purchase_Order_ID DESC\n"
	            + "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
	            + "FETCH NEXT 11 ROWS ONLY";
	    
	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            orders.add(new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            ));
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return orders;
	}

	@Override
	public ArrayList<Purchase_Order> get10Purchase_OrderBySupplierName(String name, int numPage) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Purchase_Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Purchase_Orders\n"
				+ 	"LEFT JOIN Suppliers\r\n"
        		+ 	"ON Purchase_Orders.Supplier_ID = Suppliers.Supplier_ID\n"
        		+ 	"WHERE (Company_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%')\n"
				+ 	  "ORDER BY Purchase_Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Purchase_Order order = new Purchase_Order(
                		rs.getInt("Purchase_Order_ID"),
    	                rs.getInt("Supplier_ID"),
    	                rs.getDate("Order_Date"),
    	                rs.getString("Status"),
    	                rs.getFloat("Total_Cost"),
    	                rs.getFloat("Remaining_Debt"),
    	                rs.getInt("Accountant_ID"),
    	                rs.getInt("Warehouse_Staff_ID")
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
	public ArrayList<Purchase_Order> get10Purchase_OrderByStatuses(int numPage) {
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    ArrayList<Purchase_Order> orders = new ArrayList<>();

	    String sql =  "SELECT *\n"
	            + "FROM Purchase_Orders\n"
	            + "WHERE Status = 'IN DEBT' OR Status = 'PAID' \n"
	            + "ORDER BY Purchase_Order_ID DESC\n"
	            + "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
	            + "FETCH NEXT 11 ROWS ONLY";
	    
	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            orders.add(new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            ));
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return orders;
	}

	@Override
	public ArrayList<Purchase_Order> getAllPurchase_OrderByStatuses() {
		Connection conn = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    ArrayList<Purchase_Order> orders = new ArrayList<>();

	    String sql =  "SELECT *\n"
	            + "FROM Purchase_Orders\n"
	            + "WHERE Status = 'IN DEBT' OR Status = 'PAID' \n"
	            + "ORDER BY Purchase_Order_ID DESC";
	    
	    try {
	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            orders.add(new Purchase_Order(
	                rs.getInt("Purchase_Order_ID"),
	                rs.getInt("Supplier_ID"),
	                rs.getDate("Order_Date"),
	                rs.getString("Status"),
	                rs.getFloat("Total_Cost"),
	                rs.getFloat("Remaining_Debt"),
	                rs.getInt("Accountant_ID"),
	                rs.getInt("Warehouse_Staff_ID")
	            ));
	        }
	    }
        catch(SQLException e)
		{
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(conn);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(conn);

        return orders;
	}
	
	@Override
	public ArrayList<Purchase_Order> get10Purchase_OrderBySupplierNameWithStatuses(String name, int numPage) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Purchase_Order> orders = new ArrayList<>();

        int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Purchase_Orders\n"
				+ 	"LEFT JOIN Suppliers\r\n"
        		+ 	"ON Purchase_Orders.Supplier_ID = Suppliers.Supplier_ID\n"
        		+ 	"WHERE (Company_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%') AND (Status = 'IN DEBT' or Status = 'PAID')\n"
				+ 	  "ORDER BY Purchase_Order_ID DESC\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                Purchase_Order order = new Purchase_Order(
                		rs.getInt("Purchase_Order_ID"),
    	                rs.getInt("Supplier_ID"),
    	                rs.getDate("Order_Date"),
    	                rs.getString("Status"),
    	                rs.getFloat("Total_Cost"),
    	                rs.getFloat("Remaining_Debt"),
    	                rs.getInt("Accountant_ID"),
    	                rs.getInt("Warehouse_Staff_ID")
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
	public ArrayList<Purchase_Order> getAllPurchase_OrderBySupplierNameWithStatuses(String name) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Purchase_Order> orders = new ArrayList<>();
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Purchase_Orders\n"
				+ 	"LEFT JOIN Suppliers\r\n"
        		+ 	"ON Purchase_Orders.Supplier_ID = Suppliers.Supplier_ID\n"
        		+ 	"WHERE (Company_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%') AND (Status = 'IN DEBT' or Status = 'PAID')\n"
				+ 	  "ORDER BY Purchase_Order_ID DESC";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();

            while (rs.next()) {
                Purchase_Order order = new Purchase_Order(
                		rs.getInt("Purchase_Order_ID"),
    	                rs.getInt("Supplier_ID"),
    	                rs.getDate("Order_Date"),
    	                rs.getString("Status"),
    	                rs.getFloat("Total_Cost"),
    	                rs.getFloat("Remaining_Debt"),
    	                rs.getInt("Accountant_ID"),
    	                rs.getInt("Warehouse_Staff_ID")
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

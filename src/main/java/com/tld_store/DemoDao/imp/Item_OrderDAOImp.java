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
import dao.Item_OrderDAO;
import dto.Item_Order;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class Item_OrderDAOImp implements Item_OrderDAO {

	@Override
	public Item_Order findById(Integer id){
		
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo employees
		Item_Order item = new Item_Order();
		
		//Truy vấn các employees trong 1 table
		String sql = "SELECT * FROM Item_Orders WHERE Item_Order_ID = ?";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(!rs.next())
			{
				item = null;
			}
			else {
				item.setItemId(rs.getInt("Item_Order_ID"));
				item.setOrderId(rs.getInt("Order_ID"));
				item.setProductId(rs.getString("Product_ID"));
				item.setQuantity(rs.getInt("Order_Quantity"));
				item.setPricePerUnit(rs.getFloat("Price_Per_Unit"));
				item.setDiscount(rs.getFloat("Discount"));
			}	
		}
        catch (SQLException e) {
            Database.closeResultSet(rs);
            Database.closePreparedStatement(ps);
            Database.closeConnection(con);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(con);

        return item;	
		
	}

	@Override
	public List<Item_Order> getAll(){
//		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
//		Connection con = Database.getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		
//		//Khởi tạo list
//		ArrayList<Item_Order> arr = new ArrayList<>();
//		Item_Order item = null;
//		
//		if(con == null) return null;
//		//Truy vấn các employees trong 1 table
//		String sql = "Select * from Item_Orders";
//		try 
//		{
//			ps = con.prepareStatement(sql);
//			rs = ps.executeQuery();
//			while(rs.next())
//			{
//				item = new Item_Order();
//				item.setItemId(rs.getInt("Item_Order_ID"));
//				item.setOrderId(rs.getInt("Order_ID"));
//				item.setProductId(rs.getString("Product_ID"));
//				item.setQuantity(rs.getInt("Order_Quantity"));
//				item.setPricePerUnit(rs.getFloat("Price_Per_Unit"));
//				item.setDiscount(rs.getFloat("Discount"));
//				arr.add(item);
//			}
//		}
//		catch(SQLException e)
//		{
//			e.printStackTrace();
//		}
//		finally {
//			Database.closeResultSet(rs);
//			Database.closePreparedStatement(ps);
//			Database.closeConnection(con);
//		}
//		return arr;
		return null;
	}

	@Override
	public int insert(Item_Order t){
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = 1;

	    String sql = "INSERT INTO Item_Orders (Order_ID, Product_ID, Price_Per_Unit, Order_Quantity, Discount) " +
	                 "VALUES (?, ?, ?, ?, ?)";

	    try {
	        ps = con.prepareStatement(sql);
	        
	        ps.setInt(1, t.getOrderId());
	        ps.setString(2, t.getProductId());
	        ps.setFloat(3, t.getPricePerUnit());
	        ps.setInt(4, t.getQuantity());
	        ps.setFloat(5, t.getDiscount());

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
	public int update(Item_Order t){
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = 1;

	    String sql = "UPDATE Item_Orders\n" +
	                 "SET Order_ID = ?, Product_ID = ?, Price_Per_Unit = ?, Order_Quantity = ?, Discount = ?\n" +
	                 "WHERE Item_Order_ID = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        ps.setInt(1, t.getOrderId());
	        ps.setString(2, t.getProductId());
	        ps.setFloat(3, t.getPricePerUnit());
	        ps.setInt(4, t.getQuantity());
	        ps.setFloat(5, t.getDiscount());
	        ps.setInt(6, t.getItemId());

	        ps.executeUpdate();
	        result = 1;
	    }
	    catch (SQLException e) {
	        Database.closePreparedStatement(ps);
	        Database.closeConnection(con);
	        throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    }

	    Database.closePreparedStatement(ps);
	    Database.closeConnection(con);
	        
	    return result;
	}

	@Override
	public int delete(Item_Order t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Item_Orders");
			cbst.setString(2, Integer.toString(t.getItemId()));

			cbst.execute();			
		}
		catch(SQLException e)
		{
			Database.closeCallableStatement(cbst);;
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		Database.closeCallableStatement(cbst);;
		Database.closeConnection(con);
		return result;
	}

	@Override
	public ArrayList<Item_Order> getAllItemOrderByOrderId(int orderId){
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo list
		ArrayList<Item_Order> arr = new ArrayList<>();
		Item_Order item = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Item_Orders where Order_ID = ?";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, orderId);
			rs = ps.executeQuery();
			while(rs.next())
			{
				item = new Item_Order();
				item.setItemId(rs.getInt("Item_Order_ID"));
				item.setOrderId(rs.getInt("Order_ID"));
				item.setProductId(rs.getString("Product_ID"));
				item.setQuantity(rs.getInt("Order_Quantity"));
				item.setPricePerUnit(rs.getFloat("Price_Per_Unit"));
				item.setDiscount(rs.getFloat("Discount"));
				arr.add(item);
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
		return arr;
	}

	@Override
	public List<Item_Order> get10(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}

}

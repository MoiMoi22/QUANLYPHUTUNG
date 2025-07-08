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
import dao.PurchaseOrderItemDAO;
import dto.Purchase_Order_Item;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class PurchaseItemDAOImp implements PurchaseOrderItemDAO {

	@Override
	public Purchase_Order_Item findById(Integer id){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Purchase_Order_Item purchaseOrderItem = null;
        
        String sql = "SELECT * FROM Purchase_Order_Items WHERE Purchase_Order_Item_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                purchaseOrderItem = new Purchase_Order_Item(
                    rs.getInt("Purchase_Order_Item_ID"),
                    rs.getInt("Purchase_Order_ID"),
                    rs.getString("Product_ID"),
                    rs.getInt("Quantity"),
                    rs.getFloat("Price_Per_Unit")
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

        return purchaseOrderItem;
	}

	@Override
	public List<Purchase_Order_Item> getAll(){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Purchase_Order_Item> purchaseOrderItems = new ArrayList<>();

        String sql = "SELECT * FROM Purchase_Order_Items";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                purchaseOrderItems.add(new Purchase_Order_Item(
                    rs.getInt("Purchase_Order_Item_ID"),
                    rs.getInt("Purchase_Order_ID"),
                    rs.getString("Product_ID"),
                    rs.getInt("Quantity"),
                    rs.getFloat("Price_Per_Unit")
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

        return purchaseOrderItems;
	}

	@Override
	public int insert(Purchase_Order_Item t) {
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;

        int result = 1;
        String sql = "INSERT INTO Purchase_Order_Items (Purchase_Order_ID, Product_ID, Quantity, Price_Per_Unit)\n"
        			+ "VALUES (?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getOrderId());
            ps.setString(2, t.getProductId());
            ps.setInt(3, t.getQuantity());
            ps.setFloat(4, t.getPricePerUnit());
            ps.execute();

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
	public int update(Purchase_Order_Item t) {
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;

        int result = 1;
        
        String sql = "UPDATE Purchase_Order_Items SET Purchase_Order_ID = ?, Product_ID = ?, Quantity = ?, Price_Per_Unit = ? WHERE Purchase_Order_Item_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getOrderId());
            ps.setString(2, t.getProductId());
            ps.setInt(3, t.getQuantity());
            ps.setFloat(4, t.getPricePerUnit());
            ps.setInt(5, t.getItemId());
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
	public int delete(Purchase_Order_Item t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Purchase_Order_Items");
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
	public ArrayList<Purchase_Order_Item> getAllPurchaseOrderItemByPurchaseOrderId(int purchaseOrderId)
			{
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Purchase_Order_Item> purchaseOrderItems = new ArrayList<>();

        String sql = "SELECT * FROM Purchase_Order_Items WHERE Purchase_Order_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, purchaseOrderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                purchaseOrderItems.add(new Purchase_Order_Item(
                    rs.getInt("Purchase_Order_Item_ID"),
                    rs.getInt("Purchase_Order_ID"),
                    rs.getString("Product_ID"),
                    rs.getInt("Quantity"),
                    rs.getFloat("Price_Per_Unit")
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

        return purchaseOrderItems;
	}


	@Override
	public ArrayList<Purchase_Order_Item> getAllPurchaseOrderItemByProductId(int productId) {
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Purchase_Order_Item> purchaseOrderItems = new ArrayList<>();

        String sql = "SELECT * FROM Purchase_Order_Items WHERE Product_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            while (rs.next()) {
                purchaseOrderItems.add(new Purchase_Order_Item(
                    rs.getInt("Purchase_Order_Item_ID"),
                    rs.getInt("Purchase_Order_ID"),
                    rs.getString("Product_ID"),
                    rs.getInt("Quantity"),
                    rs.getFloat("Price_Per_Unit")
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

        return purchaseOrderItems;
	}

	@Override
	public List<Purchase_Order_Item> get10(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}

}

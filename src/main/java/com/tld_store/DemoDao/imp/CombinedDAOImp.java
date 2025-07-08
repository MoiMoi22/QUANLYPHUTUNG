package com.tld_store.DemoDao.imp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dao.CombinedDAO;
import dao.Database;
import dto.Address;
import dto.Item_Order;
import dto.Order;
import dto.OrderTemplate;
import dto.Purchase_Order;
import dto.Purchase_Order_Item;
import dto.Supplier;
import dto.Transaction;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class CombinedDAOImp implements CombinedDAO {
	
	@Value("${myapp.some.float-value}")
	private float profitRate;
	
	@Override
	@Transactional(rollbackFor={ Exception.class})
	public void insertOrder(Order order, ArrayList<Item_Order> itemList) {
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			con.setAutoCommit(false);
			
	        String sql = "INSERT INTO Orders (Shopper_ID, Order_Date, Discount, Order_Total, "
	                   + "Remaining_Debt, Cancel_Reason, Status, "
	                   + "Shipping_Address_ID, Sales_ID, Accountant_ID, Warehouse_Staff_ID) "
	                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";
	        
	        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getPeopleId());
            ps.setDate(2, Date.valueOf(java.time.LocalDate.now()));
            ps.setFloat(3, order.getDiscount());
            ps.setFloat(4, order.getTotalCost());
            ps.setFloat(5, order.getRemainingDebt());
            ps.setString(6, order.getCancelReason());
            ps.setString(7, order.getStatus());
            ps.setInt(8, order.getShippingAddressId());
            ps.setInt(9, order.getSalesId());
            ps.setNull(10, Types.INTEGER);
            ps.setNull(11, Types.INTEGER);

            ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			int orderId = -1;
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			
            sql = "INSERT INTO Item_Orders (Order_ID, Product_ID, Price_Per_Unit, Order_Quantity, Discount)\n" 
            	+ "VALUES (?, ?, ?, ?, ?)";
            
            for (Item_Order item: itemList) {
            	ps = con.prepareStatement(sql);
    	        
    	        ps.setInt(1, orderId);
    	        ps.setString(2, item.getProductId());
    	        ps.setFloat(3, item.getPricePerUnit());
    	        ps.setInt(4, item.getQuantity());
    	        ps.setFloat(5, item.getDiscount());

    	        ps.executeUpdate();
            		
            }
            con.commit();
	        
		}catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);
		
	}
///////////////////////////// Updated ////////////////////////////////	
	


	@Override
	@Transactional(rollbackFor={Exception.class})
	public void insertOrder(Order order, ArrayList<Item_Order> itemList, Address address) {
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			con.setAutoCommit(false);
			String sql = "INSERT INTO Addresses(Address, Commune_ID) VALUES(?, ?)\n";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, address.getAddressName());
			ps.setInt(2, address.getCommuneWardId());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			int addressId = -1;
			if (rs.next()) {
			    addressId = rs.getInt(1);
			}
	        
			sql = "INSERT INTO Orders (Shopper_ID, Order_Date, Discount, Order_Total, "
	                   + "Remaining_Debt, Cancel_Reason, Status, "
	                   + "Shipping_Address_ID, Sales_ID, Accountant_ID, Warehouse_Staff_ID) "
	                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";
	        
	        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getPeopleId());
            ps.setDate(2, Date.valueOf(java.time.LocalDate.now()));
            ps.setFloat(3, order.getDiscount());
            ps.setFloat(4, order.getTotalCost());
            ps.setFloat(5, order.getRemainingDebt());
            ps.setString(6, order.getCancelReason());
            ps.setString(7, order.getStatus());
            ps.setInt(8, addressId);
            ps.setInt(9, order.getSalesId());
            ps.setNull(10, Types.INTEGER);
            ps.setNull(11, Types.INTEGER);

         ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			int orderId = -1;
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
			
         sql = "INSERT INTO Item_Orders (Order_ID, Product_ID, Price_Per_Unit, Order_Quantity, Discount)\n" 
         	+ "VALUES (?, ?, ?, ?, ?)";
         
         for (Item_Order item: itemList) {
         	ps = con.prepareStatement(sql);
 	        
 	        ps.setInt(1, orderId);
 	        ps.setString(2, item.getProductId());
 	        ps.setFloat(3, item.getPricePerUnit());
 	        ps.setInt(4, item.getQuantity());
 	        ps.setFloat(5, item.getDiscount());

 	        ps.executeUpdate();
         		
         }
            
            con.commit();
	        
		}catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);
	}

//	@Override
//	@Transactional
//	public void insertPurchaseOrder(Purchase_Order order, ArrayList<Purchase_Order_Item> itemList) {
//		Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//		
//		try {
//			con = Database.getConnection();
//			con.setAutoCommit(false);
//			
//			String sql = "INSERT INTO Purchase_Orders (Supplier_ID, Order_Date, Status, " +
//		                 "Total_Cost, Remaining_Debt, Accountant_ID) " +
//		                 "VALUES (?, ?, ?, ?, ?, ?)";
//	        
//	        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//	        ps.setInt(1, order.getPeopleId());
//	        ps.setDate(2, order.getOrderDate());
//	        ps.setString(3, order.getStatus());
//	        ps.setFloat(4, order.getTotalCost());
//	        ps.setFloat(5, order.getRemainingDebt());
//	        ps.setInt(6, order.getAccountantId());
//
//	        ps.executeUpdate();
//			rs = ps.getGeneratedKeys();
//			int orderId = -1;
//			if (rs.next()) {
//				orderId = rs.getInt(1);
//			}
//            
//			sql = "INSERT INTO Purchase_Order_Items (Purchase_Order_ID, Product_ID, Quantity, Price_Per_Unit)\n"
//        			+ "VALUES (?, ?, ?, ?)";
//			
//			String sql3 = "UPDATE Products\n"
//						+ "SET Price = ?, Total_Product_Availability = ?\n"
//						+ "WHERE Product_ID = ?";
//            
//			String sql2 = "SELECT * FROM Products WHERE Product_ID = ?";
//			
//			Product product = new Product();
//			
//            for (Purchase_Order_Item item: itemList) {
//            	ps = con.prepareStatement(sql);
//    	        
//                ps.setInt(1, orderId);
//                ps.setString(2, item.getProductId());
//                ps.setInt(3, item.getQuantity());
//                ps.setFloat(4, item.getPricePerUnit());
//
//    	        ps.executeUpdate();
//    	        
//    	        ps = con.prepareStatement(sql2);
//                ps.setString(1, item.getProductId());
//                rs = ps.executeQuery();
//
//                if (rs.next()) {
//                    product = new Product(
//                        rs.getString("Product_ID"),
//                        rs.getString("Product_Name"),
//                        rs.getString("Description"),
//                        rs.getString("Category_ID"),
//                        rs.getString("Url"),
//                        rs.getFloat("Weight"),
//                        rs.getInt("Shelf_Life"),
//                        rs.getFloat("Price"),
//                        rs.getInt("Total_Product_Availability")
//                    );
//                }
//                
//                ps = con.prepareStatement(sql3);
//                float price = (item.getPricePerUnit() * item.getQuantity() + product.getPrice() * product.getQuantity()) 
//                		/ (item.getQuantity() + product.getQuantity());
//                ps.setFloat(1, price);
//                ps.setInt(2, item.getQuantity() + product.getQuantity());
//                ps.setString(3, item.getProductId());
//                ps.executeUpdate();
//            		
//            }
//            con.commit();
//	        
//		}catch (Exception e){
//			Database.closeResultSet(rs);
//			Database.closePreparedStatement(ps);
//			Database.closeConnection(con);
//			throw new CustomException(e.getMessage());
//		}
//		
//		Database.closeResultSet(rs);
//		Database.closePreparedStatement(ps);
//		Database.closeConnection(con);
//		
//	}

	///////////////////////////////////////// updated ////////////////////////////////////////////
	@Override
	public void insertPurchaseOrder(Purchase_Order order, ArrayList<Purchase_Order_Item> itemList) {
		Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			con.setAutoCommit(false);
			
			String sql = "INSERT INTO Purchase_Orders (Supplier_ID, Order_Date, Status, " +
		                 "Total_Cost, Remaining_Debt, Accountant_ID) " +
		                 "VALUES (?, ?, ?, ?, ?, ?)";
	        
	        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setInt(1, order.getPeopleId());
	        ps.setDate(2, Date.valueOf(java.time.LocalDate.now()));
	        ps.setString(3, order.getStatus());
	        ps.setFloat(4, order.getTotalCost());
	        ps.setFloat(5, order.getRemainingDebt());
	        ps.setInt(6, order.getAccountantId());

	        ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			int orderId = -1;
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
          
			sql = "INSERT INTO Purchase_Order_Items (Purchase_Order_ID, Product_ID, Quantity, Price_Per_Unit)\n"
      			+ "VALUES (?, ?, ?, ?)";
			
          for (Purchase_Order_Item item: itemList) {
          	ps = con.prepareStatement(sql);
  	        
              ps.setInt(1, orderId);
              ps.setString(2, item.getProductId());
              ps.setInt(3, item.getQuantity());
              ps.setFloat(4, item.getPricePerUnit());

  	        ps.executeUpdate();
          }
 
          con.commit();
	       
		}
          catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);
		
	}
/////////////////////////////////////////////////////////////////////////////////////////////	

	@Override
	@Transactional(rollbackFor={ Exception.class})
	public void insertTransaction(Transaction transaction, String transactionType) {
		
		OrderTemplate order;
		
		if (transactionType.equals("THU")) {
			order = new OrderDAOImp().findById(transaction.getRelatedOrderId());
		}else {
			order = new Purchase_OrderDAOImp().findById(transaction.getRelatedOrderId());
		}
		
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
        	con = Database.getConnection();
			con.setAutoCommit(false);
			
			String sql = "INSERT INTO Transactions (Transaction_Date, Transaction_Type, Amount, Title, Description, Accountant_ID, Related_Order_ID) "
	                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
			
			ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(transaction.getTransactionDate().getTime()));
            ps.setString(2, transaction.getTransactionType());
            ps.setFloat(3, transaction.getAmount());
            ps.setString(4, transaction.getTitle());
            ps.setString(5, transaction.getDescription());
            ps.setInt(6, transaction.getAccountantId());
            ps.setInt(7, transaction.getRelatedOrderId());
            ps.executeUpdate();
            
            if (transactionType.equals("THU")) {
            	sql = "UPDATE Orders SET "
                        + "Remaining_Debt = ?, Status = ?, Accountant_ID = ?"
                        + "WHERE Order_ID = ?";
    		}else {
    			sql = "UPDATE Purchase_Orders "
    					+ "SET Remaining_Debt = ?, Status = ?, Accountant_ID = ?" 
    					+ "WHERE Purchase_Order_ID = ?";
    		}
            ps = con.prepareStatement(sql);
            ps.setFloat(1, order.getRemainingDebt() - transaction.getAmount());
            if (order.getRemainingDebt() - transaction.getAmount() > 0) {
            	ps.setString(2, "IN DEBT");
            }else {
            	ps.setString(2, "PAID");
            }
            
            ps.setInt(3, transaction.getAccountantId());
            ps.setInt(4, order.getOrderId());

            ps.executeUpdate();
			
			con.commit();
		} catch (SQLException e) {
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
        
        Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);
	}
	
	@Override
	@Transactional(rollbackFor={ Exception.class})
	public void deleteTransaction(Transaction transaction, String transactionType) {
		OrderTemplate order;
		
		if (transactionType.equals("THU")) {
			order = new OrderDAOImp().findById(transaction.getRelatedOrderId());
		}else {
			order = new Purchase_OrderDAOImp().findById(transaction.getRelatedOrderId());
		}
		
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
        	con = Database.getConnection();
			con.setAutoCommit(false);
			
			String sql = "DELETE FROM Transactions\n"
	                   + "WHERE Transaction_ID = ?\n";
			
			ps = con.prepareStatement(sql);
            ps.setInt(1, transaction.getTransactionId());
            ps.executeUpdate();
            
            if (transactionType.equals("THU")) {
            	sql = "UPDATE Orders SET "
                        + "Remaining_Debt = ?, "
                        + "WHERE Order_ID = ?";
    		}else {
    			sql = "UPDATE Purchase_Orders "
    					+ "SET Remaining_Debt = ? " 
    					+ "WHERE Purchase_Order_ID = ?";
    		}
            ps = con.prepareStatement(sql);
            ps.setFloat(1, order.getRemainingDebt() + transaction.getAmount());
            ps.setInt(2, order.getOrderId());
            ps.executeUpdate();
			
			con.commit();
		} catch (SQLException e) {
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
        
        Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);
		
	}

	@Override
	public int insertSupplier(Supplier supplier, Address address) {
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			con.setAutoCommit(false);
			String sql = "INSERT INTO Addresses(Address, Commune_ID) VALUES(?, ?)\n";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, address.getAddressName());
			ps.setInt(2, address.getCommuneWardId());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			int addressId = -1;
			if (rs.next()) {
			    addressId = rs.getInt(1);
			}
		
	        
			sql = "INSERT INTO Suppliers (Company_Name, Contact_Name, Contact_Title, Phone, Email, Supplier_Address_ID, Url) "
	                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
	        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, supplier.getCompanyName());
	        ps.setString(2, supplier.getContactName());
	        ps.setString(3, supplier.getContactTitle());
	        ps.setString(4, supplier.getPhone());
	        ps.setString(5, supplier.getEmail());
	        ps.setInt(6, addressId);
	        ps.setString(7, supplier.getUrl());
	        ps.executeUpdate();

	        rs = ps.getGeneratedKeys();
	        int supplierId = -1;
	        if (rs.next()) {
	            supplierId = rs.getInt(1);
	        }

	        if (supplierId == -1) {
	            throw new CustomException("Failed to retrieve generated Supplier ID.");
	        }

	        con.commit();
	        
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			
	        return supplierId;
	        
		}
		catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
	}

	@Override
	public void updateSupplier(Supplier supplier, Address address) {
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			con.setAutoCommit(false);
			String sql = "INSERT INTO Addresses(Address, Commune_ID) VALUES(?, ?)\n";
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, address.getAddressName());
			ps.setInt(2, address.getCommuneWardId());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			int addressId = -1;
			if (rs.next()) {
			    addressId = rs.getInt(1);
			}
		
	        
			sql = "UPDATE Suppliers SET Company_Name = ?, Contact_Name = ?, Contact_Title = ?, Phone = ?, Email = ?, Supplier_Address_ID = ?, Url = ? WHERE Supplier_ID = ?";
	        ps = con.prepareStatement(sql);
	        ps.setString(1, supplier.getCompanyName());
	        ps.setString(2, supplier.getContactName());
	        ps.setString(3, supplier.getContactTitle());
	        ps.setString(4, supplier.getPhone());
	        ps.setString(5, supplier.getEmail());
	        ps.setInt(6, addressId);
	        ps.setString(7, supplier.getUrl());
	        ps.setInt(8, supplier.getSupplierId());
	        ps.executeUpdate();

	        con.commit();
	        
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
				        
		}
		catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
	}

	@Override
	@Transactional(rollbackFor={ Exception.class})
	public void acceptOrder(Order order, ArrayList<Item_Order> itemList) {
		Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      		
		try {
			con = Database.getConnection();
			con.setAutoCommit(false);
			
			String sql = "UPDATE Orders SET "
            + "Status = ?, Warehouse_Staff_ID = ?\n"
            + "WHERE Order_ID = ?";
	        
	        ps = con.prepareStatement(sql);
	      if(order.getStatus().equals("IN DEBT")) {
	    	  ps.setString(1, "IN DEBT COMPLETED");
	      }
	      else {
	          ps.setString(1, "COMPLETED");

	      }
          ps.setInt(2, order.getWarehouseStaffId());
          ps.setInt(3, order.getOrderId());

          ps.executeUpdate();
			
          
          sql = "Update Products\n"
        	  + "SET Total_Product_Availability = Total_Product_Availability - ? \n"
        	  + "WHERE Product_ID = ?";
          
          for (Item_Order item: itemList) {
            	ps = con.prepareStatement(sql);
    	        
    	        ps.setInt(1, item.getQuantity());
    	        ps.setString(2, item.getProductId());

    	        ps.executeUpdate();
            		
            }
          
          con.commit();
	        
		}catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);		
		
	}

	@Override
	public void denyOrder(Order order) {
		Connection con = null;
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	  	try {
			con = Database.getConnection();
			
			String sql = "UPDATE Orders SET "
            + "Status = ?, Warehouse_Staff_ID = ?\n"
            + "WHERE Order_ID = ?";
	        
	        ps = con.prepareStatement(sql);
	        ps.setString(1, "CANCEL");
	        ps.setInt(2, order.getWarehouseStaffId());
	        ps.setInt(3, order.getOrderId());

	        ps.executeUpdate();
          	        
		}
	  	catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);	      
		
	}

	@Override
	@Transactional(rollbackFor={ Exception.class})	
	public void acceptPurchaseOrder(Purchase_Order order, ArrayList<Purchase_Order_Item> itemList) {
		
		Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			con.setAutoCommit(false);
						
			String sql3 = "UPDATE Products\n"
						+ "SET Price = ?, Total_Product_Availability = Total_Product_Availability + ?\n"
						+ "WHERE Product_ID = ?";
          
			String sql2 = "SELECT\n"
					+ "    COUNT(poi.Purchase_Order_Item_ID) AS Total_Entries, \n"
					+ "    SUM(poi.Quantity * poi.Price_Per_Unit) AS Total_Import_Cost\n"
					+ "FROM \n"
					+ "    Purchase_Order_Items poi\n"
					+ "JOIN \n"
					+ "    Purchase_Orders po\n"
					+ "ON \n"
					+ "    poi.Purchase_Order_ID = po.Purchase_Order_ID\n"
					+ "WHERE \n"
					+ "    poi.Product_ID = ?\n"
					+ "    AND po.Status IN ('IN DEBT COMPLETED', 'COMPLETED')\n"
					+ "GROUP BY \n"
					+ "    poi.Product_ID;";
			
			float total_cost = 0;
			int total_num = 0;
			
          for (Purchase_Order_Item item: itemList) {
  	        
  	        ps = con.prepareStatement(sql2);
              ps.setString(1, item.getProductId());
              rs = ps.executeQuery();

              if (rs.next()) {
                      total_cost = rs.getFloat("Total_Import_Cost");
                      total_num = rs.getInt("Total_Entries");
              }
              
              ps = con.prepareStatement(sql3);
              float price = ((total_cost + item.getPricePerUnit()*item.getQuantity())*profitRate)
            		  		/(total_num+item.getQuantity());
              ps.setFloat(1, price);
              ps.setInt(2, item.getQuantity());
              ps.setString(3, item.getProductId());
              ps.executeUpdate();
          		
          }
          
			String sql = "UPDATE Purchase_Orders SET "
		            + "Status = ?, Warehouse_Staff_ID = ?\n"
		            + "WHERE Purchase_Order_ID = ?";
			        
			        ps = con.prepareStatement(sql);
			      if(order.getStatus().equals("IN DEBT")) {
			    	  ps.setString(1, "IN DEBT COMPLETED");
			      }
			      else {
			          ps.setString(1, "COMPLETED");

			      }
		          ps.setInt(2, order.getWarehouseStaffId());
		          ps.setInt(3, order.getOrderId());

		          ps.executeUpdate();
		          
          con.commit();
	        
		}catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);
	}

	@Override
	public void denyPurchaseOrder(Purchase_Order order) {
		Connection con = null;
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	  	try {
			con = Database.getConnection();
			
			String sql = "UPDATE Purchase_Orders SET "
		            + "Status = ?, Warehouse_Staff_ID = ?\n"
		            + "WHERE Purchase_Order_ID = ?";
	        
	        ps = con.prepareStatement(sql);
	        ps.setString(1, "CANCEL");
	        ps.setInt(2, order.getWarehouseStaffId());
	        ps.setInt(3, order.getOrderId());

	        ps.executeUpdate();
        	        
		}
	  	catch (SQLException e){
			Database.closeResultSet(rs);
			Database.closePreparedStatement(ps);
			Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}
		
		Database.closeResultSet(rs);
		Database.closePreparedStatement(ps);
		Database.closeConnection(con);			
		
	}
	
	

}

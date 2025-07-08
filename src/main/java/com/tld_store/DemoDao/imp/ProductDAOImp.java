package com.tld_store.DemoDao.imp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dao.Database;
import dao.ProductDAO;
import dto.Product;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class ProductDAOImp implements ProductDAO {

	@Override
	public Product findById(String id){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Product product = null;

        String sql = "SELECT * FROM Products WHERE Product_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                product = new Product(
                    rs.getString("Product_ID"),
                    rs.getString("Product_Name"),
                    rs.getString("Description"),
                    rs.getString("Category_ID"),
                    rs.getString("Url"),
                    rs.getFloat("Weight"),
                    rs.getInt("Shelf_Life"),
                    rs.getFloat("Price"),
                    rs.getInt("Total_Product_Availability")
                );
            }
        }
        catch (SQLException e) {
            Database.closeResultSet(rs);
            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(conn);
        return product;
	}

	@Override
	public List<Product> getAll(){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM Products";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                    rs.getString("Product_ID"),
                    rs.getString("Product_Name"),
                    rs.getString("Description"),
                    rs.getString("Category_ID"),
                    rs.getString("Url"),
                    rs.getFloat("Weight"),
                    rs.getInt("Shelf_Life"),
                    rs.getFloat("Price"),
                    rs.getInt("Total_Product_Availability")
                ));
            }
        }
        catch (SQLException e) {
            Database.closeResultSet(rs);
            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(conn);

        return products;
	}

	@Override
	public String insertProduct(Product t) {
	    Connection con = Database.getConnection();
	    CallableStatement cbst = null;
	    String newProductId = null;

	    String sql = "{CALL sp_addProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	    try {
	        cbst = con.prepareCall(sql);

	        // Set các tham số đầu vào
	        cbst.setString(1, t.getProductId());         // @Product_ID
	        cbst.setString(2, t.getProductName());       // @Product_Name
	        cbst.setString(3, t.getDescription());       // @Description
	        cbst.setString(4, t.getCategoryId());        // @Category_ID
	        cbst.setFloat(5, t.getWeight());             // @Weight
	        cbst.setInt(6, t.getShelfLife());            // @Shelf_Life
	        cbst.setFloat(7, t.getPrice());              // @Price
	        cbst.setInt(8, t.getQuantity());             // @Total_Product_Availability
	        cbst.setString(9, t.getUrl());               // @Url

	        // Thêm tham số OUTPUT
	        cbst.registerOutParameter(10, Types.NVARCHAR); // @NewProduct_ID OUTPUT

	        // Thực thi stored procedure
	        cbst.execute();

	        // Lấy giá trị trả về
	        newProductId = cbst.getString(10);

	    } catch (SQLException e) {
	        throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    } finally {
	        Database.closeCallableStatement(cbst);
	        Database.closeConnection(con);
	    }

	    return newProductId; // Trả về mã sản phẩm mới
	}


	@Override
	public int update(Product t){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;
        
        String sql = "UPDATE Products SET Product_Name = ?, Description = ?, Category_ID = ?, Weight = ?, Shelf_Life = ?, Price = ?, Total_Product_Availability = ?, Url = ? WHERE Product_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, t.getProductName());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getCategoryId());
            ps.setFloat(4, t.getWeight());
            ps.setInt(5, t.getShelfLife());
            ps.setFloat(6, t.getPrice());
            ps.setInt(7, t.getQuantity());
            ps.setString(8, t.getUrl()); 
            ps.setString(9, t.getProductId());
       
            ps.executeUpdate();
            result = 1;
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
	public int delete(Product t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Products");
			cbst.setString(2, t.getProductId());

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
	public ArrayList<Product> findAllProductsByCategoryId(int categoryId){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM Products WHERE Category_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                    rs.getString("Product_ID"),
                    rs.getString("Product_Name"),
                    rs.getString("Description"),
                    rs.getString("Category_ID"),
                    rs.getString("Url"),
                    rs.getFloat("Weight"),
                    rs.getInt("Shelf_Life"),
                    rs.getFloat("Price"),
                    rs.getInt("Total_Product_Availability")
                ));
            }
        }
        catch (SQLException e) {
            Database.closeResultSet(rs);
            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);
            throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
        }
        Database.closeResultSet(rs);
        Database.closePreparedStatement(ps);
        Database.closeConnection(conn);

        return products;
	}

	@Override
	public List<Product> get10(int numPage) {
		Connection con = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

		int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Products\n"
				+ 	  "ORDER BY Product_ID\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                    rs.getString("Product_ID"),
                    rs.getString("Product_Name"),
                    rs.getString("Description"),
                    rs.getString("Category_ID"),
                    rs.getString("Url"),
                    rs.getFloat("Weight"),
                    rs.getInt("Shelf_Life"),
                    rs.getFloat("Price"),
                    rs.getInt("Total_Product_Availability")
                ));
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

        return products;
	}

	@Override
	public ArrayList<Product> findAllProductsByName(String productName) {
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
        ResultSet rs = null;
        ArrayList<Product> products = new ArrayList<>();
        
        String sql = "{call sp_searchAllProduct(?)}";
        try {
			cbst = con.prepareCall(sql);
			cbst.setString(1, productName);
			rs = cbst.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("Product_ID"),
                        rs.getString("Product_Name"),
                        rs.getString("Description"),
                        rs.getString("Category_ID"),
                        rs.getString("Url"),
                        rs.getFloat("Weight"),
                        rs.getInt("Shelf_Life"),
                        rs.getFloat("Price"),
                        rs.getInt("Total_Product_Availability")
                    ));
            }
        }
		catch(SQLException e)
		{
			Database.closeResultSet(rs);
            Database.closeCallableStatement(cbst);;
            Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
		Database.closeResultSet(rs);
        Database.closeCallableStatement(cbst);;
        Database.closeConnection(con);
        return products;
	}

	@Override
	public ArrayList<Product> findTop10ProductsByName(String productName, int numPage) {
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
        ResultSet rs = null;
        ArrayList<Product> products = new ArrayList<>();
        
        int offset = (numPage-1)*10;
        
        String sql = "{call sp_searchProduct(?, ?)}";
        try {
			cbst = con.prepareCall(sql);
			cbst.setInt(1, offset);
			cbst.setString(2, productName);
			rs = cbst.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("Product_ID"),
                        rs.getString("Product_Name"),
                        rs.getString("Description"),
                        rs.getString("Category_ID"),
                        rs.getString("Url"),
                        rs.getFloat("Weight"),
                        rs.getInt("Shelf_Life"),
                        rs.getFloat("Price"),
                        rs.getInt("Total_Product_Availability")
                    ));
            }
        }
		catch(SQLException e)
		{
			Database.closeResultSet(rs);
            Database.closeCallableStatement(cbst);;
            Database.closeConnection(con);
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
		}     
		Database.closeResultSet(rs);
        Database.closeCallableStatement(cbst);;
        Database.closeConnection(con);
        return products;
	}

	@Override
	public int updateUrl(String id, String url) {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Products SET Url = ? \n "
	               + " WHERE Product_ID = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        
			ps.setString(1, url);
	        ps.setString(2, id);

	        ps.executeUpdate();
	        result = 1;
	    } catch (SQLException e) {
	        Database.closePreparedStatement(ps);
	        Database.closeConnection(con);
	    	throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    } 
	    Database.closePreparedStatement(ps);
	    Database.closeConnection(con);
	    return result;
	}

	@Override
	public int insert(Product t) {
		// TODO Auto-generated method stub
		return 0;
	}

}

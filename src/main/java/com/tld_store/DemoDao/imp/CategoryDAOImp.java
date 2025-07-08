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
import dao.CategoryDAO;
import dto.ProductCategory;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class CategoryDAOImp implements CategoryDAO {

	@Override
	public ProductCategory findById(String id){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductCategory category = null;

        String sql = "SELECT * FROM Product_Categories WHERE Category_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                category = new ProductCategory(
                        rs.getString("Category_ID"),
                        rs.getString("Category_Name"),
                        rs.getString("Description")
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
        return category;
	}

	@Override
	public List<ProductCategory> getAll(){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProductCategory> categories = new ArrayList<>();

        String sql = "SELECT * FROM Product_Categories";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(new ProductCategory(
                        rs.getString("Category_ID"),
                        rs.getString("Category_Name"),
                        rs.getString("Description")
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
        return categories;
	}

	@Override
	public int insert(ProductCategory t){
		Connection con = Database.getConnection();
        CallableStatement cbst = null;
        int result = 1;
                
        String sql = "{call sp_AddProductCategory(?, ?, ?)}";
        
        try {

            cbst = con.prepareCall(sql);

            // Cài đặt các tham số đầu vào
            cbst.setString(1, t.getCategoryId());
            cbst.setString(2, t.getName());
            cbst.setString(3, t.getDescription());

            // Thực thi Stored Procedure
            cbst.execute();
        }
        
		catch(SQLException e)
		{
            Database.closeCallableStatement(cbst);;
            Database.closeConnection(con);
            System.out.println(e.getMessage());
			throw new CustomException(e.getMessage());
		}     
        Database.closeCallableStatement(cbst);;
        Database.closeConnection(con);
        return result;
	}

	@Override
	public int update(ProductCategory t){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;

        String sql = "UPDATE Product_Categories SET Category_Name = ?, Description = ? WHERE Category_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, t.getName());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getCategoryId());
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
	public int delete(ProductCategory t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Product_Categories");
			cbst.setString(2, t.getCategoryId());

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
	public List<ProductCategory> get10(int numPage) {
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProductCategory> categories = new ArrayList<>();
        int offset = (numPage-1) * 10;
        
        String sql = "SELECT *\n"
				+     "FROM Product_Categories\n"
				+ 	  "ORDER BY Category_ID\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, offset);
            rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(new ProductCategory(
                        rs.getString("Category_ID"),
                        rs.getString("Category_Name"),
                        rs.getString("Description")
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
        return categories;
	}

	@Override
	public List<ProductCategory> getProductCategoriesByName(String name, int numPage) {
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
        ResultSet rs = null;
        List<ProductCategory> categories = new ArrayList<>();
        
        int offset = (numPage-1)* 10;

        String sql = "{call sp_searchProductCategoriesByName(?, ?)}";
        try {
			cbst = con.prepareCall(sql);
			cbst.setInt(1, offset);
			cbst.setString(2, name);
			rs = cbst.executeQuery();

            while (rs.next()) {
                categories.add(new ProductCategory(
                        rs.getString("Category_ID"),
                        rs.getString("Category_Name"),
                        rs.getString("Description")
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
        return categories;
	}

}

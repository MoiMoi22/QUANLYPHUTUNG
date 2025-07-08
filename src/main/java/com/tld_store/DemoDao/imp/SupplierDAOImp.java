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
import dao.SupplierDAO;
import dto.Shopper;
import dto.Supplier;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class SupplierDAOImp implements SupplierDAO {

	@Override
	public Supplier findById(Integer id){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Supplier supplier = null;

        String sql = "SELECT * FROM Suppliers WHERE Supplier_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                supplier = new Supplier(
                        rs.getInt("Supplier_ID"),
                        rs.getString("Company_Name"),
                        rs.getString("Contact_Name"),
                        rs.getString("Contact_Title"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Url"),
                        rs.getInt("Supplier_Address_ID")
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
		
		return supplier;
	}

	@Override
	public List<Supplier> getAll(){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Supplier> suppliers = new ArrayList<>();

        if (conn == null) return null;

		String sql =  "SELECT *\n"
				+     "FROM Suppliers\n"
				+ 	  "ORDER BY Supplier_ID\n"
				+ "OFFSET 0 ROWS\n"
				+ "FETCH NEXT 11 ROW ONLY";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("Supplier_ID"),
                        rs.getString("Company_Name"),
                        rs.getString("Contact_Name"),
                        rs.getString("Contact_Title"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Url"),
                        rs.getInt("Supplier_Address_ID")
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
		
		return suppliers;
	}

	@Override
	public int insert(Supplier t){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;
        
        String sql = "INSERT INTO Suppliers (Company_Name, Contact_Name, Contact_Title, Phone, Email, Supplier_Address_ID, Url) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, t.getCompanyName());
            ps.setString(2, t.getContactName());
            ps.setString(3, t.getContactTitle());
            ps.setString(4, t.getPhone());
            ps.setString(5, t.getEmail());
            ps.setInt(6, t.getSupplierAddressId());
            ps.setString(7, t.getUrl());
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
	public int update(Supplier t) {
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        int result = 1;

        String sql = "UPDATE Suppliers SET Company_Name = ?, Contact_Name = ?, Contact_Title = ?, Phone = ?, Email = ?, Url = ? WHERE Supplier_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, t.getCompanyName());
            ps.setString(2, t.getContactName());
            ps.setString(3, t.getContactTitle());
            ps.setString(4, t.getPhone());
            ps.setString(5, t.getEmail());
            ps.setString(6, t.getUrl());
            ps.setInt(7, t.getSupplierId());
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
	public int delete(Supplier t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Suppliers");
			cbst.setString(2, Integer.toString(t.getSupplierId()));

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
	public Supplier getSupplierByAddressId(int addressId){
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Supplier supplier = null;

        String sql = "SELECT * FROM Suppliers WHERE Supplier_Address_ID = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, addressId);
            rs = ps.executeQuery();

            if (rs.next()) {
                supplier = new Supplier(
                        rs.getInt("Supplier_ID"),
                        rs.getString("Company_Name"),
                        rs.getString("Contact_Name"),
                        rs.getString("Contact_Title"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Url"),
                        rs.getInt("Supplier_Address_ID"));
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
		
		return supplier;
	}

	@Override
	public List<Supplier> get10(int numPage) {
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Supplier> suppliers = new ArrayList<>();

        String sql =  "SELECT *\n"
				+ "FROM Suppliers\n"
				+ "ORDER BY Supplier_ID\n"
				+ "OFFSET " + Integer.toString(10 * (numPage - 1)) + " ROWS\n"
				+ "FETCH NEXT 11 ROW ONLY";
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("Supplier_ID"),
                        rs.getString("Company_Name"),
                        rs.getString("Contact_Name"),
                        rs.getString("Contact_Title"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Url"),
                        rs.getInt("Supplier_Address_ID")
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
		
		return suppliers;
	}

	@Override
	public ArrayList<Supplier> getTop10SupplierByName(String name, int numPage) {
		Connection conn = Database.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Supplier> suppliers = new ArrayList<>();

        String sql;
        if (numPage == -1) {
        	sql = "SELECT * FROM Suppliers \n"
        		+ "WHERE (Company_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%')";
        }else {
	        sql =  "SELECT *\n"
					+ "FROM Suppliers\n"
					+ "WHERE (Company_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%')"
					+ "ORDER BY Supplier_ID\n"
					+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
					+ "FETCH NEXT 11 ROW ONLY";
        }
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("Supplier_ID"),
                        rs.getString("Company_Name"),
                        rs.getString("Contact_Name"),
                        rs.getString("Contact_Title"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Url"),
                        rs.getInt("Supplier_Address_ID")
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
		
		return suppliers;
	}

	@Override
	public int updateUrl(int id, String url) {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Suppliers SET Url = ?\n "
	               + " WHERE Supplier_ID = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        
			ps.setString(1, url);
	        ps.setInt(2, id);

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
	public Supplier getSupplierByPhoneNum(String phoneNum) {
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo shopper
		Supplier supplier = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Suppliers where Phone = ?";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, phoneNum);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
                supplier = new Supplier(
                        rs.getInt("Supplier_ID"),
                        rs.getString("Company_Name"),
                        rs.getString("Contact_Name"),
                        rs.getString("Contact_Title"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Url"),
                        rs.getInt("Supplier_Address_ID"));
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

        return supplier;
	}
	
}

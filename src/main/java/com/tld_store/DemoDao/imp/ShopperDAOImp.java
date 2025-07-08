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
import dao.ShopperDAO;
import dto.Shopper;
import error_handling.ErrorMessages;
import exception.CustomException;
@Repository
public class ShopperDAOImp implements ShopperDAO {
	
	@Override
	public Shopper findById(Integer id) {

		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo shopper
		Shopper cus = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Shoppers where Shopper_ID = ?";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				cus = new Shopper();
				cus.setId(rs.getInt("Shopper_ID"));
				cus.setFirstName(rs.getString("First_Name"));
				cus.setLastName(rs.getString("Last_Name"));
				cus.setPhoneNum(rs.getString("Phone_Number"));
				cus.setEmail(rs.getString("Email"));
				cus.setDob(rs.getDate("Date_of_Birth"));
				cus.setUrl(rs.getString("Url"));
				cus.setGender(rs.getString("Gender"));
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

        return cus;
	}

	@Override
	public List<Shopper> getAll() {
		
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo shopper
		Shopper cus = null;
		ArrayList<Shopper> listShoppers = new ArrayList<>();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Shoppers";
		try 
		{
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				cus = new Shopper();
				cus.setId(rs.getInt("Shopper_ID"));
				cus.setFirstName(rs.getString("First_Name"));
				cus.setLastName(rs.getString("Last_Name"));
				cus.setPhoneNum(rs.getString("Phone_Number"));
				cus.setEmail(rs.getString("Email"));
				cus.setDob(rs.getDate("Date_of_Birth"));
				cus.setUrl(rs.getString("Url"));
				cus.setGender(rs.getString("Gender"));
				listShoppers.add(cus);
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
		
		return listShoppers;
	}

	@Override
	public int insert(Shopper t) {
		
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		
		int result = 1;
		//Truy vấn các employees trong 1 table
		String sql = "INSERT INTO Shoppers (First_Name, Last_Name, Email, Phone_Number, Date_of_Birth, Gender, Url) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?);";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, t.getFirstName());
			ps.setString(2, t.getLastName());
			ps.setString(3, t.getEmail());
			ps.setString(4, t.getPhoneNum());
			ps.setDate(5, t.getDob());
			ps.setString(6, t.getGender());
			ps.setString(7, t.getUrl());
			result = ps.executeUpdate();
			
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
	public int update(Shopper t) {
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		int result = 1;
		
		//Truy vấn các employees trong 1 table
		String sql = "Update Shoppers\n"
					+"Set First_Name = ?, Last_Name = ?, Email = ?, Phone_Number = ?, Date_of_Birth = ?, Gender = ?\n, Url = ?\n"
					+"Where Shopper_ID = ?";
		try 
		{
			ps = con.prepareStatement(sql);
			
			ps.setString(1, t.getFirstName());
			ps.setString(2, t.getLastName());
			ps.setString(3, t.getEmail());
			ps.setString(4, t.getPhoneNum());
			ps.setDate(5, t.getDob());
			ps.setString(6, t.getGender());
			ps.setString(7, t.getUrl());
			ps.setInt(8, t.getId());
			
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
	public int delete(Shopper t) {
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Shoppers");
			cbst.setString(2, Integer.toString(t.getId()));

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
	public Shopper getShopperByOrderId(Integer orderId) {

		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo shopper
		Shopper cus = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select Shoppers.Shopper_ID, Shoppers.First_Name, Shoppers.Last_Name, Shoppers.Phone_Number, Shoppers.Email, Shoppers.Date_of_Birth, Shoppers.Gender, Shoppers.Url\n"
				   + "From Shoppers, Orders\n"
				   + "Where Orders.Order_ID = ? AND Orders.Shopper_ID = Shoppers.Shopper_ID";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, orderId);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				cus = new Shopper();
				cus.setId(rs.getInt("Shopper_ID"));
				cus.setFirstName(rs.getString("First_Name"));
				cus.setLastName(rs.getString("Last_Name"));
				cus.setPhoneNum(rs.getString("Phone_Number"));
				cus.setEmail(rs.getString("Email"));
				cus.setDob(rs.getDate("Date_of_Birth"));
				cus.setUrl(rs.getString("Url"));
				cus.setGender(rs.getString("Gender"));
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

        return cus;
	}

	@Override
	public List<Shopper> get10(int numPage) {
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
				Connection con = Database.getConnection();
				PreparedStatement ps = null;
				ResultSet rs = null;
				
				//Khởi tạo shopper
				Shopper cus = null;
				ArrayList<Shopper> listShoppers = new ArrayList<>();
				
				//Truy vấn các employees trong 1 table
				String sql =  "SELECT *\n"
						+ "FROM Shoppers\n"
						+ "ORDER BY Shopper_ID\n"
						+ "OFFSET " + Integer.toString(10 * (numPage-1)) + " ROWS\n"
						+ "FETCH NEXT 11 ROW ONLY";
				try 
				{
					ps = con.prepareStatement(sql);
					rs = ps.executeQuery();
					
					while(rs.next())
					{
						cus = new Shopper();
						cus.setId(rs.getInt("Shopper_ID"));
						cus.setFirstName(rs.getString("First_Name"));
						cus.setLastName(rs.getString("Last_Name"));
						cus.setPhoneNum(rs.getString("Phone_Number"));
						cus.setEmail(rs.getString("Email"));
						cus.setDob(rs.getDate("Date_of_Birth"));
						cus.setGender(rs.getString("Gender"));
						cus.setUrl(rs.getString("Url"));
						listShoppers.add(cus);
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
				
				return listShoppers;
	}

	@Override
	public ArrayList<Shopper> getTop10ShopperByFullName(String name, int numPage) {
		
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo shopper
		Shopper cus = null;
		ArrayList<Shopper> listShoppers = new ArrayList<>();
		
		//Truy vấn các employees trong 1 table
		String sql;
		if (numPage == -1) {
			sql = "Select * from Shoppers\n"
				+ "WHERE (Last_Name + ' ' + First_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%')";
		}
		else {
			sql =  "SELECT *\n"
					+ "FROM Shoppers\n"
					+ "WHERE (Last_Name + ' ' + First_Name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%' + ? + '%')\n"
					+ "ORDER BY Shopper_ID\n"
					+ "OFFSET " + Integer.toString(10 * (numPage - 1)) + " ROWS\n"
					+ "FETCH NEXT 11 ROWS ONLY";
		}
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				cus = new Shopper();
				cus.setId(rs.getInt("Shopper_ID"));
				cus.setFirstName(rs.getString("First_Name"));
				cus.setLastName(rs.getString("Last_Name"));
				cus.setPhoneNum(rs.getString("Phone_Number"));
				cus.setEmail(rs.getString("Email"));
				cus.setDob(rs.getDate("Date_of_Birth"));
				cus.setGender(rs.getString("Gender"));
				cus.setUrl(rs.getString("Url"));
				listShoppers.add(cus);
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
		
		return listShoppers;
	}

	@Override
	public Shopper getShopperByPhoneNum(String phoneNum) {
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo shopper
		Shopper cus = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Shoppers where Phone_Number = ?";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, phoneNum);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				cus = new Shopper();
				cus.setId(rs.getInt("Shopper_ID"));
				cus.setFirstName(rs.getString("First_Name"));
				cus.setLastName(rs.getString("Last_Name"));
				cus.setPhoneNum(rs.getString("Phone_Number"));
				cus.setEmail(rs.getString("Email"));
				cus.setDob(rs.getDate("Date_of_Birth"));
				cus.setUrl(rs.getString("Url"));
				cus.setGender(rs.getString("Gender"));
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

        return cus;
	}

	@Override
	public int updateUrl(String phoneNum, String url) {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Shoppers SET Url = ?\n "
	               + " WHERE Phone_Number = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        
			ps.setString(1, url);
	        ps.setString(2, phoneNum);

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


}


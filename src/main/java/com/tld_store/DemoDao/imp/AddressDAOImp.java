package com.tld_store.DemoDao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dao.AddressDAO;
import dao.Database;
import dto.Address;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class AddressDAOImp implements AddressDAO{

	public AddressDAOImp() {
		super();
	}
	
	@Override
	public Address findById(Integer id) {
		// Get connection from to database
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		Address address = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Addresses where Address_ID = ?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				address = new Address(rs.getInt("Address_ID"), 
										rs.getString("Address"),
										rs.getInt("Commune_ID"));
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
		return address;
	}

	@Override
	public List<Address> getAll(){
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		List<Address> addresses = new ArrayList<Address>();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Addresses";
		try 
		{
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				addresses.add(new Address(rs.getInt("Address_ID"), 
										rs.getString("Address"),
										rs.getInt("Commune_ID")));
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
		return addresses;
	}

	@Override
	public int insert(Address t){
		// Get connection from to database
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "INSERT INTO Addresses(Address, Commune_ID) VALUES(?, ?)";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setString(1, t.getAddressName());
			ps.setInt(2, t.getCommuneWardId());
			ps.execute();
			
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
		return 1;
	}

	@Override
	public int update(Address t){
		// Get connection from to database
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
				
		String sql = "UPDATE Addresses SET Address=?, Commune_ID=? WHERE Address_ID=?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setString(1, t.getAddressName());
			ps.setInt(2, t.getCommuneWardId());
			ps.setInt(3, t.getAddressId());
			
			ps.execute();
			
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
			return 1;
	}

	@Override
	public int delete(Address t){
		// Get connection from to database
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
				
		String sql = "DELETE Addresses WHERE Address_ID=?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getAddressId());
			
			ps.execute();
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
		return 1;
	}

	@Override
	public ArrayList<Address> getAllAddressesByCommuneId(int communeId){
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		ArrayList<Address> addresses = new ArrayList<Address>();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Addresses WHERE Commune_ID=?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, communeId);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				addresses.add(new Address(rs.getInt("Address_ID"), 
										rs.getString("Address"),
										rs.getInt("Commune_ID")));
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
		return addresses;
	}

	@Override
	public List<Address> get10(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

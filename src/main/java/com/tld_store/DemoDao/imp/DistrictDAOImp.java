package com.tld_store.DemoDao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dao.Database;
import dao.DistrictDAO;
import dto.District;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class DistrictDAOImp implements DistrictDAO {
	
	public DistrictDAOImp() {
		super();
	}

	@Override
	public District findById(Integer id){
		// Get connection from to database
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		District district = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from District where District_ID = ?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				district = new District(rs.getInt("District_ID"), 
										rs.getString("District_Name"),
										rs.getInt("Province_ID"));
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
		
		return district;
	}

	@Override
	public List<District> getAll(){
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		List<District> districts = new ArrayList<District>();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from District";
		try 
		{
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				districts.add(new District(rs.getInt("District_ID"), 
											rs.getString("District_Name"),
											rs.getInt("Province_ID")));
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
		return districts;
	}

	@Override
	public int insert(District t){
		return 0;
	}

	@Override
	public int update(District t){
		return 0;
	}

	@Override
	public int delete(District t){
		return 0;
	}

	@Override
	public ArrayList<District> getAllDistrictByProvinceById(int provinceId){
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		ArrayList<District> districts = new ArrayList<District>();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from District where Province_ID = ?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, provinceId);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				districts.add(new District(rs.getInt("District_ID"), 
											rs.getString("District_Name"),
											rs.getInt("Province_ID")));
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
		return districts;
	}

	@Override
	public List<District> get10(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}

}

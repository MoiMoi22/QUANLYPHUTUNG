package com.tld_store.DemoDao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dao.CommuneDAO;
import dao.Database;
import dto.CommuneWard;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class CommuneDAOImp implements CommuneDAO{
	
	public CommuneDAOImp() {
		super();
	}

	@Override
	public CommuneWard findById(Integer id){
		// Get connection from to database
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		CommuneWard commune = null;
		
		String sql = "Select * from Commune_Ward where Commune_Ward_ID = ?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				commune = new CommuneWard(rs.getInt("Commune_Ward_ID"), 
										rs.getString("Commune_Ward_Name"),
										rs.getInt("District_ID"));
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
		
		return commune;
	}

	@Override
	public List<CommuneWard> getAll(){
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		List<CommuneWard> communes = new ArrayList<CommuneWard>();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Commune_Ward";
		try 
		{
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				communes.add(new CommuneWard(rs.getInt("Commune_Ward_ID"), 
											rs.getString("Commune_Ward_Name"),
											rs.getInt("District_ID")));
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
		return communes;
	}

	@Override
	public int insert(CommuneWard t){
		return 0;
	}

	@Override
	public int update(CommuneWard t){
		return 0;
	}

	@Override
	public int delete(CommuneWard t){
		return 0;
	}

	@Override
	public ArrayList<CommuneWard> getAllCommuneWardByDistrictId(int districtId){
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		ArrayList<CommuneWard> communes = new ArrayList<CommuneWard>();
		
		if(conn == null) return null;
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Commune_Ward WHERE District_ID=?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, districtId);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				communes.add(new CommuneWard(rs.getInt("Commune_Ward_ID"), 
											rs.getString("Commune_Ward_Name"),
											rs.getInt("District_ID")));
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
		return communes;
	}

	@Override
	public List<CommuneWard> get10(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

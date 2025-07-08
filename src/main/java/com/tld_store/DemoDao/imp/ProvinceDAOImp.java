package com.tld_store.DemoDao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dao.Database;
import dao.ProvinceDAO;
import dto.Province;
import error_handling.ErrorMessages;
import exception.CustomException;

@Repository
public class ProvinceDAOImp implements ProvinceDAO{

	@Override
	public Province findById(Integer id){
		// Get connection from to database
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		Province province = null;
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Province where Province_ID = ?";
		try 
		{
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				province = new Province(rs.getInt("Province_ID"), 
										rs.getString("Province_Name"));
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
		
		return province;
	}

	@Override
	public List<Province> getAll(){
		Connection conn = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		// Init Address DTO
		List<Province> provinces = new ArrayList<Province>();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Province";
		try 
		{
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				provinces.add(new Province(rs.getInt("Province_ID"), 
											rs.getString("Province_Name")));
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
		return provinces;
	}

	@Override
	public int insert(Province t){
		return 0;
	}

	@Override
	public int update(Province t){
		return 0;
	}

	@Override
	public int delete(Province t){
		return 0;
	}

	@Override
	public List<Province> get10(int numPage) {
		// TODO Auto-generated method stub
		return null;
	}

}

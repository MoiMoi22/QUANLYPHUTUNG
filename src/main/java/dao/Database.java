package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.CustomException;

public class Database {
	
	private static String connectionUrl = "jdbc:sqlserver://localhost:1433;encrypt=false;databaseName=PHUTUNG2;user=sa;password=12";
	
	private Database() {}
	
	public static Connection getConnection()
	{
		Connection con = null;
		try {
			con = DriverManager.getConnection(connectionUrl);
		} catch (SQLException e) {
			throw new CustomException("Lỗi kết nối database");
		}
		return con;
	}
	
	public static void closeConnection(Connection con)
	{
		try
		{
			if(con != null)
			{
				con.close();
			}
		}
		catch(SQLException e)
		{
			throw new CustomException("Lỗi kết nối database");
		}
	}
	
	public static void closePreparedStatement(PreparedStatement ps)
	{
		try 
		{
			if(ps != null)
			{
				ps.close();
			}
		}
		catch (SQLException e)
		{
			throw new CustomException("Lỗi kết nối database");
		}
	}
	
	public static void closeCallableStatement(CallableStatement cs)
	{
		try 
		{
			if(cs != null)
			{
				cs.close();
			}
		}
		catch (SQLException e)
		{
			throw new CustomException("Lỗi kết nối database");
		}
	}
	
	public static void closeResultSet(ResultSet rs)
	{
		try 
		{
			if(rs != null)
			{
				rs.close();
			}
		}
		catch (SQLException e)
		{
			throw new CustomException("Lỗi kết nối database");
		}
	}
	
}

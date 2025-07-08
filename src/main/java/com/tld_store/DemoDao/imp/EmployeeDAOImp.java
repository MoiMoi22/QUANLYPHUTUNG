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
import dao.EmployeeDAO;
import dto.Employee;
import error_handling.ErrorMessages;
import exception.CustomException;
@Repository
public class EmployeeDAOImp implements EmployeeDAO  {

	@Override
	public Employee findById(Integer id){
		
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo employees
		Employee emp = new Employee();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Employees where Employee_ID = ?";

		try 
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(!rs.next())
			{
				emp = null;
			}
			else {
				emp.setId(rs.getInt("Employee_ID"));
				emp.setFirstName(rs.getString("First_Name"));
				emp.setLastName(rs.getString("Last_Name"));
				emp.setRole(rs.getString("Role"));
				emp.setPhoneNum(rs.getString("Phone"));
				emp.setEmail(rs.getString("Email"));
				emp.setDob(rs.getDate("Date_of_Birth"));
				emp.setGender(rs.getString("Gender"));
				emp.setUrl(rs.getString("Url"));
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
		
		return emp;
	}

	@Override
	public List<Employee> get10(int numPage){
		
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo list
		ArrayList<Employee> arr = new ArrayList<>();
		Employee emp = null;
		
		int offset = (numPage-1) * 10;
		
		//Truy vấn các employees trong 1 table
		String sql =  "SELECT *\n"
				+     "FROM Employees\n"
				+ 	  "ORDER BY Employee_ID\n"
				+ 	  "OFFSET ? ROWS\n"
				+     "FETCH NEXT 11 ROW ONLY";
		try 
		{
			ps = con.prepareStatement(sql);
			ps.setInt(1, offset);
			rs = ps.executeQuery();
			while(rs.next())
			{
				emp = new Employee();
				emp.setId(rs.getInt("Employee_ID"));
				emp.setFirstName(rs.getString("First_Name"));
				emp.setLastName(rs.getString("Last_Name"));
				emp.setRole(rs.getString("Role"));
				emp.setPhoneNum(rs.getString("Phone"));
				emp.setEmail(rs.getString("Email"));
				emp.setDob(rs.getDate("Date_of_Birth"));
				emp.setGender(rs.getString("Gender"));
				emp.setUrl(rs.getString("Url"));
				arr.add(emp);
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
		return arr;
	}

	@Override
	public int insert(Employee t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		int result = 1;
		String sql =  "{call sp_insertEmployee(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			cbst.setString(1, t.getFirstName());
			cbst.setString(2, t.getLastName());
			cbst.setString(3, t.getRole());
			cbst.setString(4, t.getPhoneNum());
			cbst.setString(5, t.getEmail());
			cbst.setDate(6, t.getDob());
			cbst.setString(7, t.getGender());
			cbst.setString(8, t.getPassword());
			cbst.setString(9, t.getUrl());
			
//			cbst = con.prepareCall(sql);
//			cbst.setNull(1, java.sql.Types.NVARCHAR);
//			cbst.setNull(2, java.sql.Types.NVARCHAR);
//			cbst.setString(3, t.getRole());
//			cbst.setNull(4, java.sql.Types.VARCHAR);
//			cbst.setString(5, t.getEmail());
//			cbst.setNull(6, java.sql.Types.DATE);
//			cbst.setNull(7, java.sql.Types.CHAR);
//			cbst.setString(8, t.getUsername());
//			cbst.setString(9, t.getPassword());
//			cbst.setNull(10, java.sql.Types.NVARCHAR);

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
	public int update(Employee t){
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Employees SET First_Name = ?, Last_Name = ?, Role = ?, Phone = ?, Email = ?, "
	               + "Date_of_Birth = ?, Gender = ?, Url = ? WHERE Employee_ID = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        ps.setString(1, t.getFirstName());
	        ps.setString(2, t.getLastName());
	        ps.setString(3, t.getRole());
	        ps.setString(4, t.getPhoneNum());
	        ps.setString(5, t.getEmail());
	        ps.setDate(6, t.getDob());
	        ps.setString(7, t.getGender());
            ps.setString(8, t.getUrl());
	        ps.setInt(9, t.getId());
	        ps.executeUpdate();
	        result = 1;
	    } catch (SQLException e) {
	    	throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    } finally {
	        Database.closePreparedStatement(ps);
	        Database.closeConnection(con);
	    }
	    return result;
	}

	@Override
	public int delete(Employee t){
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		
		//Khởi tạo list
		int result = 1;
		String sql = "{CALL sp_deleteById (?, ?)}";
		try 
		{
			cbst = con.prepareCall(sql);
			
			cbst.setString(1, "Employees");
			cbst.setString(2, Integer.toString(t.getId()));

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
	public String getFullNameById(Integer id){
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String fullName = null;

	    String sql = "SELECT First_Name, Last_Name FROM Employees WHERE Employee_ID = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        ps.setInt(1, id);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            fullName = rs.getString("First_Name") + " " + rs.getString("Last_Name");
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
	    return fullName;
	}

	@Override
	public Employee getEmployeeByUserName(String username){
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    Employee emp = null;

	    String sql = "SELECT * FROM Employees WHERE Email = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        ps.setString(1, username);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            emp = new Employee();
	            emp.setId(rs.getInt("Employee_ID"));
	            emp.setFirstName(rs.getString("First_Name"));
	            emp.setLastName(rs.getString("Last_Name"));
	            emp.setRole(rs.getString("Role"));
	            emp.setPhoneNum(rs.getString("Phone"));
	            emp.setEmail(rs.getString("Email"));
	            emp.setDob(rs.getDate("Date_of_Birth"));
	            emp.setGender(rs.getString("Gender"));
	            emp.setPassword(rs.getString("Password"));
	            emp.setUrl(rs.getString("Url"));
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
	    return emp;
	}

	@Override
	public List<Employee> getAll() {
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo list
		ArrayList<Employee> arr = new ArrayList<>();
		Employee emp = null;
		
		//Truy vấn các employees trong 1 table
		String sql =  "Select * from Employees";
		try 
		{
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next())
			{
				emp = new Employee();
				emp.setId(rs.getInt("Employee_ID"));
				emp.setFirstName(rs.getString("First_Name"));
				emp.setLastName(rs.getString("Last_Name"));
				emp.setRole(rs.getString("Role"));
				emp.setPhoneNum(rs.getString("Phone"));
				emp.setEmail(rs.getString("Email"));
				emp.setDob(rs.getDate("Date_of_Birth"));
				emp.setGender(rs.getString("Gender"));
				emp.setUrl(rs.getString("Url"));
				arr.add(emp);
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
		return arr;
	}

	@Override
	public ArrayList<Employee> getTop10ByFullName(String fullName, int numPage) {
		Connection con = Database.getConnection();
		CallableStatement cbst = null;
		ResultSet rs = null;
		
		//Khởi tạo list
		ArrayList<Employee> arr = new ArrayList<>();
		Employee emp = null;
		
		int offset = (numPage-1)* 10;
		
		String sql =  "{call sp_SearchByFullNameEmployees(?, ?)}";
		
		try {
			cbst = con.prepareCall(sql);
			cbst.setInt(1, offset);
			cbst.setString(2, fullName);
			rs = cbst.executeQuery();
			while(rs.next())
			{
				emp = new Employee();
				emp.setId(rs.getInt("Employee_ID"));
				emp.setFirstName(rs.getString("First_Name"));
				emp.setLastName(rs.getString("Last_Name"));
				emp.setRole(rs.getString("Role"));
				emp.setPhoneNum(rs.getString("Phone"));
				emp.setEmail(rs.getString("Email"));
				emp.setDob(rs.getDate("Date_of_Birth"));
				emp.setGender(rs.getString("Gender"));
				emp.setUrl(rs.getString("Url"));
				arr.add(emp);
			}			
		}
		catch (SQLException e) {
			Database.closeResultSet(rs);
			Database.closeCallableStatement(cbst);
			Database.closeConnection(con);	
			throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
			
		}
		finally {
			Database.closeResultSet(rs);
			Database.closeCallableStatement(cbst);
			Database.closeConnection(con);
		}
		
		return arr;
	}
	
	public Employee checkPassword(String userName, String passWord) {
		
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo employees
		Employee emp = new Employee();
		
		//Truy vấn các employees trong 1 table
		String sql = "{Call sp_checkPassword (?, ?)}";

		try 
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, passWord);
			rs = ps.executeQuery();
			
			if(!rs.next())
			{
				emp = null;
			}
			else {
				emp.setId(rs.getInt("Employee_ID"));
				emp.setFirstName(rs.getString("First_Name"));
				emp.setLastName(rs.getString("Last_Name"));
				emp.setRole(rs.getString("Role"));
				emp.setPhoneNum(rs.getString("Phone"));
				emp.setEmail(rs.getString("Email"));
				emp.setDob(rs.getDate("Date_of_Birth"));
				emp.setGender(rs.getString("Gender"));
				emp.setUrl(rs.getString("Url"));
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
		
		return emp;
	}

	@Override
	public Employee findByEmail(String email) {
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo employees
		Employee emp = new Employee();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Employees where Email = ?";

		try 
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			
			if(!rs.next())
			{
				emp = null;
			}
			else {
				emp.setId(rs.getInt("Employee_ID"));
				emp.setFirstName(rs.getString("First_Name"));
				emp.setLastName(rs.getString("Last_Name"));
				emp.setRole(rs.getString("Role"));
				emp.setPhoneNum(rs.getString("Phone"));
				emp.setEmail(rs.getString("Email"));
				emp.setDob(rs.getDate("Date_of_Birth"));
				emp.setGender(rs.getString("Gender"));
				emp.setUrl(rs.getString("Url"));
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
		
		return emp;
	}

	@Override
	public int insertToken(String email, String token) {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Employees SET Reset_Token = ? \n"
	               + " WHERE Email = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        
	        ps.setString(1, token);	        
	        ps.setString(2, email);

	        
	        result = ps.executeUpdate();
	    } catch (SQLException e) {
	    	throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    } finally {
	        Database.closePreparedStatement(ps);
	        Database.closeConnection(con);
	    }
	    return result;
	}

	@Override
	public Employee findByToken(String token) {
		//Tạo kết nối, khởi tạo PreparedStatement và ResultSet
		Connection con = Database.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Khởi tạo employees
		Employee emp = new Employee();
		
		//Truy vấn các employees trong 1 table
		String sql = "Select * from Employees where Reset_Token = ?";

		try 
		{
			ps = con.prepareStatement(sql);
			ps.setString(1, token);
			rs = ps.executeQuery();
			
			if(!rs.next())
			{
				emp = null;
			}
			else {
				emp.setId(rs.getInt("Employee_ID"));
				emp.setFirstName(rs.getString("First_Name"));
				emp.setLastName(rs.getString("Last_Name"));
				emp.setRole(rs.getString("Role"));
				emp.setPhoneNum(rs.getString("Phone"));
				emp.setEmail(rs.getString("Email"));
				emp.setDob(rs.getDate("Date_of_Birth"));
				emp.setGender(rs.getString("Gender"));
				emp.setUrl(rs.getString("Url"));
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
		
		return emp;
	}

	@Override
	public int deleteToken(String token) {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Employees SET Reset_Token = ?, "
	               + " WHERE Reset_Token = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        
			ps.setNull(1, java.sql.Types.VARCHAR);
	        ps.setString(2, token);

	        ps.executeUpdate();
	        result = 1;
	    } catch (SQLException e) {
	    	throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    } finally {
	        Database.closePreparedStatement(ps);
	        Database.closeConnection(con);
	    }
	    return result;
	}

	@Override
	public int updatePassword(int id, String newPassword) {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Employees SET Password = ?, Reset_Token = ?"
	               + " WHERE Employee_ID = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        
			ps.setString(1, newPassword);
			ps.setNull(2,java.sql.Types.VARCHAR);
	        ps.setInt(3, id);

	        ps.executeUpdate();
	        result = 1;
	    } catch (SQLException e) {
	    	throw new CustomException(ErrorMessages.getSQLErrorMessage("SQL" + e.getErrorCode()));
	    } finally {
	        Database.closePreparedStatement(ps);
	        Database.closeConnection(con);
	    }
	    return result;
	}
	
	@Override
	public int updateUrl(String email, String url) {
	    Connection con = Database.getConnection();
	    PreparedStatement ps = null;
	    int result = -1;

	    String sql = "UPDATE Employees SET Url = ?\n "
	               + " WHERE Email = ?";

	    try {
	        ps = con.prepareStatement(sql);
	        
			ps.setString(1, url);
	        ps.setString(2, email);

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

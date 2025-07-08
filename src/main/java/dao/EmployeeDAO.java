package dao;

import java.util.ArrayList;

import dto.Employee;

public interface EmployeeDAO extends DAO<Employee, Integer> {
	
	String getFullNameById(Integer id);
	
	Employee getEmployeeByUserName(String username);
	
	ArrayList<Employee> getTop10ByFullName(String fullName, int numPage);
	
	public Employee checkPassword(String userName, String passWord);
	
	public Employee findByEmail(String email);
	
	public int insertToken( String email, String token);
	
	public Employee findByToken(String token);
	
	public int deleteToken(String token);
	
	public int updatePassword(int id, String newPassword);
	
	public int updateUrl(String email, String token);
	
}

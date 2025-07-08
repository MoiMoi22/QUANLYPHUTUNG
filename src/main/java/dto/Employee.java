package dto;

import java.sql.Date;

public class Employee extends PeopleTemplate
{
	private String role;
	private String password;
	
	public Employee()
	{
		super();
	}
	public Employee(int id, String firstName, String lastName, String phoneNum, String email, Date dob, String gender, String url, String role, String password) {
		super(id, firstName, lastName, phoneNum, email, dob, gender, url);
		this.role = role;
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
	    return "Employee{" +
	            "id=" + getId() +
	            ", firstName='" + getFirstName() + '\'' +
	            ", lastName='" + getLastName() + '\'' +
	            ", phoneNum='" + getPhoneNum() + '\'' +
	            ", email='" + getEmail() + '\'' +
	            ", dob=" + getDob() +
	            ", gender='" + getGender() + '\'' +
	            ", url='" + getUrl() + '\'' +
	            ", role='" + role + '\'' +
	            ", password='" + password + '\'' +
	            '}';
	}

	
}

package dto;

import java.sql.Date;

public class Shopper extends PeopleTemplate {

	public Shopper() {}
	
	public Shopper(int id, String firstName, String lastName, String phoneNum, String email, Date dob, String gender, String url) {
		super(id, firstName, lastName, phoneNum, email, dob, gender, url);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
	    return "Shopper{" +
	            "id=" + getId() +
	            ", firstName='" + getFirstName() + '\'' +
	            ", lastName='" + getLastName() + '\'' +
	            ", phoneNum='" + getPhoneNum() + '\'' +
	            ", email='" + getEmail() + '\'' +
	            ", dob=" + getDob() +
	            ", gender='" + getGender() + '\'' +
	            ", url='" + getUrl() + '\'' +
	            '}';
	}


}

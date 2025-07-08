package dto;

public class Address 
{
	private int addressId;
	private String addressName;
	private int communeWardId;
	
	public Address() {}

	public Address(int addressId, String addressName, int communeWardId) {
		super();
		this.addressId = addressId;
		this.addressName = addressName;
		this.communeWardId = communeWardId;
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public int getCommuneWardId() {
		return communeWardId;
	}

	public void setCommuneWardId(int communeWardId) {
		this.communeWardId = communeWardId;
	}
	
}

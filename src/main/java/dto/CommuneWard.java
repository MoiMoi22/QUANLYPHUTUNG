package dto;

public class CommuneWard {
	private int communeId;
	private String communeName;
	private int districtId;
	
	public CommuneWard() {}
	
	public CommuneWard(int communeId, String communeName, int districtId) {
		super();
		this.communeId = communeId;
		this.communeName = communeName;
		this.districtId = districtId;
	}
	public int getCommuneId() {
		return communeId;
	}
	public void setCommuneId(int communeId) {
		this.communeId = communeId;
	}
	public String getCommuneName() {
		return communeName;
	}
	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}
	

	
}

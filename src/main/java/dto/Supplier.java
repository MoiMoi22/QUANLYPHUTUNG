package dto;

public class Supplier {
	 private int supplierId;
	    private String companyName;
	    private String contactName;
	    private String contactTitle;
	    private String phone;
	    private String email;
	    private String url;
	    private int supplierAddressId;

	    // Constructor không tham số
	    public Supplier() {}

	    // Constructor có tham số
	    public Supplier(int supplierId, String companyName, String contactName, String contactTitle, String phone, String email, String url, int supplierAddressId) {
	        this.supplierId = supplierId;
	        this.companyName = companyName;
	        this.contactName = contactName;
	        this.contactTitle = contactTitle;
	        this.phone = phone;
	        this.email = email;
	        this.supplierAddressId = supplierAddressId;
	        this.url = url;
	    }

	    // Getter và Setter cho từng thuộc tính
	    public int getSupplierId() {
	        return supplierId;
	    }

	    public void setSupplierId(int supplierId) {
	        this.supplierId = supplierId;
	    }

	    public String getCompanyName() {
	        return companyName;
	    }

	    public void setCompanyName(String companyName) {
	        this.companyName = companyName;
	    }

	    public String getContactName() {
	        return contactName;
	    }

	    public void setContactName(String contactName) {
	        this.contactName = contactName;
	    }

	    public String getContactTitle() {
	        return contactTitle;
	    }

	    public void setContactTitle(String contactTitle) {
	        this.contactTitle = contactTitle;
	    }

	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public int getSupplierAddressId() {
	        return supplierAddressId;
	    }

	    public void setSupplierAddressId(int supplierAddressId) {
	        this.supplierAddressId = supplierAddressId;
	    }
	    
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	    
}

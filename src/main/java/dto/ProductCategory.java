package dto;

public class ProductCategory {
	private String categoryId;
	private String name;
	private String description;
	
	public ProductCategory() {}
	
	public ProductCategory(String categoryId, String name, String description) {
		super();
		this.categoryId = categoryId;
		this.name = name;
		this.description = description;
	}

	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
	    return "ProductCategory{" +
	            "categoryId='" + categoryId + '\'' +
	            ", name='" + name + '\'' +
	            ", description='" + description + '\'' +
	            '}';
	}

}

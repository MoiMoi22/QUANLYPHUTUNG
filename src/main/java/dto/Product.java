package dto;

public class Product 
{
	private String productId;
    private String productName;
    private String description;
    private String categoryId;
    private String url;
    private float weight;
    private int shelfLife;
    private float price;
    private int quantity;

    // Constructors
    public Product() {}

    
    
	public Product(String productId, String productName, String description, String categoryId, String url,
			float weight, int shelfLife, float price, int quantity) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.description = description;
		this.categoryId = categoryId;
		this.url = url;
		this.weight = weight;
		this.shelfLife = shelfLife;
		this.price = price;
		this.quantity = quantity;
	}



	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(int shelfLife) {
		this.shelfLife = shelfLife;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
	    return "Product {" +
	           "productId='" + productId + '\'' +
	           ", productName='" + productName + '\'' +
	           ", description='" + description + '\'' +
	           ", categoryId='" + categoryId + '\'' +
	           ", url='" + url + '\'' +
	           ", weight=" + weight +
	           ", shelfLife=" + shelfLife +
	           ", price=" + price +
	           ", quantity=" + quantity +
	           '}';
	}

    
}

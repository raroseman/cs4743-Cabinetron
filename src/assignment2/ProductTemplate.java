package assignment2;

public class ProductTemplate {
	// each has: ProductTemplatePartModel
	// which contains a reference to this template ID, a list of parts, and their quantities
	
	// Thus, this also has an ID
	private Integer ID;
	private String productNumber = "", description = "", timestamp = "";
	private ProductTemplatePartModel templatePartModel;
	
	// Does not include the database ID or time stamp
	public ProductTemplate(String productNumber, String description) {
		setProductNumber(productNumber);
		setDescription(description);
	}
	
	// Does not include the database time stamp
	public ProductTemplate(Integer ID, String productNumber, String description) {
		this(productNumber, description);
		setID(ID);
	}
	
	// Does not include the database ID 
	public ProductTemplate(String productNumber, String description, String timestamp) {
		this(productNumber, description);
		setTimestamp(timestamp);
	}
	
	// Includes both the database ID and time stamp
	public ProductTemplate(Integer ID, String productNumber, String description, String timestamp) {
		this(ID, productNumber, description);
		setTimestamp(timestamp);
	}
	
	public Integer getID() {
		return this.ID;
	}
	
	public String getProductNumber() {
		return this.productNumber;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	private void setID(Integer ID) {
		this.ID = ID;
	}
	
	private void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	
	private void setDescription(String description) {
		this.description = description;
	}
	
	private void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}

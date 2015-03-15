package assignment2;

import java.io.IOException;
import java.util.Comparator;

public class ProductTemplate implements Comparator<ProductTemplate> {
	// each has: ProductTemplatePartModel
	// which contains a reference to this template ID, a list of parts, and their quantities
	
	// Thus, this also has an ID
	private Integer ID; // this is the templateID as referenced by the templatePartModel
	private String productNumber = "", description = "", timestamp = "";
	private ProductTemplatePartModel templatePartModel;
	private static int maxProductNumberLength = 20;
	private static int maxDescriptionLength = 255;
	
	// Does not include the database ID or time stamp
	public ProductTemplate(String productNumber, String description) throws IOException {
		setProductNumber(productNumber);
		setDescription(description);
	}
	
	// Does not include the database time stamp
	public ProductTemplate(Integer ID, String productNumber, String description) throws IOException {
		this(productNumber, description);
		setID(ID);
		templatePartModel = new ProductTemplatePartModel(ID);
	}
	
	// Does not include the database ID 
	public ProductTemplate(String productNumber, String description, String timestamp) throws IOException {
		this(productNumber, description);
		setTimestamp(timestamp);
	}
	
	// Includes both the database ID and time stamp
	public ProductTemplate(Integer ID, String productNumber, String description, String timestamp) throws IOException {
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
	
	public ProductTemplatePartModel getProductTemplatePartModel() {
		return this.templatePartModel;
	}
	
	private void setID(Integer ID) {
		this.ID = ID;
	}
	
	private void setProductNumber(String productNumber) throws IOException {
		if (!(productNumber.trim().startsWith("A"))) { // Must start with "A"
			throw new IOException("Error: product number must start with an \"A\".");
		}
		if (productNumber.trim().length() > maxProductNumberLength) {
			throw new IOException("Error: product number is too long (" + maxProductNumberLength + " characters max).");
		}
		else if (productNumber.trim().length() == 0) {
			throw new IOException("Error: product number is required.");
		}
		else {
			this.productNumber = productNumber.trim(); // still need to test for uniqueness in the database
		}
	}
	
	private void setDescription(String description) throws IOException {
		if (description.length() > maxDescriptionLength) {
			throw new IOException("Error: description is too long (" + maxDescriptionLength + " characters max).");
		}
		else if (description.trim().length() == 0) {
			throw new IOException("Error: description is required.");
		}
		else {
			this.description = description.trim();
		}
	}
	
	private void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	// used to sort by ID in descending order
	public static Comparator<ProductTemplate> IDDescending = new Comparator<ProductTemplate>() {
		public int compare(ProductTemplate template, ProductTemplate anotherTemplate) {
			Integer ID1 = template.getID();
			Integer ID2 = anotherTemplate.getID();
			return ID1.compareTo(ID2);
		}
	};
	
	// used to sort by ID in ascending order
	public static Comparator<ProductTemplate> IDAscending = new Comparator<ProductTemplate>() {
		public int compare(ProductTemplate template, ProductTemplate anotherTemplate) {
			Integer ID1 = template.getID();
			Integer ID2 = anotherTemplate.getID();
			return ID2.compareTo(ID1);
		}
	};
	
	// used to sort by Product # in descending order
	public static Comparator<ProductTemplate> ProductNumberDescending = new Comparator<ProductTemplate>() {
		public int compare(ProductTemplate template, ProductTemplate anotherTemplate) {
			String productNumber1 = template.getProductNumber();
			String productNumber2 = anotherTemplate.getProductNumber();
			return productNumber1.compareTo(productNumber2);
		}
	};
	
	// used to sort by Product # in ascending order
	public static Comparator<ProductTemplate> ProductNumberAscending = new Comparator<ProductTemplate>() {
		public int compare(ProductTemplate template, ProductTemplate anotherTemplate) {
			String productNumber1 = template.getProductNumber();
			String productNumber2 = anotherTemplate.getProductNumber();
			return productNumber2.compareTo(productNumber1);
		}
	};
	
	// used to sort by Description in descending order
	public static Comparator<ProductTemplate> DescriptionDescending = new Comparator<ProductTemplate>() {
		public int compare(ProductTemplate template, ProductTemplate anotherTemplate) {
			String description1 = template.getDescription();
			String description2 = anotherTemplate.getDescription();
			return description1.compareTo(description2);
		}
	};
	
	// used to sort by Description in descending order
	public static Comparator<ProductTemplate> DescriptionAscending = new Comparator<ProductTemplate>() {
		public int compare(ProductTemplate template, ProductTemplate anotherTemplate) {
			String description1 = template.getDescription();
			String description2 = anotherTemplate.getDescription();
			return description2.compareTo(description1);
		}
	};

	@Override
	public int compare(ProductTemplate o1, ProductTemplate o2) {
		// TODO Auto-generated method stub
		return 0;
	}
}

package assignment2;

import java.io.IOException;
import java.util.Comparator;

public class ProductTemplatePart implements Comparator<ProductTemplatePart> {

	private Integer ID = 0;
	private Integer productTemplateID = 0;
	private Integer partID = 0;
	private Integer quantity = 0;
	private String timestamp = "";
	private ProductTemplate template;
	private Part part;
	
	// Does not contain database ID or timestamp
	public ProductTemplatePart(Integer productTemplateID, Integer partID, Integer quantity) throws IOException {
		try {
			setProductTemplateID(productTemplateID);
			setPartID(partID);
			setQuantity(quantity);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	// Contains database ID -- does not contain timestamp
	public ProductTemplatePart(Integer ID, Integer productTemplateID, Integer partID, Integer quantity) throws IOException {
		this(productTemplateID, partID, quantity);
		try {
			setID(ID);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	// Contains database timestamp -- does not contain ID
	public ProductTemplatePart(Integer productTemplateID, Integer partID, Integer quantity, String timestamp) throws IOException {
		this(productTemplateID, partID, quantity);
		setTimestamp(timestamp);
	}
	
	// Contains database ID and timestamp
	public ProductTemplatePart(Integer ID, Integer productTemplateID, Integer partID, Integer quantity, String timestamp) throws IOException {
		this(ID, productTemplateID, partID, quantity);
		setTimestamp(timestamp);
	}
	
	// Contains database ID, timestamp, and object references
	public ProductTemplatePart(Integer ID, ProductTemplate template, Part part, Integer quantity, String timestamp) throws IOException {
		this(ID, template.getID(), part.getID(), quantity, timestamp);
		setProductTemplate(template);
		setPart(part);
	}
	
	public Integer getQuantity() {
		return this.quantity;
	}
	
	public Integer getPartID() {
		return this.partID;
	}
	
	public Integer getProductTemplateID() {
		return this.productTemplateID;
	}
	
	public Integer getID() {
		return this.ID;
	}

	public String getTimestamp() {
		return this.timestamp;
	}
	
	public ProductTemplate getProductTemplate() {
		return this.template;
	}
	
	public Part getPart() {
		return this.part;
	}
	
	private void setID(Integer ID) throws IOException {
		if (ID < 1) {
			throw new IOException("Error: cannot assign negative value to ID.");
		}
		else {
			this.ID = ID;
		}
	}
	
	private void setQuantity(Integer quantity) throws IOException {
		if (quantity <= 0) {
			throw new IOException("Error: quantity of a part in a product template must be > 0.");
		}
		this.quantity = quantity;
	}
	
	private void setProductTemplateID(Integer productTemplateID) {
		this.productTemplateID = productTemplateID;
	}
	
	private void setPartID(Integer partID) {
		this.partID = partID;
	}
	
	private void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	private void setProductTemplate(ProductTemplate template) throws IOException {
		if (template == null) {
			throw new IOException("Error: No ProductTemplate linked to this ProductTemplatePart.");
		}
		this.template = template;
	}
	
	private void setPart(Part part) throws IOException {
		if (part == null) {
			throw new IOException("Error: No Part linked to this ProductTemplatePart.");
		}
		this.part = part;
	}
	
	// used to sort by ID in descending order
	public static Comparator<ProductTemplatePart> IDDescending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer ID1 = template.getID();
			Integer ID2 = anotherTemplate.getID();
			return ID1.compareTo(ID2);
		}
	};
	
	// used to sort by ID in ascending order
	public static Comparator<ProductTemplatePart> IDAscending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer ID1 = template.getID();
			Integer ID2 = anotherTemplate.getID();
			return ID2.compareTo(ID1);
		}
	};
	
	// used to sort by quantity in descending order
	public static Comparator<ProductTemplatePart> QuantityDescending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer quantity1 = template.getQuantity();
			Integer quantity2 = anotherTemplate.getQuantity();
			return quantity1.compareTo(quantity2);
		}
	};
	
	// used to sort by quantity in ascending order
	public static Comparator<ProductTemplatePart> QuantityAscending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer quantity1 = template.getQuantity();
			Integer quantity2 = anotherTemplate.getQuantity();
			return quantity2.compareTo(quantity1);
		}
	};
	
	// used to sort by ProductTemplateID in descending order
	public static Comparator<ProductTemplatePart> ProductTemplateIDDescending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer ID1 = template.getProductTemplateID();
			Integer ID2 = anotherTemplate.getProductTemplateID();
			return ID1.compareTo(ID2);
		}
	};
	
	// used to sort by ProductTemplateID in descending order
	public static Comparator<ProductTemplatePart> ProductTemplateIDAscending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer ID1 = template.getProductTemplateID();
			Integer ID2 = anotherTemplate.getProductTemplateID();
			return ID2.compareTo(ID1);
		}
	};
	
	// used to sort by PartID in descending order
	public static Comparator<ProductTemplatePart> PartIDDescending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer ID1 = template.getPartID();
			Integer ID2 = anotherTemplate.getPartID();
			return ID1.compareTo(ID2);
		}
	};
	
	// used to sort by PartID in descending order
	public static Comparator<ProductTemplatePart> PartIDAscending = new Comparator<ProductTemplatePart>() {
		public int compare(ProductTemplatePart template, ProductTemplatePart anotherTemplate) {
			Integer ID1 = template.getPartID();
			Integer ID2 = anotherTemplate.getPartID();
			return ID2.compareTo(ID1);
		}
	};
		
	@Override
	public int compare(ProductTemplatePart o1, ProductTemplatePart o2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

package assignment2;

import java.io.IOException;
import java.util.Comparator;

public class InventoryItem implements Comparable<InventoryItem> {
	private Integer id = 0;
	private Integer partID = 0;
	private Integer quantity = 0;
	private Part part = null;
	private String location = "Unknown";
	
	/*
	 * Does not include the optional parameters: ID (required for reference only),
	 * Since the ID field is determined by the database, setting the ID before it is assigned by the database
	 * may introduce inconsistencies.
	 */
	public InventoryItem(Integer partID, String location, Integer quantity) throws IOException {
		try {
			setPartID(partID);
			setQuantity(quantity);
			setLocation(location);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	public InventoryItem(Part p, String location, Integer quantity) throws IOException {
		try {
			setPart(p);
			setQuantity(quantity);
			setLocation(location);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	public InventoryItem(Integer id, Integer partID, String location, Integer quantity) throws IOException {
		try {
			setID(id);
			setPartID(partID);
			setQuantity(quantity);
			setLocation(location);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	public InventoryItem(Integer id, Part p, String location, Integer quantity) throws IOException {
		try {
			setID(id);
			setPart(p);
			setQuantity(quantity);
			setLocation(location);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	public Integer getID() {
		return this.id;
	}
	
	public Integer getPartID() {
		return this.partID;
	}
	
	public Part getPart() {
		return this.part;
	}
	
	public String getLocation() {
		return this.location;
	}

	public Integer getQuantity() {
		return this.quantity;
	}
	
	
	private void setID(Integer id) throws IOException {
		if (id < 1) {
			throw new IOException("Error: cannot assign negative value to quantity.");
		}
		else {
			this.id = id;
		}
	}
	
	private void setPartID(Integer partID) throws IOException {
		if (partID < 1) {
			throw new IOException("Error: part ID must be greater than zero.");
		}
		else {
			this.partID = partID;
		}
	}
	
	private void setPart(Part p) throws IOException {
		if (p == null) {
			throw new IOException("Error: No Part linked to this InventoryItem.");
		}
		else {
			this.part = p;
		}
	}
	
	private void setQuantity(Integer quantity) throws IOException {
		if (quantity < 0) {
			throw new IOException("Error: cannot assign negative value to quantity.");
		}
		else {
			this.quantity = quantity;
		}
	}
	
	private void setLocation(String location) throws IOException {
		if (location.equals("Unknown")) {
			throw new IOException("Error: location cannot be listed as \"unknown.\"");
		}
		else {
			this.location = location;
		}
	}
	
	// sort by ID in descending order
	public static Comparator<InventoryItem> IDDescending = new Comparator<InventoryItem>() {
		public int compare(InventoryItem item, InventoryItem anotherItem) {
			Integer id1 = item.getID();
			Integer id2 = anotherItem.getID();
			return id1.compareTo(id2);
		}
	};
	
	// sort by ID in ascending order
	public static Comparator<InventoryItem> IDAscending = new Comparator<InventoryItem>() {
		public int compare(InventoryItem item, InventoryItem anotherItem) {
			Integer id1 = item.getID();
			Integer id2 = anotherItem.getID();
			return id2.compareTo(id1);
		}
	};
	
	@Override
	public int compareTo(InventoryItem o) {
		return 0;
	}
}
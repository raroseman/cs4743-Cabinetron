package assignment2;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventoryItemModel {
	private List<InventoryItem> inventoryItems;
	private Comparator<InventoryItem> sortingMode = InventoryItem.IDDescending; // default sort
	private InventoryItemGateway iig; 
	/**
	 * PDG should eventually be an InventoryItemGateway
	 */
	
	public InventoryItemModel() {
		iig = new InventoryItemGateway();
		inventoryItems = iig.getInventory(); // gets list of inventory items
	}
	
	public void addInventoryItem(InventoryItem ii) throws Exception {
		try {
			addInventoryItem(ii.getPartID(), ii.getLocation(), ii.getQuantity());
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public void addInventoryItem(Part p, String location, Integer quantity) throws Exception { // with optional args omitted
		try {
			addInventoryItem(p.getID(), location, quantity);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void addInventoryItem(Integer partID, String location, Integer quantity) throws Exception, IOException, SQLException { // all args
		if (quantity <= 0) {
			throw new IOException("Error: Quantity for a new item must be greater than zero.");
		}
		try {
			iig.addInventoryItem(partID, location, quantity);
			inventoryItems = iig.getInventory(); // update list of inventory items
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deletePart(InventoryItem ii) throws SQLException, IOException {
		try {
			iig.deleteInventoryItem(ii.getID()); // if it exists, first instance (unique, only one entry) is removed. otherwise does nothing
			inventoryItems = iig.getInventory(); // update list of inventory items
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		} catch (IOException ioe) {
			throw new IOException(ioe.getMessage());
		}
	}
	
	public void editInventoryItem(InventoryItem iiOld, InventoryItem iiNew) throws SQLException, IOException {
		try {
			iig.editInventoryItem(iiOld.getID(), iiNew.getPartID(), iiNew.getLocation(), iiNew.getQuantity());
			inventoryItems = iig.getInventory(); // gets list of inventory items
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		}
		catch (IOException ioe) {
			throw new SQLException(ioe.getMessage());
		}
	}
	
	public InventoryItem findItemByID(Integer i) {
		for (InventoryItem item : inventoryItems) { // this is O(n)
			if (item.getID().equals(i)) {
				return item;
			}
		}
		return null;
	}
	
	public int getSize() {
		return inventoryItems.size();
	}
	
	public List<InventoryItem> getInventory() { // for GUI output
		return inventoryItems;
	}
	
	public ArrayList<String> getLocations() throws SQLException {
		return iig.getLocations();
	}
	
	public ArrayList<String> getParts() throws SQLException {
		return iig.getParts();
	}
	
	public Integer getPartIDByPartNumber(String partNumber) throws SQLException {
		return iig.getPartIDByPartNumber(partNumber);
	}
	
	public String getPartNumberByID(Integer ID) throws SQLException {
		return iig.getPartNumberByID(ID);
	}
	
	public void sortByCurrentSortMethod() {
		inventoryItems.sort(sortingMode);
	}
	
	public void sortByID() {
		if (sortingMode == InventoryItem.IDDescending) {
			sortingMode = InventoryItem.IDAscending;
		}
		else {
			sortingMode = InventoryItem.IDDescending;
		}
		inventoryItems.sort(sortingMode);
	}
}

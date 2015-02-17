package assignment2;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class PartsInventoryModel {
	private List<Part> partsInventory;
	private Comparator<Part> sortingMode = Part.IDDescending; // default sort
	private PartsInventoryGateway pdg;
	
	public PartsInventoryModel() {
		pdg = new PartsInventoryGateway();
		partsInventory = pdg.getParts();
	}
	
	public void addPart(Part p) throws Exception {
		try {
			addPart(p.getQuantityUnitType(), p.getPartName(), p.getPartNumber(), p.getExternalPartNumber(), p.getVendor());
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public void addPart(String unitOfQuantity, String partName, String partNumber, String externalPartNumber) throws Exception {
		try {
			addPart(unitOfQuantity, partName, partNumber, externalPartNumber);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void addPart(String unitOfQuantity, String partName, String partNumber, String externalPartNumber, String vendor) throws Exception, IOException, SQLException {

		try {
			Part p = new Part(unitOfQuantity, partName, partNumber, externalPartNumber, vendor);
			pdg.addPart(p.getPartName(), p.getPartNumber(), p.getVendor(), p.getQuantityUnitType(), p.getExternalPartNumber());
			partsInventory = pdg.getParts();
		}
		catch (IOException ioe) {
			throw new IOException(ioe.getMessage());
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deletePart(Part p) throws SQLException, IOException {
		try {
			pdg.deletePart(p.getID()); // if it exists, first instance (unique, only one entry) is removed. otherwise does nothing
			partsInventory = pdg.getParts();
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			throw new IOException(ioe.getMessage());
		}
	}
	
	public void editPart(Part partOld, Part partNew) throws SQLException, IOException {
		try {
			pdg.editPart(partOld.getID(), partNew.getPartName(), partNew.getPartNumber(), partNew.getVendor(), 
						 partNew.getQuantityUnitType(), partNew.getExternalPartNumber());
			partsInventory = pdg.getParts();
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		}
		catch (IOException ioe) {
			throw new SQLException(ioe.getMessage());
		}
	}
	
	public Part findPartName(String partName) {
		if (partName.length() > Part.getMaxPartNameLength()) {
			partName = partName.substring(0, Part.getMaxPartNameLength()); // maybe just throw length exceeded exception...
		}
		for (Part part : partsInventory) { // this is O(n)
			if (part.getPartName().equals(partName)) {
				return part;
			}
		}
		return null;
	}
	
	public Part findPartNumber(String partNumber) {
		if (partNumber.length() > Part.getMaxPartNumberLength()) {
			partNumber = partNumber.substring(0, Part.getMaxPartNumberLength());
		}
		for (Part part : partsInventory) { // this is O(n)
			if (part.getPartNumber().equals(partNumber)) {
				return part;
			}
		}
		return null;
	}
	
	public int getSize() {
		return partsInventory.size();
	}
	
	public List<Part> getInventory() { // for GUI output
		return partsInventory;
	}
	
	public String[] getValidQuantityUnitTypes() {
		return Part.getValidQuantityUnitTypes();
	}
	
	public void sortByCurrentSortMethod() {
		partsInventory.sort(sortingMode);
	}
	
	public void sortByQuantityUnitType() {
		if (sortingMode == Part.QuantityUnitTypeDescending) {
			sortingMode = Part.QuantityUnitTypeAscending;
		}
		else {
			sortingMode = Part.QuantityUnitTypeDescending;
		}
		partsInventory.sort(sortingMode);
	}
	
	public void sortByPartName() {
		if (sortingMode == Part.PartNameDescending) {
			sortingMode = Part.PartNameAscending;
		}
		else {
			sortingMode = Part.PartNameDescending;
		}
		partsInventory.sort(sortingMode);
	}
	
	public void sortByPartNumber() {
		if (sortingMode == Part.PartNumberDescending) {
			sortingMode = Part.PartNumberAscending;
		}
		else {
			sortingMode = Part.PartNumberDescending;
		}
		partsInventory.sort(sortingMode);
	}
	
	public void sortByVendor() {
		if (sortingMode == Part.VendorDescending) {
			sortingMode = Part.VendorAscending;
		}
		else {
			sortingMode = Part.VendorDescending;
		}
		partsInventory.sort(sortingMode);
	}
	
	public void sortByID() {
		if (sortingMode == Part.IDDescending) {
			sortingMode = Part.IDAscending;
		}
		else {
			sortingMode = Part.IDDescending;
		}
		partsInventory.sort(sortingMode);
	}
	
	public void sortByExternalPartNumber() {
		if (sortingMode == Part.ExternalPartNumberDescending) {
			sortingMode = Part.ExternalPartNumberAscending;
		}
		else {
			sortingMode = Part.ExternalPartNumberDescending;
		}
		partsInventory.sort(sortingMode);
	}
	
}

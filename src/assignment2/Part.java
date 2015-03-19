package assignment2;

import java.io.IOException;
import java.util.Comparator;

public class Part implements Comparable<Part> {
	private Integer id = 0;
	private String partName = "";
	private String partNumber = "";
	private String externalPartNumber = null;
	private String vendor = null;
	private String quantityUnitType = "Unknown";
	private static int maxPartNameLength = 255;
	private static int maxPartNumberLength = 20;
	private static int maxVendorLength = 255;
	private static int maxExternalNumberLength = 50;
	
	/*
	 * Does not include the optional parameters: ID (required for reference only), externalPartNumber, vendor
	 * Since the ID field is determined by the database, setting the ID before it is assigned by the database
	 * may introduce inconsistencies.
	 */
	public Part(String quantityUnitType, String partName, String partNum) throws IOException {
		try {
			setQuantityUnitType(quantityUnitType);
			setPartName(partName);
			setPartNumber(partNum);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/*
	 * Does not include the optional parameter: ID, vendor
	 */
	public Part(String quantityUnitType, String partName, String partNum, String externalPartNumber) throws IOException {
		this(quantityUnitType, partName, partNum);
		try {
			setExternalPartNumber(externalPartNumber);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/*
	 * TODO: Constructor for --- NO ID, no externalPartNum (requires reordering constructor arguments)
	 */
	
	
	/*
	 * Does not include the optional parameter: ID
	 */
	public Part(String quantityUnitType, String partName, String partNum, String externalPartNumber, String vendor) throws IOException {
		this(quantityUnitType, partName, partNum, externalPartNumber);
		try {
			setVendor(vendor);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/*
	 * Does not include the optional parameters: externalPartNumber, vendor
	 */
	public Part(Integer id, String quantityUnitType, String partName, String partNum) throws IOException {
		this(quantityUnitType, partName, partNum);
		try {
			setID(id);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/*
	 * Does not include the optional parameter: vendor
	 */
	public Part(Integer id, String quantityUnitType, String partName, String partNum, String externalPartNumber) throws IOException {
		this(id, quantityUnitType, partName, partNum);
		try {
			setExternalPartNumber(externalPartNumber);
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/*
	 * TODO: Constructor for --- Does not include the optional parameter: externalPartNumber
	 */

	public Part(Integer id, String quantityUnitType, String partName, String partNum, String externalPartNumber, String vendor) throws IOException {
		this(id, quantityUnitType, partName, partNum, externalPartNumber);
		setVendor(vendor);
	}
	
	public String getQuantityUnitType() {
		return this.quantityUnitType;
	}
	
	public String getPartName() {
		return this.partName;
	}
	
	public Integer getID() {
		return this.id;
	}
	
	public String getPartNumber() {
		return this.partNumber;
	}
	
	public String getExternalPartNumber() {
		return this.externalPartNumber;
	}
	
	public String getVendor() {
		return this.vendor;
	}
	
	public static int getMaxPartNameLength() {
		return maxPartNameLength;
	}
	
	public static int getMaxPartNumberLength() {
		return maxPartNumberLength;
	}
	
	public static int getMaxVendorLength() {
		return maxVendorLength;
	}
	
	private void setID(Integer id) throws IOException {
		if (id < 1) {
			throw new IOException("Error: cannot assign negative value to quantity.");
		}
		else {
			this.id = id;
		}
	}
	
	private void setQuantityUnitType(String quantityUnitType) throws IOException {
		if (quantityUnitType.equals("Unknown")) {
			throw new IOException("Error: unit type cannot be listed as \"unknown.\"");
		}
		this.quantityUnitType = quantityUnitType;
	}
	
	private void setPartName(String partName) throws IOException {
		if (partName.length() > maxPartNameLength) {
			throw new IOException("Error: part name is too long (" + maxPartNameLength + " characters max).");
		}
		else if (partName.trim().length() == 0) {
			throw new IOException("Error: part name is required.");
		}
		else {
			this.partName = partName.trim();
		}
	}
	
	private void setPartNumber(String partNumber) throws IOException {
		if (partNumber.length() > maxPartNumberLength) {
			throw new IOException("Error: part number is too long (" + maxPartNumberLength + " characters max).");
		}
		else if (partNumber.trim().length() == 0) {
			throw new IOException("Error: part number is required.");
		}
		else if (!partNumber.trim().startsWith("P")) {
			throw new IOException("Error: part number must start with a \"P\".");
		}
		else {
			this.partNumber = partNumber.trim();
		}
	}
	
	private void setExternalPartNumber(String externalPartNumber) throws IOException {
		if (externalPartNumber != null) { // optional parameter
			if (externalPartNumber.length() > maxExternalNumberLength) {
				throw new IOException("Error: external part number is too long (" + maxExternalNumberLength + " characters max).");
			}
			else {
				this.externalPartNumber = externalPartNumber.trim();
			}
		}
	}
	
	private void setVendor(String vendor) throws IOException {
		if (vendor != null) { // optional parameter
			if (vendor.length() > maxVendorLength) {
				throw new IOException("Error: vendor name is too long (" + maxVendorLength + " characters max).");
			}
			else {
				this.vendor = vendor.trim();
			}
		}
	}
	
	// used to sort by unit type in descending order
		public static Comparator<Part> QuantityUnitTypeDescending = new Comparator<Part>() {
			public int compare(Part part, Part anotherPart) {
				String unitType1 = part.getQuantityUnitType();
				String unitType2 = anotherPart.getQuantityUnitType();
				return unitType1.compareTo(unitType2);
			}
		};
		
		// used to sort by unit type in ascending order
		public static Comparator<Part> QuantityUnitTypeAscending = new Comparator<Part>() {
			public int compare(Part part, Part anotherPart) {
				String unitType1 = part.getQuantityUnitType();
				String unitType2 = anotherPart.getQuantityUnitType();
				return unitType2.compareTo(unitType1);
			}
		};
	
	// used to sort by part name in descending order
	public static Comparator<Part> PartNameDescending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String partName1 = part.getPartName().toUpperCase();
			String partName2 = anotherPart.getPartName().toUpperCase();
			return partName1.compareTo(partName2);
		}
	};
	
	// used to sort by part name in ascending order
	public static Comparator<Part> PartNameAscending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String partName1 = part.getPartName().toUpperCase();
			String partName2 = anotherPart.getPartName().toUpperCase();
			return partName2.compareTo(partName1);
		}
	};
	
	// used to sort by part number in descending order
	public static Comparator<Part> PartNumberDescending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String partNumber1 = part.getPartNumber().toUpperCase();
			String partNumber2 = anotherPart.getPartNumber().toUpperCase();
			return partNumber1.compareTo(partNumber2);
		}
	};
	
	// used to sort by part number in ascending order
	public static Comparator<Part> PartNumberAscending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String partNumber1 = part.getPartNumber().toUpperCase();
			String partNumber2 = anotherPart.getPartNumber().toUpperCase();
			return partNumber2.compareTo(partNumber1);
		}
	};
	
	// used to sort by vendor in descending order
	public static Comparator<Part> VendorDescending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String vendor1 = part.getVendor().toUpperCase();
			String vendor2 = anotherPart.getVendor().toUpperCase();
			return vendor1.compareTo(vendor2);
		}
	};
	
	// used to sort by vendor in ascending order
	public static Comparator<Part> VendorAscending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String vendor1 = part.getVendor().toUpperCase();
			String vendor2 = anotherPart.getVendor().toUpperCase();
			return vendor2.compareTo(vendor1);
		}
	};
	
	// used to sort by ID in descending order
	public static Comparator<Part> IDDescending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			Integer vendor1 = part.getID();
			Integer vendor2 = anotherPart.getID();
			return vendor1.compareTo(vendor2);
		}
	};
	
	// used to sort by ID in ascending order
	public static Comparator<Part> IDAscending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			Integer vendor1 = part.getID();
			Integer vendor2 = anotherPart.getID();
			return vendor2.compareTo(vendor1);
		}
	};
	
	// used to sort by External Part # in descending order
	public static Comparator<Part> ExternalPartNumberDescending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String vendor1 = part.getExternalPartNumber();
			String vendor2 = anotherPart.getExternalPartNumber();
			return vendor1.compareTo(vendor2);
		}
	};
	
	// used to sort by External Part # in ascending order
	public static Comparator<Part> ExternalPartNumberAscending = new Comparator<Part>() {
		public int compare(Part part, Part anotherPart) {
			String vendor1 = part.getExternalPartNumber();
			String vendor2 = anotherPart.getExternalPartNumber();
			return vendor2.compareTo(vendor1);
		}
	};
	
	@Override
	public int compareTo(Part o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
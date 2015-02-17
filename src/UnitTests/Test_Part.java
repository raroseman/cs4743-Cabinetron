package UnitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import assignment2.Part;

public class Test_Part {
	Integer id;
	String unitOfQuantity;
	String partName;
	String partNumber;
	String externalPartNumber;
	String vendor;
	String location;
	Part p;
	
	@Before 
	public void setUp() {	
		id = 1;
		unitOfQuantity = "Pieces";
		partName = "The Part Name v1.0";
		partNumber = "18J-2015A1";
		externalPartNumber = "18D00B";
		vendor = "The_Vendor @ 1 UTSA Cir";
	}
	
	@Test
	public void testPartCreation_NoVendor() {
		try {
			p = new Part(id, unitOfQuantity, partName, partNumber, externalPartNumber);
			assertTrue(p.getQuantityUnitType().equals(unitOfQuantity));
			assertTrue(p.getPartName().equals(partName));
			assertTrue(p.getPartNumber().equals(partNumber));
			assertTrue(p.getVendor() == null);
		}
		catch (IOException e) {
			fail("Exception thrown during unexceptional part creation: \n\t" + e);
		}	
	}

	@Test
	public void testPartCreation_WithVendor() {
		try {
			p = new Part(id, unitOfQuantity, partName, partNumber, externalPartNumber, vendor);
			assertTrue(p.getQuantityUnitType().equals(unitOfQuantity));
			assertTrue(p.getPartName().equals(partName));
			assertTrue(p.getPartNumber().equals(partNumber));
			assertTrue(p.getVendor().equals(vendor));
		}
		catch (IOException e) {
			fail("Exception thrown during unexceptional part creation.");
		}	
	}
	
	@Test
	public void testPartCreation_PartNameLengthMax() {
		String longPartName = "";
		for (int i = 0; i < Part.getMaxPartNameLength(); i++) {
			longPartName = longPartName + "A"; // add one letter to the string
		}
		try {
			p = new Part(id, unitOfQuantity, longPartName, partNumber, externalPartNumber);
		}
		catch (IOException e) {
			fail("Exception thrown during unexceptional part creation: partName length is: " + 
					longPartName.length() + " and limit is: " + Part.getMaxPartNameLength());
		}
	}
	
	@Test (expected = IOException.class)
	public void testPartCreation_PartNameLengthExceeded() throws IOException {
		String longPartName = "";
		for (int i = 0; i < Part.getMaxPartNameLength() + 1; i++) {
			longPartName = longPartName + "A"; // add one letter to the string
		}
		try {
			p = new Part(id, unitOfQuantity, longPartName, partNumber, externalPartNumber);
			fail("Should have thrown an exception: partName length is: " + 
					p.getPartName().length() + " and limit is: " + Part.getMaxPartNameLength());
		}
		catch (IOException e) {
			throw new IOException(e); // expected behavior - should fail if no exception caught
		}
	}
	
	@Test
	public void testPartCreation_PartNumberLengthMax() {
		String longPartNumber = "";
		for (int i = 0; i < Part.getMaxPartNumberLength(); i++) {
			longPartNumber = longPartNumber + "A"; // add one letter to the string
		}
		try {
			p = new Part(id, unitOfQuantity, partName, longPartNumber, externalPartNumber);
		}
		catch (IOException e) {
			fail("Exception thrown during unexceptional part creation: partName length is: " + 
					longPartNumber.length() + " and limit is: " + Part.getMaxPartNumberLength());
		}
	}
	
	@Test (expected = IOException.class)
	public void testPartCreation_PartNumberLengthExceeded() throws IOException {
		String longPartNumber = "";
		for (int i = 0; i < Part.getMaxPartNumberLength() + 1; i++) {
			longPartNumber = longPartNumber + "A"; // add one letter to the string
		}
		try {
			p = new Part(id, unitOfQuantity, partName, longPartNumber, externalPartNumber);
			fail("Should have thrown an exception: partName length is: " + 
					p.getPartName().length() + " and limit is: " + Part.getMaxPartNumberLength());
		}
		catch (IOException e) {
			throw new IOException(e); // expected behavior - should fail if no exception caught
		}
	}
	
	@Test
	public void testPartCreation_VendorLengthMax() {
		String longVendor = "";
		for (int i = 0; i < Part.getMaxVendorLength(); i++) {
			longVendor = longVendor + "A"; // add one letter to the string
		}
		try {
			p = new Part(id, unitOfQuantity, partName, partNumber, externalPartNumber, longVendor);
		}
		catch (IOException e) {
			fail("Exception thrown during unexceptional part creation: vendor length is: " + 
					longVendor.length() + " and limit is: " + Part.getMaxVendorLength());
		}
	}
	
	@Test (expected = IOException.class)
	public void testPartCreation_VendorLengthExceeded() throws IOException {
		String longVendor = "";
		for (int i = 0; i < Part.getMaxVendorLength() + 1; i++) {
			longVendor = longVendor + "A"; // add one letter to the string
		}
		try {
			p = new Part(id, unitOfQuantity, partName, partNumber, longVendor);
			fail("Should have thrown an exception: vendor length is: " + 
					p.getVendor().length() + " and limit is: " + Part.getMaxVendorLength());
		}
		catch (IOException e) {
			throw new IOException(e); // expected behavior - should fail if no exception caught
		}
	}
}

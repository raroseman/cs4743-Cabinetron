package UnitTests;
import assignment2.Part;
import assignment2.PartsInventoryModel;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class Test_InventoryModel {

	PartsInventoryModel pim;
	Integer id;
	String quantityUnitType;
	String partName;
	String partNumber;
	String externalPartNumber;
	String vendor;
	Part p;
	
	@Before 
	public void setUp() {
		quantityUnitType = "Pieces";
		partName = "The Part Name v1.0";
		partNumber = "18J-2015A1";
		externalPartNumber = "18D00B";
		vendor = "The_Vendor @ 1 UTSA Cir";
		pim = new PartsInventoryModel();
	}
	/*
	@Test
	public void testInventoryModel_AddPartAsObject() {
		try {
			p = new Part(quantityUnitType, partName, partNumber, externalPartNumber);
			pim.addPart(p);
			assertTrue(pim.findPartName(partName).getPartName().equals(partName)); // If found, a Part is returned - validate partName match
		}
		catch (IOException e) {
			fail("IOException thrown during unexceptional part creation: \n\t" + e);
		}
		catch (Exception e) {
			fail("Exception thrown during unexceptional part creation: \n\t" + e);
		}	
	}
	
	@Test (expected = Exception.class)
	public void testInventoryModel_AddPartAsNullObject() throws Exception {
		try {
			p = null;
			pim.addPart(p);
			fail("Should have thrown an exception: null Part object was added.");
		}
		catch (IOException e) {
			fail("Should not have thrown an IOException, just an Exception: null Part object was added.");
		}
		catch (Exception e) {
			throw new Exception(e);
		}	
	}
	
	@Test
	public void testInventoryModel_AddPart() {
		try {
			pim.addPart(quantityUnitType, partName, partNumber, vendor);
		}
		catch (IOException e) {
			fail("IOException thrown during unexceptional part creation: \n\t" + e);
		}
		catch (Exception e) {
			fail("Exception thrown during unexceptional part creation: \n\t" + e);
		}	
	}
	
	@Test (expected = IOException.class)
	public void testInventoryModel_AddPartWithNegativeQuantity() throws IOException {
		try {
			pim.addPart(quantityUnitType, partName, partNumber, vendor);
			fail("Should have thrown an exception: Part with negative quantity was added.");
		}
		catch (IOException e) {
			throw new IOException(e);
		}
		catch (Exception e) {
			fail("Should have thrown an IOException, not an Exception: Part with negative quantity was added.");
		}	
	}
	
	@Test (expected = IOException.class)
	public void testInventoryModel_AddPartWithZeroQuantity() throws IOException {
		try {
			pim.addPart(quantityUnitType, partName, partNumber, vendor);
			fail("Should have thrown an IOException: Part with zero quantity was added.");
		}
		catch (IOException e) {
			throw new IOException(e);
		}
		catch (Exception e) {
			fail("Should have thrown an IOException, not an Exception: Part with negative quantity was added.");
		}	
	}
	
	// To test exception chaining from the Part class through the PartsInventoryModel class
	@Test (expected = IOException.class)
	public void testInventoryModel_AddPartWithPartNumberExceedingMaxLength() throws IOException {
		String longPartNumber = "";
		for (int i = 0; i < Part.getMaxPartNumberLength(); i++) {
			longPartNumber = longPartNumber + "A"; // add one letter to the string
		}
		try {
			pim.addPart(quantityUnitType, partName, longPartNumber, vendor);
			fail("Should have thrown an IOException: Part with partNumber exceeding max length was added.");
		}
		catch (IOException e) {
			throw new IOException(e);
		}
		catch (Exception e) {
			fail("Should have thrown an IOException, not an Exception: Part with partNumber exceeding max length was added.");
		}	
	}
	
	@Test (expected = Exception.class)
	public void testInventoryModel_AddPartWithDuplicateName() throws Exception {
		try {
			pim.addPart(quantityUnitType, partName, partNumber, vendor);
			pim.addPart(quantityUnitType, partName, partNumber, vendor);
			fail("Should have thrown an exception: attempted to add Part with duplicate name.");
		}
		catch (IOException e) {
			fail("Should have thrown an Exception, not an IOException: attempted to add Part with duplicate name.");
		}
		catch (Exception e) {
			throw new Exception(e);
		}	
	}
	
	
	@Test
	public void testInventoryModel_AddOneThousandUniqueParts() {
		try {
			for (int i = 0; i < 1000; i++) {
				pim.addPart(quantityUnitType, partName + "_" + i, partNumber + "_" + i, vendor);
			}
			assertTrue(pim.getSize() == 1000);
			assertTrue(pim.findPartName(partName + "_" + 500) != null); // should find a valid Part object
			assertTrue(pim.findPartName("NoSuch" + "_" + 500) == null); // should not find a partName that was not added
		}
		catch (IOException e) {
			fail("IOException thrown during unexceptional part creation: \n\t" + e);
		}
		catch (Exception e) {
			fail("Exception thrown during unexceptional part creation: \n\t" + e);
		}	
	}
	
	@Test
	public void testInventoryModel_DeletePartByObjectReference() {
		try {
			Part p = null;
			pim.addPart(quantityUnitType, partName, partNumber, vendor);
			assertTrue((p = pim.findPartName(partName)) != null); // should find a valid Part object
			pim.deletePart(p);
		}
		catch (IOException e) {
			fail("IOException thrown during unexceptional part creation: \n\t" + e);
		}
		catch (Exception e) {
			fail("Exception thrown during unexceptional part creation: \n\t" + e);
		}	
	}
	
	@Test
	public void testInventoryModel_EditPartWithObjectReference() {
		try {
			Part partOriginal = null;
			pim.addPart(quantityUnitType, partName, partNumber, vendor);
			assertTrue((partOriginal = pim.findPartName(partName)) != null); // should find a valid Part object
			Part partReplace = new Part(42, "Linear Feet", "ThisNewPartName", partNumber, "DifferentVendor");
			pim.editPart(partOriginal, partReplace);
			assertTrue(pim.findPartName(partName) == null); // should not find the old Part name
			assertTrue(pim.findPartName("ThisNewPartName") != null); // should find the new Part name
		}
		catch (IOException e) {
			fail("IOException thrown during unexceptional part creation: \n\t" + e);
		}
		catch (Exception e) {
			fail("Exception thrown during unexceptional part creation: \n\t" + e);
		}	
	}
	*/
}

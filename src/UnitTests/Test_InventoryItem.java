package UnitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import InventoryItems.InventoryItem;

public class Test_InventoryItem {
	Integer itemID;
	Integer partID;
	String location;
	Integer quantity;
	InventoryItem ii;
	
	@Before 
	public void setUp() {	
		partID = 1;
		location = "Facility 2";
		quantity = 3;	
	}
	
	@Test
	public void testInventoryItemCreation_NoID() {
		try {
			ii = new InventoryItem(partID, location, quantity);
			assertTrue(ii.getQuantity().equals(quantity));
			assertTrue(ii.getPartID().equals(partID));
			assertTrue(ii.getLocation().equals(location));
		}
		catch (IOException e) {
			fail("Exception thrown during unexceptional part creation: \n\t" + e);
		}	
	}
	
	// Zero quantity OK in some cases
	@Test (expected = IOException.class)
	public void testInventoryItemCreation_NegativeQuantity() throws IOException {
		Integer badQuantity = -1;
		ii = new InventoryItem(partID, location, badQuantity);
		assertTrue(ii.getQuantity().equals(badQuantity));
		fail("Exception failed to trigger given incorrect quantity \"" + badQuantity + "\"\n\t");
	}
}

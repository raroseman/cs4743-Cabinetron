package assignment2;

/*
 * 
 * CS 4743 assignment 3
 * Written by Josef Klein and Ryan Roseman
 * 
 */

public class Tester {
	private static PartsInventoryView partsInventoryView;
	private static InventoryView inventoryView;
	private static PartsInventoryController partsInventoryController;
	private static InventoryController inventoryController;
	private static PartsInventoryModel partsInventoryModel;
	private static InventoryItemModel inventoryItemModel;
	
	public static void main(String args[]) {
		/*
		DB_CreateTables c = new DB_CreateTables();
		c.Setup();
		DB_InsertTableData i = new DB_InsertTableData();
		i.Setup();
		*/
		partsInventoryModel = new PartsInventoryModel();
	
		partsInventoryView = new PartsInventoryView(partsInventoryModel);
		
		inventoryItemModel = new InventoryItemModel();
		
		inventoryView = new InventoryView(partsInventoryModel, inventoryItemModel);
		
		partsInventoryController = new PartsInventoryController(partsInventoryModel, partsInventoryView);
		
		inventoryController = new InventoryController(inventoryItemModel, inventoryView);
			
		partsInventoryView.register(partsInventoryController);	
		
		inventoryView.register(inventoryController);
		
	}
}

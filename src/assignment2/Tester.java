package assignment2;

import assignment2.DB_CreateTables;
import assignment2.DB_InsertTableData;

/*
 * 
 * CS 4743 assignment 2
 * Written by Josef Klein and Ryan Roseman
 * 
 */

public class Tester {
	private static PartsInventoryView partsInventoryView;
	private static PartsInventoryController partsInventoryController;
	private static PartsInventoryModel partsInventoryModel;
	
	public static void main(String args[]) {
		
		DB_CreateTables c = new DB_CreateTables();
		c.Setup();
		DB_InsertTableData i = new DB_InsertTableData();
		i.Setup();
		
		partsInventoryModel = new PartsInventoryModel();
	
		partsInventoryView = new PartsInventoryView(partsInventoryModel);
		
		partsInventoryController = new PartsInventoryController(partsInventoryModel, partsInventoryView);
			
		partsInventoryView.register(partsInventoryController);
		
		
	}
}

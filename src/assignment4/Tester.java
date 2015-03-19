package assignment4;

import Database.*;
import InventoryItems.InventoryController;
import InventoryItems.InventoryItemModel;
import InventoryItems.InventoryView;
import Parts.PartsInventoryController;
import Parts.PartsInventoryModel;
import Parts.PartsInventoryView;
import ProductTemplates.ProductTemplateListController;
import ProductTemplates.ProductTemplateListView;
import ProductTemplates.ProductTemplateModel;

/*
 * 
 * CS 4743 assignment 3
 * Written by Josef Klein and Ryan Roseman
 * 
 */

public class Tester {
	private static PartsInventoryView partsInventoryView;
	private static InventoryView inventoryView;
	private static ProductTemplateListView productTemplateListView;
	private static PartsInventoryController partsInventoryController;
	private static InventoryController inventoryController;
	private static ProductTemplateListController productTemplateListController;

	private static PartsInventoryModel partsInventoryModel;
	private static InventoryItemModel inventoryItemModel;
	private static ProductTemplateModel productTemplateModel;
	
	public static void main(String args[]) {
		
		DB_CreateTables c = new DB_CreateTables();
		c.Setup();
		DB_InsertTableData i = new DB_InsertTableData();
		i.Setup();
		
	
		partsInventoryModel = new PartsInventoryModel();
	
		partsInventoryView = new PartsInventoryView(partsInventoryModel);
		
		inventoryItemModel = new InventoryItemModel();
		
		inventoryView = new InventoryView(partsInventoryModel, inventoryItemModel);
		
		productTemplateModel = new ProductTemplateModel();
		
		productTemplateListView = new ProductTemplateListView(productTemplateModel);
		
		partsInventoryController = new PartsInventoryController(partsInventoryModel, partsInventoryView);
		
		inventoryController = new InventoryController(inventoryItemModel, inventoryView);
		
		productTemplateListController = new ProductTemplateListController(productTemplateModel, productTemplateListView);
			
		partsInventoryView.register(partsInventoryController);	
		
		inventoryView.register(inventoryController);
		
		productTemplateListView.register(productTemplateListController);

	}
}

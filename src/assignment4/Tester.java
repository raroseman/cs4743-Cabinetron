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
 * CS 4743 assignment 5
 * Written by Josef Klein and Ryan Roseman
 * 
 */

public class Tester {
	
	public static void main(String args[]) {
		CabinetronModel model = new CabinetronModel();
		CabinetronView view = new CabinetronView(model);
		CabinetronController controller = new CabinetronController(model, view);
		view.register(controller);
		
		/*
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
		*/
	}
}

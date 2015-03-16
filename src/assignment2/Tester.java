package assignment2;

import java.io.IOException;
import java.sql.SQLException;

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
	
	
	private static ProductTemplatePartModel templatePartModel;
	private static ProductTemplateModel ptm;
	
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
		
		productTemplateModel = new ProductTemplateModel();
		
		productTemplateListView = new ProductTemplateListView(productTemplateModel);
		
		partsInventoryController = new PartsInventoryController(partsInventoryModel, partsInventoryView);
		
		inventoryController = new InventoryController(inventoryItemModel, inventoryView);
		
		productTemplateListController = new ProductTemplateListController(productTemplateModel, productTemplateListView);
			
		partsInventoryView.register(partsInventoryController);	
		
		inventoryView.register(inventoryController);
		
		productTemplateListView.register(productTemplateListController);
		/*
		templatePartModel = new ProductTemplatePartModel(5);

		ptm = new ProductTemplateModel();
		
		for (ProductTemplate template : ptm.getProductTemplates()) {
			System.out.println(template.getID() + " | " + template.getProductNumber() + " | " + template.getDescription() + " | " + template.getTimestamp());
		}
		*/
		
		/* // add test
		
		try {
			ProductTemplate newTemp = new ProductTemplate("AProductTest", "A test product.");
			ptm.addProductTemplate(newTemp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (ProductTemplate template : ptm.getProductTemplates()) {
			System.out.println(template.getID() + " | " + template.getProductNumber() + " | " + template.getDescription() + " | " + template.getTimestamp());
		}
		
		*/
		
		/*  // delete test
		
		try {
			ptm.deleteProductTemplate(3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (ProductTemplate template : ptm.getProductTemplates()) {
			System.out.println(template.getID() + " | " + template.getProductNumber() + " | " + template.getDescription() + " | " + template.getTimestamp());
		}
		
		*/
		
		/*  // edit test
		ProductTemplate oldTemp = ptm.getProductTemplate(1);
		ProductTemplate newTemp;
		try {
			newTemp = new ProductTemplate(oldTemp.getID(), "AProductNEWNUMBER", oldTemp.getDescription(), oldTemp.getTimestamp());
			ptm.editProductTemplate(oldTemp, newTemp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ProductTemplate template : ptm.getProductTemplates()) {
			System.out.println(template.getID() + " | " + template.getProductNumber() + " | " + template.getDescription() + " | " + template.getTimestamp());
		}
		*/
		
		/*  //add test
		for (ProductTemplatePart ptp : templatePartModel.getProductTemplateParts()) {
			System.out.println(ptp.getPartID() + " | " + ptp.getProductTemplateID() + " | " + ptp.getQuantity());
		}
		 
		ProductTemplatePart newPTP = null;
		try {
			newPTP = new ProductTemplatePart(5, 5, 5);
			templatePartModel.addProductTemplatePart(newPTP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*  // delete test
		try {
			templatePartModel.deleteProductTemplatePart(5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		for (ProductTemplatePart ptp : templatePartModel.getProductTemplateParts()) {
			System.out.println(ptp.getPartID() + " | " + ptp.getProductTemplateID() + " | " + ptp.getQuantity());
		}
		*/

		/*   // edit test
		
		ProductTemplatePart editedPTP;
		try {
			newPTP = templatePartModel.getProductTemplatePart(5); // templateID is 5, partID is 5
			editedPTP = new ProductTemplatePart(5, 3, 3);
			templatePartModel.editProductTemplatePart(newPTP, editedPTP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (ProductTemplatePart ptp : templatePartModel.getProductTemplateParts()) {
			System.out.println(ptp.getPartID() + " | " + ptp.getProductTemplateID() + " | " + ptp.getQuantity());
		}
		
		*/
		
		
	}
}

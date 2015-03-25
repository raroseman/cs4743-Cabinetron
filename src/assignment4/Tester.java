package assignment4;

import AccessControl.Authenticator;
import AccessControl.Session;
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
	
	private static Authenticator authenticator;
	private static Session session;
	public static void main(String args[]) {
		CabinetronModel model = new CabinetronModel();
		CabinetronView view = new CabinetronView(model);
		CabinetronController controller = new CabinetronController(model, view);
		view.register(controller);
				
		authenticator = new Authenticator();
		
		session = authenticator.getUserCredentials("TomJones@Cabinetron.com", "TommyJ$$$");
		
		System.out.println(session);

	}
}

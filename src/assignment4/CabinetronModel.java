package assignment4;

import java.awt.Toolkit;

import InventoryItems.InventoryController;
import InventoryItems.InventoryItemModel;
import InventoryItems.InventoryView;
import Parts.PartsInventoryController;
import Parts.PartsInventoryModel;
import Parts.PartsInventoryView;
import ProductTemplates.ProductTemplateListController;
import ProductTemplates.ProductTemplateListView;
import ProductTemplates.ProductTemplateModel;

public class CabinetronModel {

	private PartsInventoryModel partsInventoryModel;
	private InventoryItemModel inventoryItemModel;
	private ProductTemplateModel productTemplateModel;
	
	private int GUIWidth;
	private int GUIHeight;
	
	public CabinetronModel() {
		partsInventoryModel = new PartsInventoryModel();
		GUIWidth = (Toolkit.getDefaultToolkit().getScreenSize().width / 3) * 2;
		GUIHeight = Toolkit.getDefaultToolkit().getScreenSize().height - 50;
//		partsInventoryView = new PartsInventoryView(partsInventoryModel);
		
		//inventoryItemModel = new InventoryItemModel();
//		inventoryView = new InventoryView(partsInventoryModel, inventoryItemModel);
		
		//productTemplateModel = new ProductTemplateModel();
//		productTemplateListView = new ProductTemplateListView(productTemplateModel);
		
		//inventoryController = new InventoryController(inventoryItemModel, inventoryView);
		//productTemplateListController = new ProductTemplateListController(productTemplateModel, productTemplateListView);
			
		}
	
	public PartsInventoryModel GetPartsModel() {
		return partsInventoryModel;
	}
	
	public InventoryItemModel GetInventoryItemModel() {
		return inventoryItemModel;
	}
	
	public ProductTemplateModel GetProductTemplateModel() {
		return productTemplateModel;
	}
	
	public int GetGUIWidth() {
		return GUIWidth;
	}
	
	public int GetGUIHeight() {
		return GUIHeight;
	}
}

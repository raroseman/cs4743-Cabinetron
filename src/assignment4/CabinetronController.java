package assignment4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import InventoryItems.InventoryController;
import Parts.PartsInventoryController;
import Parts.PartsInventoryView;
import ProductTemplates.ProductTemplateListController;

public class CabinetronController implements ActionListener {
	
	private PartsInventoryController partsInventoryController;
	private InventoryController inventoryController;
	private ProductTemplateListController productTemplateListController;

	private CabinetronModel model;
	private CabinetronView view;
	
	public CabinetronController(CabinetronModel model, CabinetronView view) {
		this.model = model;
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		switch(command) {
			case "Part":
				PartsInventoryView partsView = view.ViewParts();
				partsInventoryController = new PartsInventoryController(model.GetPartsModel(), partsView, view);
				partsView.register(partsInventoryController);
				break;
		}
	}
	
	public PartsInventoryController GetPartsInventoryController() {
		return partsInventoryController;
	}
	
	public InventoryController GetInventoryItemController() {
		return inventoryController;
	}
	
	public ProductTemplateListController GetProductTemplateController() {
		return productTemplateListController;
	}
}

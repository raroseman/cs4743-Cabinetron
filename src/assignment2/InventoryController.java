package assignment2;

public class InventoryController {
	private InventoryItemModel inventoryItemModel;
	private InventoryView inventoryView;
	private PartView partView;
	private Part selectedPart = null;
	private boolean hasPartViewOpen;
	
	public InventoryController(InventoryItemModel inventoryItemModel, InventoryView inventoryView) {
		this.inventoryItemModel = inventoryItemModel;
		this.inventoryView = inventoryView;
		hasPartViewOpen = false;
	}
}

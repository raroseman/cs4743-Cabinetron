package AccessControl;

public class Session {
	private boolean canViewProductTemplates;
	private boolean canAddProductTemplates;
	private boolean canDeleteProductTemplates;
	private boolean canCreateProducts;
	private boolean canViewInventory;
	private boolean canAddInventory;
	private boolean canViewParts;
	private boolean canAddParts;
	private boolean canDeleteParts;
	private boolean canDeleteInventory;
	
	public Session(Administrator admin) {
		canViewProductTemplates = true;
		canAddProductTemplates = true;
		canDeleteProductTemplates = true;
		canCreateProducts = true;
		canViewInventory = true;
		canAddInventory = true;
		canViewParts = true;
		canAddParts = true;
		canDeleteParts = true;
		canDeleteInventory = true;
	}
	
	public Session(ProductionManager productManger) {
		canViewProductTemplates = true;
		canAddProductTemplates = true;
		canDeleteProductTemplates = true;
		canCreateProducts = true;
		canViewInventory = true;
		canAddInventory = false;
		canViewParts = true;
		canAddParts = false;
		canDeleteParts = false;
		canDeleteInventory = false;
	}
	
	public Session(InventoryManager inventoryManager) {
		canViewProductTemplates = false;
		canAddProductTemplates = false;
		canDeleteProductTemplates = false;
		canCreateProducts = false;
		canViewInventory = true;
		canAddInventory = true;
		canViewParts = true;
		canAddParts = true;
		canDeleteParts = false;
		canDeleteInventory = false;
	}
	
	// Implement method to disable functions for each constructor
	// Need the menu first
	public void disablePermissions() {
		if (!canViewProductTemplates) { }
	}
}

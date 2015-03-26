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
	private String sessionUser;
	
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
		sessionUser = admin.getUserName() + ": Administrator";
	}
	
	public Session(ProductionManager productManager) {
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
		sessionUser = productManager.getUserName() + ": Production Manager";
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
		sessionUser = inventoryManager.getUserName() + ": Inventory Manager";
	}
	
	public String getUserName() {
		return sessionUser;
	}
	
	// Implement method to disable functions for each constructor
	// Need the menu first
	public void disablePermissions() {
		if (!canViewProductTemplates) { }
	}
	
}

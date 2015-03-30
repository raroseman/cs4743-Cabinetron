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
	private boolean [] accessPrivileges;
	
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
		accessPrivileges = new boolean[] {true, true, true, true, true, true, true, true, true, true};
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
		accessPrivileges = new boolean[] {true, true, true, true, true, false, true, false, false, false};
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
		accessPrivileges = new boolean[] {false, false, false, false, true, true, true, true, false, false};
	}
	
	public String getUserName() {
		return sessionUser;
	}
	
	public boolean[] getAccessPrivileges() {
		return accessPrivileges;
	}	
}

package assignment2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InventoryController implements ActionListener, ListSelectionListener {
	private InventoryItemModel inventoryItemModel;
	private InventoryView inventoryView;
	private ItemView itemView;
	private Part selectedPart = null;
	private boolean hasPartViewOpen;
	
	public InventoryController(InventoryItemModel inventoryItemModel, InventoryView inventoryView) {
		this.inventoryItemModel = inventoryItemModel;
		this.inventoryView = inventoryView;
		hasPartViewOpen = false;
	}
	
	public void actionPerformed(ActionEvent e) throws NumberFormatException {
		String command = e.getActionCommand();
		
		e.paramString();
		
		switch(command) {
			case "Add": 
				inventoryView.clearErrorMessage();
				if (hasPartViewOpen) {
					itemView.dispose();
				}
				ClearSelection();
				itemView = new ItemView(inventoryItemModel, "Add New Item");
				itemView.register(this);
				//itemView.disableIDEdit();
				//itemView.hideSaveButton();
				//itemView.hideEditButton();
				hasPartViewOpen = true;
				System.out.println("test");
				break;
		}
	}

	private void ClearSelection() {
		selectedPart = null;
		inventoryView.disableDelete();
		inventoryView.disableView();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

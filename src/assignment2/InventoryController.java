package assignment2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InventoryController implements ActionListener, ListSelectionListener, WindowFocusListener {
	private InventoryItemModel inventoryItemModel;
	private InventoryView inventoryView;
	private ItemView itemView;
	private InventoryItem selectedItem = null;
	private boolean hasItemViewOpen;
	
	public InventoryController(InventoryItemModel inventoryItemModel, InventoryView inventoryView) {
		this.inventoryItemModel = inventoryItemModel;
		this.inventoryView = inventoryView;
		hasItemViewOpen = false;
	}
	
	public void actionPerformed(ActionEvent e) throws NumberFormatException {
		String command = e.getActionCommand();
		
		e.paramString();
		
		switch(command) {
			case "Add": 
				inventoryView.clearErrorMessage();
				if (hasItemViewOpen) {
					itemView.dispose();
				}
				ClearSelection();
				itemView = new ItemView(inventoryItemModel, "Add New Item");
				itemView.register(this);
				itemView.disableIDEdit();
				itemView.hideSaveButton();
				itemView.hideEditButton();
				hasItemViewOpen = true;
				break;
			case "Delete":
				inventoryView.clearErrorMessage();
				if (selectedItem != null) {
					if (hasItemViewOpen) {
						itemView.dispose();
						hasItemViewOpen = false;
					}
					try {
						inventoryItemModel.deletePart(selectedItem);
					} 
					catch (SQLException sqe) {
						inventoryView.setErrorMessage(sqe.getMessage());
					//	System.out.println(sqe.getMessage());
					}
					catch (IOException ioe) {
						inventoryView.setErrorMessage(ioe.getMessage());
					//	System.out.println(ioe.getMessage());
					}
					ClearSelection();
					inventoryView.updatePanel();
					inventoryView.repaint();
				}
				break;
			case "View":
				inventoryView.clearErrorMessage();
				if (hasItemViewOpen) {
					itemView.dispose();
				}
				if (selectedItem != null) {
					inventoryView.disableDelete();
					inventoryView.disableView();
					itemView = new ItemView(inventoryItemModel, "View/Edit Item: " + selectedItem.getID());
					itemView.register(this);
					itemView.disableEditable();
					itemView.setPartNumberByID(selectedItem.getPartID());
					itemView.setID(selectedItem.getID());
					itemView.setQuantity(selectedItem.getQuantity());
					itemView.setLocationType(selectedItem.getLocation());
					hasItemViewOpen = true;
				}
				break;	
			case "Edit":
				itemView.enableEditable();
				itemView.hideEditButton();
				itemView.repaint();
				break;
			case "Save":
				if (selectedItem != null) {
					try {
						InventoryItem newItem = new InventoryItem(itemView.getPartID(), itemView.getLocationName(), itemView.getQuantity());
						inventoryItemModel.editInventoryItem(selectedItem, newItem);
						itemView.dispose();
						inventoryView.updatePanel();
						inventoryView.repaint();
					} catch (NumberFormatException noint) {
						itemView.setErrorMessage(noint.getMessage());
					} catch (SQLException sqe) {
						itemView.setErrorMessage(sqe.getMessage());
					} catch (IOException ex) {
						itemView.setErrorMessage(ex.getMessage());
					} catch (Exception e1) {
						itemView.setErrorMessage(e1.getMessage());
					} 				
				}	
				break;
			case "OK":
				try {
					InventoryItem newItem = new InventoryItem(itemView.getPartID(), itemView.getLocationName(), itemView.getQuantity());			
					inventoryItemModel.addInventoryItem(newItem);
					itemView.dispose();
					inventoryView.updatePanel();
					inventoryView.repaint();
				}
				catch (NumberFormatException noint) {
					itemView.setErrorMessage(noint.getMessage());
				}
				catch (IOException ioex) {
					itemView.setErrorMessage(ioex.getMessage());
				}
				catch (Exception ex) {
					itemView.setErrorMessage(ex.getMessage());
				}
				break;
			case "Cancel":
				itemView.dispose();
				break;
		}
	}

	private void ClearSelection() {
		selectedItem = null;
		inventoryView.disableDelete();
		inventoryView.disableView();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
			return;
		}
		else {
			int selectedRow = lsm.getMinSelectionIndex();	
			selectedItem = inventoryView.getObjectInRow(selectedRow);
			inventoryView.clearErrorMessage();
			inventoryView.enableDelete();
			inventoryView.enableView();
		}
		
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		System.out.println("Gained focus");
		inventoryView.updatePanel();
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

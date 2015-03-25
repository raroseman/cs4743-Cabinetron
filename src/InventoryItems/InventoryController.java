package InventoryItems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import assignment4.CabinetronView;

public class InventoryController implements ActionListener, ListSelectionListener {
	private InventoryItemModel inventoryItemModel;
	private InventoryView inventoryView;
	private ItemView itemView;
	private InventoryItem selectedItem = null;
	private InventoryItem oldDatabaseItem = null;
	private InventoryItem userModifiedItem = null;
	private InventoryItem newDatabaseItem = null;
	private CabinetronView view = null;
	private int selectedRow = 0;
	private boolean hasItemViewOpen;
	
	public InventoryController(InventoryItemModel inventoryItemModel, InventoryView inventoryView, CabinetronView view) {
		this.inventoryItemModel = inventoryItemModel;
		this.inventoryView = inventoryView;
		this.view = view;
		hasItemViewOpen = false;
	}
	
	public void actionPerformed(ActionEvent e) throws NumberFormatException {
		String command = e.getActionCommand();
		
		e.paramString();
		
		switch(command) {
			case "Add": 
				inventoryView.clearErrorMessage();
				if (hasItemViewOpen) {
					view.closeInventoryItemDetailView();
				}
				clearSelection();
				itemView = view.ViewInventoryItemDetails("Add Inventory Item");
				itemView.register(this);
				itemView.hideSaveButton();
				itemView.hideEditButton();
				itemView.hideID();
				hasItemViewOpen = true;
				inventoryView.updatePanel();
				inventoryView.repaint();
				break;
			case "Delete":
				inventoryView.clearErrorMessage();
				if (selectedItem != null) {
					if (hasItemViewOpen) {
						if (selectedItem.getID() == itemView.getID()) {
							view.closePartDetailView();
							hasItemViewOpen = false;
						}
					}
					try {
						inventoryItemModel.deletePart(selectedItem);
						clearSelection();
						inventoryView.updatePanel();
						inventoryView.repaint();
					} 
					catch (SQLException sqe) {
						inventoryView.setErrorMessage(sqe.getMessage());
					}
					catch (IOException ioe) {
						inventoryView.setErrorMessage(ioe.getMessage());
					}
					
				}
				break;
			case "View":
				inventoryView.clearErrorMessage();
				if (hasItemViewOpen) {
					view.closeInventoryItemDetailView();
				}
				if (selectedItem != null) {
					inventoryView.disableDelete();
					inventoryView.disableView();
					itemView = view.ViewInventoryItemDetails("View/Edit Inventory Item");
					itemView.register(this);
					itemView.disableEditable();
					itemView.setPartNumber(selectedItem.getPart().getPartNumber());
					itemView.setID(selectedItem.getID());
					itemView.setQuantity(selectedItem.getQuantity());
					itemView.setLocationType(selectedItem.getLocation());
					hasItemViewOpen = true;
				}
				break;	
			case "Edit":
				oldDatabaseItem = selectedItem;
				if (itemView == null) {
					System.out.println("INVENTORYCONTROLLER LINE 101: ITEMVIEW IS NULL");
				}
				itemView.enableEditable();
				itemView.hideEditButton();
				itemView.repaint();
				break;
			case "Save":
			case "Override":
				if (oldDatabaseItem != null) {
					if (newDatabaseItem != null) {
						oldDatabaseItem = newDatabaseItem; // user had an edit conflict already; move "new" to "old"
					}
					try {
						System.out.println("TRYING");
						userModifiedItem = new InventoryItem(itemView.getPartID(), itemView.getLocationName(), itemView.getQuantity());
						// should generate an IOException here if location is Unknown or other parameters are incorrect
						inventoryItemModel.editInventoryItem(oldDatabaseItem, userModifiedItem);
						
						oldDatabaseItem = null;
						userModifiedItem = null;
						newDatabaseItem = null;
						
						view.closeInventoryItemDetailView();
						inventoryView.updatePanel();
						inventoryView.repaint();
					} 
					catch (NumberFormatException noint) {
						itemView.setErrorMessage(noint.getMessage());
					} 
					catch (SQLException sqe) {
						itemView.setErrorMessage(sqe.getMessage());
					} 
					catch (IOException ex) {
						itemView.setErrorMessage(ex.getMessage());
						
//4						// Item was added at the same part at location. Update list view and display changes side-by-side in edit view.
						/*
						try {
							newDatabaseItem = inventoryItemModel.getUpdatedInventoryItem(selectedItem.getID());
						} 
						catch (IOException e1) {
							itemView.setErrorMessage(e1.getMessage());
						} 
						catch (SQLException e1) {
							itemView.setErrorMessage(e1.getMessage());
						}
						
						inventoryView.updatePanel();
						inventoryView.repaint();
						itemView.setErrorMessage(e1.getMessage());
						view.showInventoryItemEditConflictWindow();
						itemView.showEditConflictWindow(oldDatabaseItem, userModifiedItem, newDatabaseItem, view.getWidth());
						*/

					} catch (Exception e1) { // Timestamp discrepancy
						try {
							newDatabaseItem = inventoryItemModel.getUpdatedInventoryItem(selectedItem.getID());
						} 
						catch (IOException ioe) {
							itemView.setErrorMessage(ioe.getMessage());
						} 
						catch (SQLException sqe) {
							itemView.setErrorMessage(sqe.getMessage());
						}
						
						inventoryView.updatePanel();
						inventoryView.repaint();
						itemView.setErrorMessage(e1.getMessage());
						view.showInventoryItemEditConflictWindow();
						itemView.showEditConflictWindow(oldDatabaseItem, userModifiedItem, newDatabaseItem, view.getWidth());
					} 				
				}	
				break;
			case "OK":
				try {
					InventoryItem newItem = new InventoryItem(itemView.getPartID(), itemView.getLocationName(), itemView.getQuantity());			
					inventoryItemModel.addInventoryItem(newItem);
					view.closeInventoryItemDetailView();
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
				view.closeInventoryItemDetailView();
				if (selectedItem != null) {
					inventoryView.enableDelete();
					inventoryView.enableView();
				}
				break;
		}
	}

	private void clearSelection() {
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
			selectedRow = lsm.getMinSelectionIndex();	
			selectedItem = inventoryView.getObjectInRow(selectedRow);
			inventoryView.clearErrorMessage();
			inventoryView.enableDelete();
			inventoryView.enableView();
		}
		
	}
}

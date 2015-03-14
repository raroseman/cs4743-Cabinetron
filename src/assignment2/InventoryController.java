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
	private int selectedRow = 0;
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
				clearSelection();
				itemView = new ItemView(inventoryItemModel, "Add New Item");
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
						itemView.dispose();
						hasItemViewOpen = false;
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
					itemView.dispose();
				}
				if (selectedItem != null) {
					inventoryView.disableDelete();
					inventoryView.disableView();
					itemView = new ItemView(inventoryItemModel, "View/Edit Item: " + selectedItem.getID());
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
				itemView.enableEditable();
				itemView.hideEditButton();
				itemView.repaint();
				break;
			case "Save":
				if (selectedItem != null) {
					InventoryItem oldDatabaseItem = selectedItem;
					InventoryItem userModifiedItem = null;
					InventoryItem newDatabaseItem = null;
					try {
						userModifiedItem = new InventoryItem(itemView.getPartID(), itemView.getLocationName(), itemView.getQuantity());
						inventoryItemModel.editInventoryItem(selectedItem, userModifiedItem);
						itemView.dispose();
						inventoryView.updatePanel();
						inventoryView.repaint();
					} catch (NumberFormatException noint) {
						itemView.setErrorMessage(noint.getMessage());
					} catch (SQLException sqe) {
						itemView.setErrorMessage(sqe.getMessage());
					} catch (IOException ex) {
//4						// Item was added at the same part at location. Update list view and display changes side-by-side in edit view.
						
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
						itemView.showEditConflictWindow(oldDatabaseItem, userModifiedItem, newDatabaseItem);

						System.out.println("INV CTRL LINE 110");
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

	@Override
	public void windowGainedFocus(WindowEvent e) {
		if (selectedItem != null) {
			inventoryView.updatePanel();
			inventoryView.setSelectedRow(selectedRow);
		}	
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		// Nothing
	}
}

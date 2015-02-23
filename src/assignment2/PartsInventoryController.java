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

public class PartsInventoryController implements ActionListener, ListSelectionListener, WindowFocusListener {
	private PartsInventoryModel partsInventoryModel;
	private PartsInventoryView inventoryView;
	private PartView partView;
	private Part selectedPart = null;
	private int selectedRow = 0;
	private boolean hasPartViewOpen;
	
	public PartsInventoryController(PartsInventoryModel inventoryModel, PartsInventoryView inventoryView) {
		this.partsInventoryModel = inventoryModel;
		this.inventoryView = inventoryView;
		hasPartViewOpen = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) throws NumberFormatException {
		String command = e.getActionCommand();
		
		e.paramString();
		
		switch(command) {
			case "Add": 
				inventoryView.clearErrorMessage();
				if (hasPartViewOpen) {
					partView.dispose();
				}
				clearSelection();
				partView = new PartView(partsInventoryModel, "Add New Part");
				partView.register(this);
				partView.disableIDEdit();
				partView.hideSaveButton();
				partView.hideEditButton();
				hasPartViewOpen = true;
				inventoryView.updatePanel();
				inventoryView.repaint();
				break;
			case "Delete":
				inventoryView.clearErrorMessage();
				if (selectedPart != null) {
					if (hasPartViewOpen) {
						partView.dispose();
						hasPartViewOpen = false;
					}
					try {
						partsInventoryModel.deletePart(selectedPart);
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
				if (hasPartViewOpen) {
					partView.dispose();
				}
				if (selectedPart != null) {
					inventoryView.disableDelete();
					inventoryView.disableView();
					partView = new PartView(partsInventoryModel, "View/Edit Part: " + selectedPart.getPartName());
					partView.register(this);
					partView.disableEditable();
					partView.setName(selectedPart.getPartName());
					partView.setID(selectedPart.getID());
					partView.setNumber(selectedPart.getPartNumber());
					partView.setExternalNumber(selectedPart.getExternalPartNumber());
					partView.setVendor(selectedPart.getVendor());
					partView.setQuantityUnitType(selectedPart.getQuantityUnitType());
					hasPartViewOpen = true;
				}
				break;
			case "Edit":
				partView.enableEditable();
				partView.hideEditButton();
				partView.repaint();
				break;
			case "Save":
				if (selectedPart != null) {
					try {
						Part newPart = new Part(partView.getID(), partView.getQuantityUnitType(), partView.getName(), partView.getNumber(), partView.getExternalPartNumber(), partView.getVendor());
						partsInventoryModel.editPart(selectedPart, newPart);
						partView.dispose();
						inventoryView.updatePanel();
						inventoryView.setSelectedRow(selectedRow);
						inventoryView.repaint();
					} catch (NumberFormatException noint) {
						partView.setErrorMessage(noint.getMessage());
					} catch (SQLException sqe) {
						partView.setErrorMessage(sqe.getMessage());
					} catch (IOException ex) {
						partView.setErrorMessage(ex.getMessage());
					} catch (Exception e1) {
						partView.setErrorMessage(e1.getMessage());
					} 				
				}	
				break;
			case "OK":
				try {
					Part part = new Part(partView.getQuantityUnitType(), partView.getName(), partView.getNumber(), partView.getExternalPartNumber(), partView.getVendor());			
					partsInventoryModel.addPart(part);
					partView.dispose();
					inventoryView.updatePanel();
					inventoryView.repaint();
				}
				catch (NumberFormatException noint) {
					partView.setErrorMessage(noint.getMessage());
				}
				catch (IOException ioex) {
					partView.setErrorMessage(ioex.getMessage());
				}
				catch (Exception ex) {
					partView.setErrorMessage(ex.getMessage());
				}
				break;
			case "Cancel":
				inventoryView.enableDelete();
				inventoryView.enableView();
				partView.dispose();
				break;
		}
	}
	
	private void clearSelection() {
		selectedPart = null;
		inventoryView.disableDelete();
		inventoryView.disableView();
	}
	
	// When the user clicks on an element in the inventory list, event is triggered: gets Part from index of list element
	@Override
	public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (lsm.isSelectionEmpty()) {
			return;
		}
		else {
			selectedRow = lsm.getMinSelectionIndex();	
			selectedPart = inventoryView.getObjectInRow(selectedRow);
			inventoryView.clearErrorMessage();
			inventoryView.enableDelete();
			inventoryView.enableView();
		}
	}
	
	@Override
	public void windowGainedFocus(WindowEvent e) {
		if (selectedPart != null) {
			inventoryView.updatePanel();
			inventoryView.setSelectedRow(selectedRow);
		}	
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		if (selectedPart != null) {
			inventoryView.enableDelete();
			inventoryView.enableView();
		}
	}
}
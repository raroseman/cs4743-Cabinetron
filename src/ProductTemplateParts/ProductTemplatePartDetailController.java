package ProductTemplateParts;

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

public class ProductTemplatePartDetailController implements ActionListener, ListSelectionListener {
	private ProductTemplatePartModel productTemplatePartModel;
	private ProductTemplatePartDetailView productTemplatePartDetailView;
	private ProductTemplatePartView productTemplatePartView;
	private ProductTemplatePart selectedTemplatePart = null;
	private CabinetronView view;
	private int selectedRow = 0;
	private boolean hasPartViewOpen;
	
	public ProductTemplatePartDetailController(ProductTemplatePartModel productTemplatePartModel, ProductTemplatePartView productTemplatePartView, CabinetronView view) {
		this.productTemplatePartModel = productTemplatePartModel;
		this.productTemplatePartView = productTemplatePartView;
		this.view = view;
		hasPartViewOpen = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) throws NumberFormatException {
		String command = e.getActionCommand();
		
		e.paramString();
		
		switch(command) {
			case "Add": 
				productTemplatePartView.clearErrorMessage(); 
				if (hasPartViewOpen) {
					view.closeProductTemplatePartDetailView();
					hasPartViewOpen = false;
				}
				clearSelection();
				productTemplatePartDetailView = view.ViewProductTemplatePartDetails("Add Part to Product Template", productTemplatePartView.getID());
				productTemplatePartDetailView.register(this);
				productTemplatePartDetailView.disableTemplateIDEdit();
				productTemplatePartDetailView.hideSaveButton();
				productTemplatePartDetailView.hideEditButton();
				hasPartViewOpen = true;
				productTemplatePartView.updatePanel();
				productTemplatePartView.repaint();
				break;
			case "Delete":
				productTemplatePartView.clearErrorMessage();
				if (selectedTemplatePart != null) {
					if (hasPartViewOpen) {
						view.closeProductTemplatePartDetailView();
						hasPartViewOpen = false;
					}
					try {
						productTemplatePartModel.deleteProductTemplatePart(selectedTemplatePart);
						clearSelection();
						productTemplatePartView.updatePanel();
						productTemplatePartView.repaint();
					} 
					catch (SQLException sqe) {
						productTemplatePartView.setErrorMessage(sqe.getMessage());
					}
					catch (IOException ioe) {
						productTemplatePartView.setErrorMessage(ioe.getMessage());
					}	
				}
				break;
			case "View":
				productTemplatePartView.clearErrorMessage();
				if (hasPartViewOpen) {
					view.closeProductTemplatePartDetailView();
				}
				if (selectedTemplatePart != null) {
					productTemplatePartView.disableDelete();
					productTemplatePartView.disableView();
					productTemplatePartDetailView = view.ViewProductTemplatePartDetails("View/Edit Product Template Part", selectedTemplatePart.getProductTemplateID());
					productTemplatePartDetailView.register(this);
					productTemplatePartDetailView.disableEditable();
					productTemplatePartDetailView.setTemplateID(selectedTemplatePart.getProductTemplateID());
					productTemplatePartDetailView.setPartID(selectedTemplatePart.getPartID());
					productTemplatePartDetailView.setQuantity(selectedTemplatePart.getQuantity());
					hasPartViewOpen = true;
				}
				break;
			case "Edit":
				productTemplatePartDetailView.enableEditable();
				productTemplatePartDetailView.hideEditButton();
				productTemplatePartDetailView.repaint();
				break;
			case "Save":
				if (selectedTemplatePart != null) {
					try {
						ProductTemplatePart newTemplatePart = new ProductTemplatePart(productTemplatePartDetailView.getProductTemplateID(), productTemplatePartDetailView.getPartID(), productTemplatePartDetailView.getQuantity());
						productTemplatePartModel.editProductTemplatePart(selectedTemplatePart, newTemplatePart);
						view.closeProductTemplatePartDetailView();
						productTemplatePartView.updatePanel();
						productTemplatePartView.setSelectedRow(selectedRow);
						productTemplatePartView.repaint();
					} catch (NumberFormatException noint) {
						productTemplatePartDetailView.setErrorMessage(noint.getMessage());
					} catch (SQLException sqe) {
						productTemplatePartDetailView.setErrorMessage(sqe.getMessage());
					} catch (IOException ex) {
						productTemplatePartDetailView.setErrorMessage(ex.getMessage());
					} catch (Exception e1) {
						productTemplatePartDetailView.setErrorMessage(e1.getMessage());
					} 				
				}	
				break;
			case "OK":
				try {
					ProductTemplatePart newTemplatePart = new ProductTemplatePart(productTemplatePartDetailView.getProductTemplateID(), productTemplatePartDetailView.getPartID(), productTemplatePartDetailView.getQuantity());			
					productTemplatePartModel.addProductTemplatePart(newTemplatePart);
					view.closeProductTemplatePartDetailView();
					productTemplatePartView.updatePanel();
					productTemplatePartView.repaint();
				}
				catch (NumberFormatException noint) {
					productTemplatePartDetailView.setErrorMessage(noint.getMessage());
				}
				catch (IOException ioex) {
					productTemplatePartDetailView.setErrorMessage(ioex.getMessage());
				}
				catch (Exception ex) {
					productTemplatePartDetailView.setErrorMessage(ex.getMessage());
				}
				break;
			case "Cancel":
				productTemplatePartView.enableDelete();
				productTemplatePartView.enableView();
				view.closeProductTemplatePartDetailView();
				break;
		}
	}
	
	private void clearSelection() {
		selectedTemplatePart = null;
		productTemplatePartView.disableDelete();
		productTemplatePartView.disableView();
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
			selectedTemplatePart = productTemplatePartView.getObjectInRow(selectedRow);
			productTemplatePartView.clearErrorMessage();
			productTemplatePartView.enableDelete();
			productTemplatePartView.enableView();
		}
	}
}
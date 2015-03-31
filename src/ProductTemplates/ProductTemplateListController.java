package ProductTemplates;

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
import ProductTemplateParts.ProductTemplatePartDetailController;
import ProductTemplateParts.ProductTemplatePartModel;
import ProductTemplateParts.ProductTemplatePartView;

public class ProductTemplateListController implements ActionListener, ListSelectionListener {
	private ProductTemplateModel productTemplateModel;
	private ProductTemplatePartModel productTemplatePartModel;
	private ProductTemplateListView productTemplateListView;
	private ProductTemplateDetailView productTemplateDetailView;
	private ProductTemplatePartView productTemplatePartView;
	private CabinetronView view;
	private ProductTemplatePartDetailController productTemplatePartDetailController;
	private ProductTemplate selectedTemplate = null;
	private int selectedRow = 0;
	private boolean hasPartViewOpen;
	private boolean hasProductTemplatePartViewOpen;
	
	public ProductTemplateListController(ProductTemplateModel productTemplateModel, ProductTemplateListView productTemplateListView, CabinetronView view) {
		this.productTemplateModel = productTemplateModel;
		this.productTemplateListView = productTemplateListView;
		this.view = view;
		hasPartViewOpen = false;
		hasProductTemplatePartViewOpen = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) throws NumberFormatException {
		String command = e.getActionCommand();
		
		e.paramString();
		
		switch(command) {
			case "Parts List":
				if (hasProductTemplatePartViewOpen) {
					view.closeProductTemplatePartListView();
					hasProductTemplatePartViewOpen = false;
				}
				try {
					productTemplateListView.clearErrorMessage(); 
					productTemplatePartModel = selectedTemplate.getProductTemplatePartModel();
					productTemplatePartView = view.ViewProductTemplatePartList(selectedTemplate);
					productTemplatePartDetailController = new ProductTemplatePartDetailController(productTemplatePartModel, productTemplatePartView, view);
					productTemplatePartView.register(productTemplatePartDetailController);
					hasProductTemplatePartViewOpen = true;
				} catch (NullPointerException n) {
						productTemplateListView.setErrorMessage(n.getMessage());
				}
				break;
			case "Add": 
				productTemplateListView.clearErrorMessage(); 
				if (hasPartViewOpen) {
					view.closeProductTemplateDetailView();
				}
				clearSelection();
				productTemplateDetailView = view.ViewProductTemplateDetails("Add New Product Template");
				productTemplateDetailView.register(this);
				productTemplateDetailView.disableIDEdit();
				productTemplateDetailView.hideSaveButton();
				productTemplateDetailView.hideEditButton();
				productTemplateDetailView.hideID();
				hasPartViewOpen = true;
				productTemplateListView.updatePanel();
				productTemplateListView.repaint();
				break;
			case "Delete":
				productTemplateListView.clearErrorMessage();
				if (selectedTemplate != null) {
					if (hasPartViewOpen) {
						view.closeProductTemplateDetailView();
						hasPartViewOpen = false;
					}
					try {
						productTemplateModel.deleteProductTemplate(selectedTemplate);
						clearSelection();
						productTemplateListView.updatePanel();
						productTemplateListView.repaint();
					} 
					catch (SQLException sqe) {
						productTemplateListView.setErrorMessage(sqe.getMessage());
					}
					catch (IOException ioe) {
						productTemplateListView.setErrorMessage(ioe.getMessage());
					}
					
				}
				break;
			case "View":
				productTemplateListView.clearErrorMessage();
				if (hasPartViewOpen) {
					view.closeProductTemplateDetailView();
					hasPartViewOpen = false;
				}
				if (selectedTemplate != null) {
					productTemplateListView.disableDelete();
					productTemplateListView.disableView();
					productTemplateDetailView = view.ViewProductTemplateDetails("View/Edit Product Template");
					productTemplateDetailView.register(this);
					productTemplateDetailView.disableEditable();
					productTemplateDetailView.setID(selectedTemplate.getID());
					productTemplateDetailView.setNumber(selectedTemplate.getProductNumber());
					productTemplateDetailView.setDescription(selectedTemplate.getDescription());
					hasPartViewOpen = true;
				}
				break;
// 5
			case "Create Product":
				productTemplateListView.clearErrorMessage();
				if (selectedTemplate != null) {
					if (hasPartViewOpen) {
						view.closeProductTemplateDetailView();
						hasPartViewOpen = false;
					}
					try {
						productTemplateModel.deleteProductTemplate(selectedTemplate);
						clearSelection();
						productTemplateListView.updatePanel();
						productTemplateListView.repaint();
					} 
					catch (SQLException sqe) {
						productTemplateListView.setErrorMessage(sqe.getMessage());
					}
					catch (IOException ioe) {
						productTemplateListView.setErrorMessage(ioe.getMessage());
					}
					
				}
				break;
			case "Edit":
				productTemplateDetailView.enableEditable();
				productTemplateDetailView.hideEditButton();
				productTemplateDetailView.repaint();
				break;
			case "Save":
				if (selectedTemplate != null) {
					try {
						ProductTemplate newTemplate = new ProductTemplate(productTemplateDetailView.getID(), productTemplateDetailView.getNumber(), productTemplateDetailView.getDescription());
						productTemplateModel.editProductTemplate(selectedTemplate, newTemplate);
						view.closeProductTemplateDetailView();
						hasPartViewOpen = false;
						productTemplateListView.updatePanel();
						productTemplateListView.setSelectedRow(selectedRow);
						productTemplateListView.repaint();
					} catch (NumberFormatException noint) {
						productTemplateDetailView.setErrorMessage(noint.getMessage());
					} catch (SQLException sqe) {
						productTemplateDetailView.setErrorMessage(sqe.getMessage());
					} catch (IOException ex) {
						productTemplateDetailView.setErrorMessage(ex.getMessage());
					} catch (Exception e1) {
						productTemplateDetailView.setErrorMessage(e1.getMessage());
					} 				
				}	
				break;
			case "OK":
				try {
					ProductTemplate newTemplate = new ProductTemplate(productTemplateDetailView.getNumber(), productTemplateDetailView.getDescription());			
					productTemplateModel.addProductTemplate(newTemplate);
					view.closeProductTemplateDetailView();
					hasPartViewOpen = false;
					productTemplateListView.updatePanel();
					productTemplateListView.repaint();
				}
				catch (NumberFormatException noint) {
					productTemplateDetailView.setErrorMessage(noint.getMessage());
				}
				catch (IOException ioex) {
					productTemplateDetailView.setErrorMessage(ioex.getMessage());
				}
				catch (Exception ex) {
					productTemplateDetailView.setErrorMessage(ex.getMessage());
				}
				break;
			case "Cancel":
				productTemplateListView.enableDelete();
				productTemplateListView.enableView();
				productTemplateListView.enableTemplateParts();
				productTemplateListView.enableCreateProduct();
				view.closeProductTemplateDetailView();
				hasPartViewOpen = false;
				break;
		}
	}
	
	private void clearSelection() {
		selectedTemplate = null;
		productTemplateListView.disableDelete();
		productTemplateListView.disableView();
		productTemplateListView.disableTemplateParts();
		productTemplateListView.disableCreateProduct();
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
			selectedTemplate = productTemplateListView.getObjectInRow(selectedRow);
			productTemplateListView.clearErrorMessage();
			productTemplateListView.enableDelete();
			productTemplateListView.enableView();
			productTemplateListView.enableTemplateParts();
			productTemplateListView.enablePartsList();
			productTemplateListView.enableCreateProduct();
		}
	}
}
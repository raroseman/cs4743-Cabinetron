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

import ProductTemplateParts.ProductTemplatePartDetailController;
import ProductTemplateParts.ProductTemplatePartModel;
import ProductTemplateParts.ProductTemplatePartView;

public class ProductTemplateListController implements ActionListener, ListSelectionListener, WindowFocusListener {
	private ProductTemplateModel productTemplateModel;
	private ProductTemplatePartModel productTemplatePartModel;
	private ProductTemplateListView productTemplateListView;
	private ProductTemplateDetailView productTemplateDetailView;
	private ProductTemplatePartView productTemplatePartView;
	private ProductTemplatePartDetailController productTemplatePartDetailController;
	private ProductTemplate selectedTemplate = null;
	private int selectedRow = 0;
	private boolean hasPartViewOpen;
	private boolean hasProductTemplatePartViewOpen;
	
	public ProductTemplateListController(ProductTemplateModel productTemplateModel, ProductTemplateListView productTemplateListView) {
		this.productTemplateModel = productTemplateModel;
		this.productTemplateListView = productTemplateListView;
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
					productTemplatePartView.dispose();
				}
				try {
					productTemplateListView.clearErrorMessage(); 
					productTemplatePartModel = selectedTemplate.getProductTemplatePartModel();
					productTemplatePartView = new ProductTemplatePartView(productTemplatePartModel);
					productTemplatePartDetailController = new ProductTemplatePartDetailController(productTemplatePartModel, productTemplatePartView);
					productTemplatePartView.register(productTemplatePartDetailController);
					hasProductTemplatePartViewOpen = true;
				} catch (NullPointerException n) {
						productTemplateListView.setErrorMessage(n.getMessage());
				}
				break;
			case "Add": 
				productTemplateListView.clearErrorMessage(); 
				if (hasPartViewOpen) {
					productTemplateDetailView.dispose();
				}
				clearSelection();
				productTemplateDetailView = new ProductTemplateDetailView(productTemplateModel, "Add New Template");
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
						productTemplateDetailView.dispose();
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
					productTemplateDetailView.dispose();
				}
				if (selectedTemplate != null) {
					productTemplateListView.disableDelete();
					productTemplateListView.disableView();
					productTemplateDetailView = new ProductTemplateDetailView(productTemplateModel, "View/Edit Template: " + selectedTemplate.getProductNumber());
					productTemplateDetailView.register(this);
					productTemplateDetailView.disableEditable();
					productTemplateDetailView.setID(selectedTemplate.getID());
					productTemplateDetailView.setNumber(selectedTemplate.getProductNumber());
					productTemplateDetailView.setDescription(selectedTemplate.getDescription());
					hasPartViewOpen = true;
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
						productTemplateDetailView.dispose();
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
					productTemplateDetailView.dispose();
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
				productTemplateDetailView.dispose();
				break;
		}
	}
	
	private void clearSelection() {
		selectedTemplate = null;
		productTemplateListView.disableDelete();
		productTemplateListView.disableView();
		productTemplateListView.disableTemplateParts();
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
		}
	}
	
	@Override
	public void windowGainedFocus(WindowEvent e) {
		if (selectedTemplate != null) {
			productTemplateListView.updatePanel();
			productTemplateListView.setSelectedRow(selectedRow);
		}	
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		if (selectedTemplate != null) {
			productTemplateListView.enableDelete();
			productTemplateListView.enableView();
			productTemplateListView.enableTemplateParts();
			productTemplateListView.enablePartsList();
		}
	}
}
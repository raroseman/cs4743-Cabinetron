package ProductTemplateParts;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

// This is a list of parts related to a specific instance of a ProductTemplate (by productTemplateID)
public class ProductTemplatePartModel {
	
	private Integer templateID;

	private List<ProductTemplatePart> productTemplateParts;
	private Comparator<ProductTemplatePart> sortingMode = ProductTemplatePart.IDDescending; // default sort
	private ProductTemplatePartGateway templatePartGateway;
	
	public ProductTemplatePartModel(Integer productTemplateID) {
		this.templateID = productTemplateID;
		templatePartGateway = new ProductTemplatePartGateway();
		productTemplateParts = templatePartGateway.getProductTemplateParts(templateID);
	}
	
	public List<ProductTemplatePart> getProductTemplateParts() {
		return productTemplateParts;
	}
	
	public ProductTemplatePart getProductTemplatePart(Integer templatePartID) {
		for (ProductTemplatePart templatePart : productTemplateParts) { // this is O(n)
			if (templatePart.getPartID().equals(templatePartID)) {
				return templatePart;
			}
		}
		return null;
	}
	
	public Integer getProductTemplateID() {
		return templateID;
	}
	
	// implicitly adds with an association to this product template ID
	public void addProductTemplatePart(ProductTemplatePart ptp) throws Exception {
		if (ptp.getProductTemplateID() != templateID) {
			System.out.println("PTP ID = " + ptp.getProductTemplateID() + " | THIS TEMPLATE ID = " + templateID);
			throw new Exception("INTERNAL ERROR: TEMPLATE ID MISMATCH in ProductTemplatePartModel line 40 (addPTP).");
		}
		try {
			addProductTemplatePart(ptp.getPartID(), ptp.getQuantity());
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	// implicitly adds with an association to this product template ID
	public void addProductTemplatePart(Integer partID, Integer quantity) throws Exception, IOException, SQLException { // all args
		if (quantity <= 0) {
			throw new IOException("Error: Quantity for a new item must be greater than zero.");
		}
		try {
			templatePartGateway.addProductTemplatePart(templateID, partID, quantity);
			productTemplateParts = templatePartGateway.getProductTemplateParts(templateID); // update list of product template parts
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	// implicitly deletes with an association to this product template ID
	public void deleteProductTemplatePart(ProductTemplatePart ptp) throws SQLException, IOException {
		try {
			deleteProductTemplatePart(ptp.getPartID()); // if it exists, first instance (unique, only one entry) is removed. otherwise does nothing
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		} catch (IOException ioe) {
			throw new IOException(ioe.getMessage());
		}
	}
	
	// implicitly deletes with an association to this product template ID
	public void deleteProductTemplatePart(Integer partID) throws SQLException, IOException {
		try {
			templatePartGateway.deleteProductTemplatePart(templateID, partID); // if it exists, first instance (unique, only one entry) is removed. otherwise does nothing
			productTemplateParts = templatePartGateway.getProductTemplateParts(templateID); // update list of product template parts
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		} catch (IOException ioe) {
			throw new IOException(ioe.getMessage());
		}
	}
	
	// implicitly edits with an association to this product template ID
		public void editProductTemplatePart(ProductTemplatePart ptpOld, ProductTemplatePart ptpNew) throws SQLException, IOException {
			try {
				templatePartGateway.editProductTemplatePart(ptpOld.getID(), ptpNew.getPartID(), ptpNew.getQuantity(), ptpOld.getTimestamp());
				productTemplateParts = templatePartGateway.getProductTemplateParts(templateID); // update list of product template parts
			}
			catch (SQLException sqe) {
				throw new SQLException(sqe.getMessage());
			} catch (IOException ioe) {
				throw new IOException(ioe.getMessage());
			}
		}
		
		public void sortByTemplateID() {
			if (sortingMode == ProductTemplatePart.IDDescending) {
				sortingMode = ProductTemplatePart.IDAscending;
			}
			else {
				sortingMode = ProductTemplatePart.IDDescending;
			}
			productTemplateParts.sort(sortingMode);
		}
		
		public void sortByID() {
			if (sortingMode == ProductTemplatePart.IDDescending) {
				sortingMode = ProductTemplatePart.IDAscending;
			}
			else {
				sortingMode = ProductTemplatePart.IDDescending;
			}
			productTemplateParts.sort(sortingMode);
		}
		
		public void sortByPartID() {
			if (sortingMode == ProductTemplatePart.PartIDDescending) {
				sortingMode = ProductTemplatePart.PartIDAscending;
			}
			else {
				sortingMode = ProductTemplatePart.PartIDDescending;
			}
			productTemplateParts.sort(sortingMode);
		}
		
		public void sortByQuantity() {
			if (sortingMode == ProductTemplatePart.QuantityDescending) {
				sortingMode = ProductTemplatePart.QuantityAscending;
			}
			else {
				sortingMode = ProductTemplatePart.QuantityDescending;
			}
			productTemplateParts.sort(sortingMode);
		}
		
		public ProductTemplatePart findItemByID(Integer i) {
			for (ProductTemplatePart templatePart : productTemplateParts) { // this is O(n)
				if (templatePart.getPartID().equals(i)) {
					return templatePart;
				}
			}
			return null;
		}
		public void sortByCurrentSortMethod() {
			productTemplateParts.sort(sortingMode);
		}
}


package assignment2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

// This contains a list of all product templates
public class ProductTemplateModel {

	private List<ProductTemplate> productTemplates;
	private Comparator<ProductTemplate> sortingMode = ProductTemplate.IDDescending; // default sort
	private ProductTemplateGateway templateGateway;
	
	public ProductTemplateModel() {
		templateGateway = new ProductTemplateGateway();
		productTemplates = templateGateway.getProductTemplates();
	}
	
	public List<ProductTemplate> getProductTemplates() {
		return productTemplates;
	}
	
	public ProductTemplate getProductTemplate(Integer templateID) {
		for (ProductTemplate template : productTemplates) { // this is O(n)
			if (template.getID().equals(templateID)) {
				return template;
			}
		}
		return null;
	}
	
	public void addProductTemplate(ProductTemplate pt) throws Exception {
		try {
			addProductTemplate(pt.getProductNumber(), pt.getDescription());
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public void addProductTemplate(String productNumber, String description) throws Exception, IOException, SQLException {
		try {
			templateGateway.addProductTemplate(productNumber, description);
			productTemplates = templateGateway.getProductTemplates(); // update list of product templates
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void deleteProductTemplate(ProductTemplate pt) throws SQLException, IOException {
		try {
			deleteProductTemplate(pt.getID()); // if it exists, first instance (unique, only one entry) is removed. otherwise does nothing
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		} catch (IOException ioe) {
			throw new IOException(ioe.getMessage());
		}
	}
	
	// implicitly deletes with an association to this product template ID
	public void deleteProductTemplate(Integer templateID) throws SQLException, IOException {
		try {
			templateGateway.deleteProductTemplate(templateID); // if it exists, first instance (unique, only one entry) is removed. otherwise does nothing
			productTemplates = templateGateway.getProductTemplates(); // update list of product templates
		}
		catch (SQLException sqe) {
			throw new SQLException(sqe.getMessage());
		} catch (IOException ioe) {
			throw new IOException(ioe.getMessage());
		}
	}
	
	// implicitly edits with an association to this product template ID
		public void editProductTemplate(ProductTemplate ptOld, ProductTemplate ptNew) throws SQLException, IOException {
			try {
				templateGateway.editProductTemplate(ptOld.getID(), ptNew.getProductNumber(), ptNew.getDescription(), ptOld.getTimestamp());
				productTemplates = templateGateway.getProductTemplates(); // update list of product templates
			}
			catch (SQLException sqe) {
				throw new SQLException(sqe.getMessage());
			} catch (IOException ioe) {
				throw new IOException(ioe.getMessage());
			}
		}
}


package assignment2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Communicates with a MySQL database using JDBC
 * to select, insert, update, and delete parts
 * from the ProductTemplate table.
 * 
 * @author Josef Klein
 *
 */
public class ProductTemplateGateway {
	MysqlDataSource ds = new MysqlDataSource();
	Connection conn = null; // the database connection handle
	String URL = "jdbc:mysql://devcloud.fulgentcorp.com:3306/eay250";
	String username = "eay250";
	String password = "HnGe29Bwm7NBLvsgCGX8";
	String SQL;
	Statement stmt; // the statement handle for the connection
	PreparedStatement prepstmt;
	ResultSet rs; // the results of an executed statement
	
	private void createConnection() {
		if (conn == null) {
			if (ds == null) {
				ds = new MysqlDataSource();
			}
			ds.setURL(URL);
			ds.setUser(username);
			ds.setPassword(password);
			// Create a database connection object
			try {
				conn = ds.getConnection();
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closePreparedStatement() {
		try {
			if (prepstmt != null) {
				prepstmt.close();
				prepstmt = null;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void closeStatement() {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void closeResultSet() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Returns the integer values that reference the productTemplateID and partID as Integers
	
	public List<ProductTemplate> getProductTemplates() {
		List<ProductTemplate> productTemplates = new ArrayList<ProductTemplate>();
		createConnection();
		// Select all product template parts related to a specific template ID

		SQL = "SELECT ID, ProductNumber, Description, Timestamp ";
		SQL += "FROM ProductTemplates ";
		
		try {
			stmt = conn.createStatement();
			stmt.execute(SQL);
			rs = stmt.getResultSet();

			while (rs.next()) {
				try {
					ProductTemplate pt = new ProductTemplate(rs.getInt("ID"), rs.getString("ProductNumber"), rs.getString("Description"), rs.getString("Timestamp"));
					productTemplates.add(pt);
				}
				catch (IOException ioe) {
					closeResultSet();
					closeStatement();
					System.out.println(ioe.getMessage());
				}
			}
			closeResultSet();
			closeStatement();

		} catch (SQLException e1) {
			closeResultSet();
			closeStatement();
			e1.printStackTrace();
		}
		closeConnection();
		return productTemplates;
	}
	
	public ProductTemplate getProductTemplate(Integer ID) throws IOException, SQLException { 
		ProductTemplate pt = null;
		// Select a unique product template part
		SQL = "SELECT ID, ProductNumber, Description, Timestamp ";
		SQL += "FROM ProductTemplates ";
		SQL += "WHERE ID=?";
		
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, ID);
			rs = prepstmt.executeQuery();

			if (rs.next()) {
				try {
					pt = new ProductTemplate(rs.getInt("ID"), rs.getString("ProductNumber"), rs.getString("Description"), rs.getString("Timestamp"));
				}
				catch (IOException ioe) {
					closeResultSet();
					closeStatement();
					System.out.println(ioe.getMessage());
				}
			}
			closeResultSet();
			closeStatement();

		} catch (SQLException e1) {
			closeResultSet();
			closeStatement();
			e1.printStackTrace();
		}
		return pt;
	}
	
	public void addProductTemplate(String productNumber, String description) throws SQLException, IOException {
		createConnection();
		
		if (!isProductNumberUnique(productNumber)) {
			closeConnection();
//4			// Potential concurrency issue - template with this product number was added to the Template table but not displayed in list view.
			throw new IOException("Error: The product number is already associated with a Product Template.");
		}
		try {	
			SQL = "INSERT IGNORE INTO ProductTemplates (ProductNumber, Description) ";
			SQL += "VALUES (?, ?)";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, productNumber);
			prepstmt.setString(2, description);
			prepstmt.execute();
		}
		catch (SQLException e) {
			closePreparedStatement();
			conn.rollback();
			closeConnection();
			throw new SQLException(e.getMessage()); // "Duplicate entry..."
		}
		closePreparedStatement();
		conn.commit();
		closeConnection();
	}
	
	public void deleteProductTemplate(Integer templateID) throws SQLException, IOException {
		createConnection();
		deleteAssociatedProductTemplateParts(templateID);
		try {	
			SQL = "DELETE FROM ProductTemplates WHERE ID=?";

			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, templateID);
			prepstmt.execute();
		}
		catch (SQLException e) {
			closePreparedStatement();
			conn.rollback();
			closeConnection();
			throw new SQLException(e.getMessage()); // Failed to delete
		}
		closePreparedStatement();
		conn.commit();
		closeConnection();
	}
	
	private void deleteAssociatedProductTemplateParts(Integer templateID) throws SQLException {
		try {	
			SQL = "DELETE FROM ProductTemplateParts WHERE ProductTemplateID=?";

			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, templateID);
			prepstmt.execute();
		}
		catch (SQLException e) {
			closePreparedStatement();
			closeConnection();
			throw new SQLException(e.getMessage()); // Failed to delete
		}
		closePreparedStatement();
	}
	
	public void editProductTemplate(Integer ID, String productNumber, String description, String prevTimestamp) throws SQLException, IOException {
		createConnection();
		
		ProductTemplate pt = null;

		try {
			pt = getProductTemplate(ID);
		} 
		catch (IOException e1) {
			closeConnection();
			throw new IOException(e1.getMessage());
		}
		catch (SQLException sqe) {
			closeConnection();
			throw new SQLException(sqe.getMessage());
		}
/* Edit conflict check - not implemented for ProductTemplateParts in assignment 4
		if (!checkTimestamp(ptp.getID(), prevTimestamp)) {
			closeConnection();
			throw new IOException("Error: This ProductTemplatePart was recently changed. You may wish to review these changes before submitting your own.");
//4
		}
*/
		if (!(pt.getProductNumber().equals(productNumber))) { // part ID changed	
			if (!isProductNumberUnique(productNumber)) {
				closeConnection();
				throw new IOException("Error: That product number is already associated with another product template.");
			}
		}
		try {
			SQL = "UPDATE ProductTemplates SET ProductNumber=?, Description=? WHERE ID=?";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, productNumber);
			prepstmt.setString(2, description);
			prepstmt.setInt(3, ID);
			prepstmt.execute();
		}
		catch (SQLException e) {
			closePreparedStatement();
			conn.rollback();
			closeConnection();
			throw new SQLException(e.getMessage()); // "Duplicate entry..."
		}
		closePreparedStatement();
		conn.commit();
		closeConnection();
	}
	
	private boolean isProductNumberUnique(String productNumber) throws SQLException {
		SQL = "SELECT ProductNumber FROM ProductTemplates WHERE ProductNumber=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, productNumber);
			rs = prepstmt.executeQuery();
		
			if (rs.next()) {
				closeResultSet();
				closePreparedStatement();
				return false; // not unique
			}
			else {
				closeResultSet();
				closePreparedStatement();
				return true;
			}
		} catch (SQLException e1) {
			closeResultSet();
			closePreparedStatement();
			throw new SQLException("Internal Error: Failed to query the given Product Number.");
		}
	}
	
	public boolean checkTimestamp(Integer ID, String prevTimestamp) throws SQLException, IOException {
		String currTimestamp = null;
		
		SQL = "SELECT Timestamp FROM InventoryItems ";
		SQL += "WHERE InventoryItems.ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, ID);
			rs = prepstmt.executeQuery();

			if (rs.next()) {
				currTimestamp = rs.getString("Timestamp");
			}
			else {
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				throw new SQLException("Error: No InventoryItem found with the given ID.");
			}
		} catch (SQLException e1) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new SQLException(e1.getMessage());
		}
		closeResultSet();
		closePreparedStatement();

		if (prevTimestamp.equals(currTimestamp)) {
			return true; // matching time stamps
		}
		else {
			return false; // time stamps do not match - concurrency issue
		}
	}
}



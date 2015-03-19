package ProductTemplateParts;

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
 * from the ProductTemplateParts table.
 * 
 * @author Josef Klein
 *
 */
public class ProductTemplatePartGateway {
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
	
	public List<ProductTemplatePart> getProductTemplateParts(Integer productTemplateID) {
		List<ProductTemplatePart> productTemplateParts = new ArrayList<ProductTemplatePart>();
		createConnection();
		// Select all product template parts related to a specific template ID

		SQL = "SELECT ID, ProductTemplateID, PartID, Quantity, Timestamp ";
		SQL += "FROM ProductTemplateParts ";
		SQL += "WHERE ProductTemplateParts.ProductTemplateID=?";
		
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, productTemplateID);
			rs = prepstmt.executeQuery();

			while (rs.next()) {
				try {
				ProductTemplatePart ptp = new ProductTemplatePart(rs.getInt("ID"), rs.getInt("ProductTemplateID"), rs.getInt("PartID"), rs.getInt("Quantity"), rs.getString("Timestamp"));
				productTemplateParts.add(ptp);
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
		return productTemplateParts;
	}
	
	public ProductTemplatePart getProductTemplatePart(Integer ID) throws IOException, SQLException { 
		ProductTemplatePart ptp = null;
		// Select a unique product template part
		SQL = "SELECT ID, ProductTemplateID, PartID, Quantity, Timestamp ";
		SQL += "FROM ProductTemplateParts ";
		SQL += "WHERE ID=?";
		
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, ID);
			rs = prepstmt.executeQuery();

			if (rs.next()) {
				try {
					ptp = new ProductTemplatePart(rs.getInt("ID"), rs.getInt("ProductTemplateID"), rs.getInt("PartID"), rs.getInt("Quantity"), rs.getString("Timestamp"));
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
		return ptp;
	}
	
	public ProductTemplatePart getProductTemplatePart(Integer productTemplateID, Integer partID) throws IOException, SQLException { 
		ProductTemplatePart ptp = null;
		createConnection();
		// Select a unique product template part
		SQL = "SELECT ID, ProductTemplateID, PartID, Quantity, Timestamp ";
		SQL += "FROM ProductTemplateParts ";
		SQL += "WHERE ProductTemplateID=? AND PartID=?";
		
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, productTemplateID);
			prepstmt.setInt(2, partID);
			rs = prepstmt.executeQuery();

			if (rs.next()) {
				try {
					ptp = new ProductTemplatePart(rs.getInt("ID"), rs.getInt("ProductTemplateID"), rs.getInt("PartID"), rs.getInt("Quantity"), rs.getString("Timestamp"));
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
		return ptp;
	}
	
	public void addProductTemplatePart(Integer templateID, Integer partID, Integer quantity) throws SQLException, IOException {
		createConnection();
		
		if (!isPartUniqueToTemplate(partID, templateID)) {
			closeConnection();
//4			// Potential concurrency issue - part ID was added to this template but was not displayed in list view.
			throw new IOException("Error: The Part ID is already associated with that Product Template.");
		}
		try {	
			SQL = "INSERT IGNORE INTO ProductTemplateParts (ProductTemplateID, PartID, Quantity) ";
			SQL += "VALUES (?, ?, ?)";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, templateID);
			prepstmt.setInt(2, partID);
			prepstmt.setInt(3, quantity);
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
	
	public void deleteProductTemplatePart(Integer templateID, Integer partID) throws SQLException, IOException {
		createConnection();
		
		try {	
			SQL = "DELETE FROM ProductTemplateParts WHERE ProductTemplateID=? AND PartID=?";

			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, templateID);
			prepstmt.setInt(2, partID);
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
	
	public void editProductTemplatePart(Integer ID, Integer newPartID, Integer quantity, String prevTimestamp) throws SQLException, IOException {
		createConnection();
		
		ProductTemplatePart ptp = null;

		try {
			ptp = getProductTemplatePart(ID);
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
		if (!(ptp.getPartID().equals(newPartID))) { // part ID changed	
			if (!isPart(newPartID)) {
				closeConnection();
				throw new IOException("Error: Part ID does not match any Part in database.");
			}
			if (!isPartUniqueToTemplate(newPartID, ptp.getProductTemplateID())) {
				closeConnection();
				throw new IOException("Error: The Part ID is already associated with that product template.");
			}
		}
		try {
			SQL = "UPDATE ProductTemplateParts SET PartID=?, Quantity=? WHERE ID=?";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, newPartID);
			prepstmt.setInt(2, quantity);
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
	
	private boolean isPartUniqueToTemplate(Integer partID, Integer templateID) throws SQLException {
		SQL = "SELECT PartID, ProductTemplateID FROM ProductTemplateParts ";
		SQL += "WHERE PartID=? AND ProductTemplateID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.setInt(2, templateID);
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
			throw new SQLException("Internal Error: Failed to query the given ProductTemplatePart.");
		}
	}
	
	private boolean isPart(Integer partID) throws SQLException, IOException {
		SQL = "SELECT Parts.ID FROM Parts ";
		SQL += "WHERE Parts.ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			rs = prepstmt.executeQuery();
			if (rs.next()) {
				closeResultSet();
				closePreparedStatement();
				return true;
			}
			else {
				closeResultSet();
				closePreparedStatement();
				return false;
			}
		} catch (SQLException e1) {
			closeResultSet();
			closePreparedStatement();
			throw new SQLException("Internal Error: Failed to query the part ID of the given product template part ID.");
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



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
	boolean warnUser = false;
	String warnedAboutName;
	
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
	
	/**
	 * addPart() will add the given parameters
	 * This is assuming the Table Data Gateway model; that is, no domain logic will be
	 * incorporated within to check for things like string length.
	 * 
	 * @param partName - a warning is return if this is exists in inventory; a successive attempt will be successful
	 * @param partNumber - unique
	 * @param vendor - optional
	 * @param unitID - an integer reference to a string as listed in the database
	 * @param externalPartNumber - optional
	 * @throws SQLException
	 */
	
	public void addPart(String partName, String partNumber, String vendor, String quantityUnitType, String externalPartNumber) throws SQLException {
		createConnection();
		
		if (isPartNumberInUse(partNumber)) {
			closeConnection();
			throw new SQLException("Error: Part # already exists.");
		}
		
		if (isPartNameInUse(partName)) {
			if (warnUser && warnedAboutName.equals(partName)) {
				// do nothing; user has already accepted this as a duplicate name
			}
			else {
				warnUser = true;
				warnedAboutName = partName;
				closeConnection();
				throw new SQLException("Part name already exists. Proceed with duplicate?");
			}
		}
		try {
			int unitID = convertQuantityUnitTypeToID(quantityUnitType);
			
			SQL = "INSERT IGNORE INTO Parts (PartName, PartNumber, Vendor, UnitID, ExternalPartNumber) ";
			SQL += "VALUES (?, ?, ?, ?, ?)";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, partName);
			prepstmt.setString(2, partNumber);
			prepstmt.setString(3, vendor);
			prepstmt.setInt(4, unitID);
			prepstmt.setString(5, externalPartNumber);
			prepstmt.execute();
			warnUser = false;
			warnedAboutName = null;
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
	
	public void deletePart(Integer partID) throws SQLException, IOException {
		createConnection();
		
		try {
			if (isPartAssociatedWithInventoryItem(partID)) {
				throw new SQLException("Error: Cannot delete part associated with an inventory item."); // "Failed to delete entry..."
			}
			SQL = "DELETE FROM Parts WHERE ID=? ";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.execute();
		}
		catch (SQLException sqe) {
			closePreparedStatement();
			conn.rollback();
			closeConnection();
			throw new SQLException(sqe.getMessage()); // "Failed to delete entry..."
		} catch (IOException ioe) {
			throw new IOException(ioe.getMessage());
		}
		closePreparedStatement();
		conn.commit();
		closeConnection();
	}
	
	public void editPart(Integer partID, String partName, String partNumber, String vendor, String quantityUnitType, String externalPartNumber) throws SQLException, IOException {
		createConnection();
		
		Part p;

		try {
			p = getPart(partID);
		} 
		catch (IOException e1) {
			closeConnection();
			throw new IOException("Invalid part ID.");
		}
		catch (SQLException sqe) {
			closeConnection();
			throw new SQLException(sqe.getMessage());
		}
		
		if (isPartNumberInUse(partNumber) && !(p.getPartNumber().equals(partNumber))) {
			closeConnection();
			throw new SQLException("Error: Part # already exists.");
		}
		
		if (isPartNameInUse(partName) && !(p.getPartName().equals(partName))) {
			if (warnUser && warnedAboutName.equals(partName)) {
				// do nothing; user has already accepted this as a duplicate name
			}
			else {
				warnUser = true;
				warnedAboutName = partName;
				closeConnection();
				throw new SQLException("Part name already exists. Proceed with duplicate?");
			}
		}
		try {
			int unitID = convertQuantityUnitTypeToID(quantityUnitType);
			
			SQL = "UPDATE Parts SET PartName=?, PartNumber=?, Vendor=?, UnitID=?, ExternalPartNumber=? WHERE ID=?";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, partName);
			prepstmt.setString(2, partNumber);
			prepstmt.setString(3, vendor);
			prepstmt.setInt(4, unitID);
			prepstmt.setString(5, externalPartNumber);
			prepstmt.setInt(6, partID);
			prepstmt.execute();
			warnUser = false;
			warnedAboutName = null;
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
	
	private int convertQuantityUnitTypeToID(String quantityUnitType) throws SQLException {
		int unitID = -1;
		
		SQL = "SELECT ID FROM Units WHERE Unit=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, quantityUnitType);
			rs = prepstmt.executeQuery();
			if (!rs.next()) {
				throw new SQLException("Unit type unrecognized - not found in database.");
			}
			else {
				unitID = rs.getInt("ID");
			}
		} catch (SQLException e) {
			throw new SQLException (e.getMessage());
		}
		return unitID;
		
	}
	
	private boolean isPartNameInUse(String partName) {
		createConnection();
		// Check to see if Part already exists
		SQL = "SELECT Parts.PartName FROM Parts ";
		SQL += "WHERE PartName=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, partName);
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
			e1.printStackTrace();
			return false;
		}
	}
	
	private boolean isPartNumberInUse(String partNumber) {
		createConnection();
		// Check to see if Part Number already exists
		SQL = "SELECT Parts.PartNumber FROM Parts ";
		SQL += "WHERE PartNumber=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, partNumber);
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
			e1.printStackTrace();
			return false;
		}
	}
	
	private boolean isPartAssociatedWithInventoryItem(Integer partID) throws SQLException, IOException {
		createConnection();
		SQL = "SELECT InventoryItems.ID FROM InventoryItems ";
		SQL += "WHERE InventoryItems.PartID=?";
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
			e1.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<String> getQuantityUnitTypes() throws SQLException {
		ArrayList<String> units = new ArrayList<String>();
		createConnection();
		
		SQL = "SELECT Unit FROM Units";
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(SQL);
			rs = stmt.getResultSet();
			
			while (rs.next()) {
				units.add(rs.getString("Unit"));
			}
		} 
		catch (SQLException e) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new SQLException (e.getMessage());
		}
		closeResultSet();
		closePreparedStatement();
		closeConnection();
		return units;
	}
	
	public List<Part> getParts() {
		List<Part> parts = new ArrayList<Part>();
		createConnection();
		// Select all parts for display
		SQL = "SELECT Parts.ID, Parts.PartName, Parts.PartNumber, Parts.Vendor, Parts.ExternalPartNumber, ";
		SQL += "Units.Unit FROM Parts ";
		SQL += "INNER JOIN Units ON Parts.UnitID = Units.ID ";
		try {
			stmt = conn.createStatement();
			stmt.execute(SQL);
			rs = stmt.getResultSet();

			while (rs.next()) {
				try {
					Part p = new Part(rs.getInt("ID"), rs.getString("Unit"), rs.getString("PartName"), 
							rs.getString("PartNumber"), rs.getString("ExternalPartNumber"), rs.getString("Vendor"));
					parts.add(p);
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
		return parts;
	}
	
	public Part getPart(Integer partID) throws SQLException, IOException {
		Part part = null;
		createConnection();
		// Select all parts for display
		SQL = "SELECT Parts.ID, Parts.PartName, Parts.PartNumber, Parts.Vendor, Parts.ExternalPartNumber, ";
		SQL += "Units.Unit FROM Parts ";
		SQL += "INNER JOIN Units ON Parts.UnitID = Units.ID ";
		SQL += "WHERE Parts.ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.executeQuery();
			rs = prepstmt.getResultSet();
			if (!rs.next()) {
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				throw new SQLException("No Part with the given part ID was found in the database.");
			}
			else {
				part = new Part(rs.getInt("ID"), rs.getString("Unit"), rs.getString("PartName"), 
						rs.getString("PartNumber"), rs.getString("ExternalPartNumber"));
			}
			closeResultSet();
			closePreparedStatement();

		} 
		catch (SQLException e1) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new SQLException(e1.getMessage());
		} catch (IOException e) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new IOException(e.getMessage());
		}
		closeConnection();
		return part;
	}
}


package assignment2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import com.mysql.jdbc.PreparedStatement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Communicates with a MySQL database using JDBC
 * to select, insert, update, and delete parts
 * from inventory.
 * 
 * @author Josef Klein
 *
 */
public class PartsInventoryGateway {
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
			if (prepstmt != null) {
				prepstmt.close();
			}
			closeConnection();
			throw new SQLException(e.getMessage()); // "Duplicate entry..."
		}
		if (prepstmt != null) {
			prepstmt.close();
		}
		closeConnection();
	}
	
	public void deletePart(Integer partID) throws SQLException {
		createConnection();

		try {
			SQL = "DELETE FROM Parts WHERE ID=? ";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.execute();
		}
		catch (SQLException e) {
			if (prepstmt != null) {
				prepstmt.close();
			}
			closeConnection();
			throw new SQLException(e.getMessage()); // "Failed to delete entry..."
		}
		if (prepstmt != null) {
			prepstmt.close();
		}
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
				rs.close();
				prepstmt.close();
				return true;
			}
			else {
				rs.close();
				prepstmt.close();
				return false;
			}
		} catch (SQLException e1) {
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
				rs.close();
				prepstmt.close();
				return true;
			}
			else {
				rs.close();
				prepstmt.close();
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
	}
	
	public void addItemToInventory() { /* NOT YET IMPLEMENTED */
		createConnection();
		// Check to see if Part already exists
		SQL = "SELECT Parts.PartName, Parts.PartNumber FROM Parts ";
		SQL += "WHERE PartName=? AND PartNumber=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, "Bilbo");
			prepstmt.setString(2, "A-42");
			rs = prepstmt.executeQuery();
			while (rs.next()) {
				System.out.print(rs.getString("PartNumber") + " | ");
				System.out.print(rs.getString("PartName") + "\n");
			}
			rs.close();
			prepstmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// Check to see if location is "Unknown"
		// Then check to see if location exists in the database
		// Finally, check the quantity - ensure it is greater than 0 if INSERT
		// or at least 0 if an UPDATE

		closeConnection();
		System.out.println("Done with the search");
	}
	
	/* Gets a list of everything in the Inventory table, with appropriate joins
	public List<Part> getInventory() {
		List<Part> inventory = new ArrayList<Part>();
		createConnection();
		// Select all inventory items for display
		SQL = "SELECT Parts.PartName, Parts.PartNumber, Parts.Vendor, Parts.ExternalPartNumber, ";
		SQL += "InventoryItems.ID, InventoryItems.Quantity, Locations.Location, Units.Unit FROM Inventory ";
		SQL += "INNER JOIN InventoryItems ON Inventory.InventoryItemID = InventoryItems.ID ";
		SQL += "INNER JOIN Parts ON InventoryItems.PartID = Parts.ID ";
		SQL += "INNER JOIN Locations ON InventoryItems.LocationID = Locations.ID ";
		SQL += "INNER JOIN Units ON Parts.UnitID = Units.ID ";
		try {
			stmt = conn.createStatement();
			stmt.execute(SQL);
			rs = stmt.getResultSet();

			while (rs.next()) {
				try {
					Part p = new Part(rs.getInt("ID"), rs.getInt("Quantity"), rs.getString("Unit"), rs.getString("PartName"), 
							rs.getString("PartNumber"), rs.getString("ExternalPartNumber"), rs.getString("Location"));
					inventory.add(p);
				}
				catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		closeConnection();
		System.out.println("Done with the search");
		return inventory;
	}
	
	*/

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
							rs.getString("PartNumber"), rs.getString("ExternalPartNumber"));
					parts.add(p);
				}
				catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		closeConnection();
		System.out.println("Done with the search");
		return parts;
	}
	
	public Part getPart(Integer partID) throws SQLException, IOException {
		Part part = null;
		createConnection();
		// Select all parts for display
		SQL = "SELECT Parts.PartName, Parts.PartNumber, Parts.Vendor, Parts.ExternalPartNumber, ";
		SQL += "Units.Unit FROM Parts ";
		SQL += "INNER JOIN Units ON Parts.UnitID = Units.ID ";
		SQL += "WHERE ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.executeQuery();
			rs = stmt.getResultSet();

			if (!rs.next()) {
				if (prepstmt != null)
					prepstmt.close();
				if (rs != null)
					rs.close();
				closeConnection();
				throw new SQLException("No Part with the given part ID was found in the database.");
			}
			else {
				part = new Part(rs.getInt("ID"), rs.getString("Unit"), rs.getString("PartName"), 
						rs.getString("PartNumber"), rs.getString("ExternalPartNumber"));
			}
			rs.close();
			prepstmt.close();

		} catch (SQLException e1) {
			throw new SQLException(e1.getMessage());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		closeConnection();
		return part;
	}
	
	public List<InventoryItem> getInventory() {
		List<InventoryItem> inventory = new ArrayList<InventoryItem>();
		createConnection();
		// Select all inventory items for display
		SQL = "SELECT Parts.ID, InventoryItems.Quantity, Locations.Location FROM Inventory ";
		SQL += "INNER JOIN InventoryItems ON Inventory.InventoryItemID = InventoryItems.ID ";
		SQL += "INNER JOIN Parts ON InventoryItems.PartID = Parts.ID ";
		SQL += "INNER JOIN Locations ON InventoryItems.LocationID = Locations.ID ";
		try {
			stmt = conn.createStatement();
			stmt.execute(SQL);
			rs = stmt.getResultSet();

			while (rs.next()) {
				try {
					InventoryItem ii = new InventoryItem(rs.getInt("ID"), rs.getInt("PartID"), rs.getString("Location"), rs.getInt("Quantity"));
					inventory.add(ii);
				}
				catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		closeConnection();
		return inventory;
	}
	/*
	public InventoryItem getInventoryItem() {
		InventoryItem item = null;
		createConnection();
		
		SQL = "SELECT Parts.ID, InventoryItems.Quantity, Locations.Location FROM Inventory ";
		SQL += "INNER JOIN InventoryItems ON Inventory.InventoryItemID = InventoryItems.ID ";
		SQL += "INNER JOIN Parts ON InventoryItems.PartID = Parts.ID ";
		SQL += "INNER JOIN Locations ON InventoryItems.LocationID = Locations.ID ";
		try {
			stmt = conn.createStatement();
			stmt.execute(SQL);
			rs = stmt.getResultSet();

			while (rs.next()) {
				try {
					InventoryItem ii = new InventoryItem(rs.getInt("ID"), rs.getInt("PartID"), rs.getString("Location"), rs.getInt("Quantity"));
					inventory.add(ii);
				}
				catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			}
			rs.close();
			stmt.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		closeConnection();
		return inventory;
	}
*/
}
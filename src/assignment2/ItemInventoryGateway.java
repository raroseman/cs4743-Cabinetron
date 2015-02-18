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
public class ItemInventoryGateway {
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
	
	public void addInventoryItem(Integer partID, String location, Integer quantity) throws SQLException, IOException {
		createConnection();
		
		int locationID = convertLocationTypeToID(location);
		if (!isPartAndLocationUnique(partID, locationID)) {
			closeConnection();
			throw new IOException("Error: The Part ID is already associated with that location.");
		}
		try {	
			SQL = "INSERT IGNORE INTO InventoryItems (PartID, LocationID, Quantity) ";
			SQL += "VALUES (?, ?, ?)";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.setInt(2, locationID);
			prepstmt.setInt(3, quantity);
			prepstmt.execute();
		}
		catch (SQLException e) {
			closePreparedStatement();
			closeConnection();
			throw new SQLException(e.getMessage()); // "Duplicate entry..."
		}
		closePreparedStatement();
		closeConnection();
	}
	
	public void deleteInventoryItem(Integer itemID) throws SQLException, IOException {
		createConnection();
		
		if (!isQuantityZero(itemID)) {
			throw new IOException("Error: Cannot remove an InventoryItem with a non-zero quantity.");
		}
		try {
			SQL = "DELETE FROM InventoryItems WHERE ID=? ";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, itemID);
			prepstmt.execute();
		}
		catch (SQLException sqe) {
			closePreparedStatement();
			closeConnection();
			throw new SQLException(sqe.getMessage()); // "Failed to delete entry..."
		}
		closePreparedStatement();
		closeConnection();
	}
	
	public void editInventoryItem(Integer itemID, Integer partID, String location, Integer quantity) throws SQLException, IOException {
		createConnection();
		/*
		InventoryItem ii = null;

		try {
			ii = getInventoryItem(itemID);
		} 
		catch (IOException e1) {
			closeConnection();
			throw new IOException(e1.getMessage());
		}
		catch (SQLException sqe) {
			closeConnection();
			throw new SQLException(sqe.getMessage());
		}
		*/
		if (!isPart(partID)) {
			closeConnection();
			throw new IOException("Error: Part ID does not match any Part in database.");
		}
		int locationID = convertLocationTypeToID(location);
		if (!isPartAndLocationUnique(partID, locationID)) {
			closeConnection();
			throw new IOException("Error: The Part ID is already associated with that location.");
		}
		try {
			SQL = "UPDATE InventoryItems SET PartID=?, LocationID=?, Quantity=? WHERE ID=?";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.setInt(2, locationID);
			prepstmt.setInt(3, quantity);
			prepstmt.execute();
		}
		catch (SQLException e) {
			closePreparedStatement();
			closeConnection();
			throw new SQLException(e.getMessage()); // "Duplicate entry..."
		}
		closePreparedStatement();
		closeConnection();
	}
	
	private boolean isQuantityZero(Integer itemID) throws SQLException, IOException {
		createConnection();
		SQL = "SELECT InventoryItems.Quantity FROM InventoryItems ";
		SQL += "WHERE InventoryItems.ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, itemID);
			rs = prepstmt.executeQuery();
			if (rs.next()) {
				int q = rs.getInt("Quantity");
				closeResultSet();
				closePreparedStatement();
				if (q == 0) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				closeResultSet();
				closePreparedStatement();
				return false;
			}
		} catch (SQLException e1) {
			closeResultSet();
			closePreparedStatement();
			throw new SQLException("Internal Error: Failed to query the quantity of the given InventoryItem ID.");
		}
	}
	
	private boolean isPartAndLocationUnique(Integer partID, Integer locationID) throws SQLException, IOException {
		createConnection();
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
			throw new SQLException("Internal Error: Failed to query the quantity of the given InventoryItem ID.");
		}
	}
	
	private boolean isPart(Integer partID) throws SQLException, IOException {
		createConnection();
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
			throw new SQLException("Internal Error: Failed to query the quantity of the given InventoryItem ID.");
		}
	}
	
	private int convertLocationTypeToID(String location) throws SQLException {
		int locationID = -1;
		
		SQL = "SELECT Locations.ID FROM Locations WHERE Location=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, location);
			rs = prepstmt.executeQuery();
			if (!rs.next()) {
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				throw new SQLException("Error: Location unrecognized - not found in database.");
			}
			else {
				locationID = rs.getInt("ID");
			}
		} catch (SQLException e) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new SQLException (e.getMessage());
		}
		closeResultSet();
		closePreparedStatement();
		closeConnection();
		return locationID;
		
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
			closeResultSet();
			closeStatement();

		} 
		catch (SQLException e1) {
			closeResultSet();
			closeStatement();
			e1.printStackTrace();
		}
		closeConnection();
		return inventory;
	}
	
	public InventoryItem getInventoryItem(Integer itemID) throws SQLException, IOException {
		InventoryItem ii = null;
		createConnection();
		
		SQL = "SELECT InventoryItems.ID, InventoryItems.PartID, InventoryItems.LocationID, InventoryItems.Quantity FROM Inventory WHERE ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, itemID);
			prepstmt.executeQuery();
			rs = stmt.getResultSet();

			if (rs.next()) {
				try {
					ii = new InventoryItem(rs.getInt("ID"), rs.getInt("PartID"), rs.getString("LocationID"), rs.getInt("Quantity"));
				}
				catch (IOException ioe) {
					closeResultSet();
					closePreparedStatement();
					closeConnection();
					throw new IOException(ioe.getMessage());
				}
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
		closeConnection();
		return ii;
	}
}

package InventoryItems;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import com.mysql.jdbc.PreparedStatement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import Parts.Part;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Communicates with a MySQL database using JDBC
 * to select, insert, update, and delete parts
 * from inventory.
 * 
 * @author Josef Klein
 *
 */
public class InventoryItemGateway {
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
	
	public boolean checkTimestamp(Integer itemID, String prevTimestamp) throws SQLException, IOException {
		String currTimestamp = null;
		
		SQL = "SELECT Timestamp FROM InventoryItems ";
		SQL += "WHERE InventoryItems.ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, itemID);
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
	
	public void addInventoryItem(Integer partID, String location, Integer quantity) throws SQLException, IOException {
		createConnection();
		
		int locationID = convertLocationTypeToID(location);
		if (!isPartAndLocationUnique(partID, locationID)) {
			closeConnection();
//4			// Concurrency issue - part was added but was not displayed in list view.
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
			conn.rollback();
			closeConnection();
			throw new SQLException(e.getMessage()); // "Duplicate entry..."
		}
		closePreparedStatement();
		conn.commit();
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
			conn.rollback();
			closeConnection();
			throw new SQLException(sqe.getMessage()); // "Failed to delete entry..."
		}
		closePreparedStatement();
		conn.commit();
		closeConnection();
	}
	
	public void editInventoryItem(Integer itemID, Integer partID, String location, Integer quantity, String prevTimestamp) throws SQLException, IOException {
		createConnection();
		
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
		int locationID = convertLocationTypeToID(location);
		if (!checkTimestamp(ii.getID(), prevTimestamp)) {
			closeConnection();
			
			throw new IOException("Error: This InventoryItem was recently changed. You may wish to review these changes before submitting your own.");
//4
		}
		
		if (!(ii.getPartID().equals(partID) && ii.getLocation().equals(location))) { // part ID or location have changed	
			if (!isPart(partID)) {
				closeConnection();
				throw new IOException("Error: Part ID does not match any Part in database.");
			}
			if (!isPartAndLocationUnique(partID, locationID)) {
				closeConnection();
				throw new IOException("Error: The Part ID is already associated with that location.");
			}
		}
		try {
			SQL = "UPDATE InventoryItems SET PartID=?, LocationID=?, Quantity=? WHERE ID=?";
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.setInt(2, locationID);
			prepstmt.setInt(3, quantity);
			prepstmt.setInt(4, itemID);
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
		SQL = "SELECT InventoryItems.ID FROM InventoryItems ";
		SQL += "WHERE InventoryItems.PartID=? AND InventoryItems.LocationID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, partID);
			prepstmt.setInt(2, locationID);
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
			throw new SQLException("Internal Error: Failed to query the given InventoryItem.");
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
			throw new SQLException("Internal Error: Failed to query the part ID of the given InventoryItem part ID.");
		}
	}
	
	private int convertLocationTypeToID(String location) throws SQLException {
		int locationID = -1;
		
		SQL = "SELECT Locations.ID FROM Locations WHERE Location=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, location);
			rs = prepstmt.executeQuery();
			if (rs.next()) {
				locationID = rs.getInt("ID");
			}
			else {
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				throw new SQLException("Error: Location unrecognized - not found in database.");
			}
		} catch (SQLException e) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new SQLException (e.getMessage());
		}
		closeResultSet();
		closePreparedStatement();
		
		return locationID;
		
	}
	
	public List<InventoryItem> getInventory() {
		List<InventoryItem> inventory = new ArrayList<InventoryItem>();
		createConnection();
		// Select all inventory items for display
		SQL = "SELECT InventoryItems.ID, InventoryItems.PartID, Units.Unit, Parts.PartName, Parts.PartNumber, Parts.ExternalPartNumber, ";
		SQL += "InventoryItems.Quantity, Locations.Location, InventoryItems.Timestamp FROM InventoryItems ";
		SQL += "INNER JOIN Parts ON InventoryItems.PartID = Parts.ID ";
		SQL += "INNER JOIN Units ON Units.ID = Parts.UnitID ";
		SQL += "INNER JOIN Locations ON InventoryItems.LocationID = Locations.ID ";
		try {
			stmt = conn.createStatement();
			stmt.execute(SQL);
			rs = stmt.getResultSet();

			while (rs.next()) {
				try {
					Part p = new Part(rs.getInt("PartID"), rs.getString("Unit"), rs.getString("PartName"), rs.getString("PartNumber"), rs.getString("ExternalPartNumber"));
					InventoryItem ii = new InventoryItem(rs.getInt("ID"), p, rs.getString("Location"), rs.getInt("Quantity"), rs.getString("Timestamp"));
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
	
	public InventoryItem getUpdatedInventoryItem(Integer itemID) throws SQLException, IOException {
		InventoryItem ii = null;
		createConnection();
		
		SQL = "SELECT InventoryItems.ID, InventoryItems.PartID, Units.Unit, Parts.PartName, Parts.PartNumber, Parts.ExternalPartNumber, ";
		SQL += "InventoryItems.Quantity, Locations.Location, InventoryItems.Timestamp FROM InventoryItems ";
		SQL += "INNER JOIN Parts ON InventoryItems.PartID = Parts.ID ";
		SQL += "INNER JOIN Units ON Units.ID = Parts.UnitID ";
		SQL += "INNER JOIN Locations ON InventoryItems.LocationID = Locations.ID ";
		SQL += "WHERE InventoryItems.ID=?";
		
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, itemID);
			rs = prepstmt.executeQuery();

			if (rs.next()) {
				try {
					Part p = new Part(rs.getInt("PartID"), rs.getString("Unit"), rs.getString("PartName"), rs.getString("PartNumber"), rs.getString("ExternalPartNumber"));
					ii = new InventoryItem(rs.getInt("ID"), p, rs.getString("Location"), rs.getInt("Quantity"), rs.getString("Timestamp"));
					
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
	
	public InventoryItem getInventoryItem(Integer itemID) throws SQLException, IOException {
		InventoryItem ii = null;
//		createConnection();
		
		SQL = "SELECT InventoryItems.ID, InventoryItems.PartID, Locations.Location, InventoryItems.Quantity, InventoryItems.Timestamp FROM InventoryItems ";
		SQL += "INNER JOIN Locations ON InventoryItems.LocationID = Locations.ID ";
		SQL += "WHERE InventoryItems.ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, itemID);
			rs = prepstmt.executeQuery();

			if (rs.next()) {
				try {
					ii = new InventoryItem(rs.getInt("ID"), rs.getInt("PartID"), rs.getString("Location"), rs.getInt("Quantity"), rs.getString("Timestamp"));
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
//		closeConnection();
		return ii;
	}
	
	public ArrayList<String> getLocations() throws SQLException {
		ArrayList<String> locations = new ArrayList<String>();
		createConnection();
		
		SQL = "SELECT Location FROM Locations";
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(SQL);
			rs = stmt.getResultSet();
			
			while (rs.next()) {
				locations.add(rs.getString("Location"));
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
		return locations;
	}
	
	public ArrayList<String> getParts() throws SQLException {
		ArrayList<String> parts = new ArrayList<String>();
		createConnection();
		
		SQL = "SELECT Parts.PartNumber FROM Parts";
		try {
			stmt = conn.createStatement();
			stmt.executeQuery(SQL);
			rs = stmt.getResultSet();
			
			while (rs.next()) {
				parts.add(rs.getString("PartNumber"));
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
		return parts;
	}
	
	public Integer getPartIDByPartNumber(String partNumber) throws SQLException {
		Integer id = 0;
		
		createConnection();
		
		SQL = "SELECT Parts.ID FROM Parts WHERE PartNumber=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setString(1, partNumber);
			rs = prepstmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt("ID");
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				return id;
			}
			else {
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				throw new SQLException ("Error: No part exists with that part number.");
			}
		} 
		catch (SQLException e) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new SQLException (e.getMessage());
		}
	}
	
	public String getPartNumberByID(Integer ID) throws SQLException {
		String partNumber = null;
		createConnection();
		
		SQL = "SELECT Parts.PartNumber FROM Parts WHERE Parts.ID=?";
		try {
			prepstmt = conn.prepareStatement(SQL);
			prepstmt.setInt(1, ID);
			rs = prepstmt.executeQuery();
			
			if (rs.next()) {
				partNumber = rs.getString("PartNumber");
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				return partNumber;
			}
			else {
				closeResultSet();
				closePreparedStatement();
				closeConnection();
				throw new SQLException ("Error: No part exists with that part ID.");
			}
		} 
		catch (SQLException e) {
			closeResultSet();
			closePreparedStatement();
			closeConnection();
			throw new SQLException (e.getMessage());
		}
	}
}

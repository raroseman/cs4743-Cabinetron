package assignment2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	static MysqlDataSource ds = new MysqlDataSource();
	static Connection conn; // the database connection handle
	static String URL = "jdbc:mysql://devcloud.fulgentcorp.com:3306/eay250";
	static String username = "eay250";
	static String password = "HnGe29Bwm7NBLvsgCGX8";
	static String SQL;
	static Statement stmt; // the statement handle for the connection
	static ResultSet rs; // the results of an executed statement
	
	public static void DisplayInventory_All() {
		ds.setURL(URL);
		ds.setUser(username);
		ds.setPassword(password);
		
		// Create a database connection object
		try {
			if (ds != null)
				conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
				System.out.print(rs.getString("ID") + " | ");
				System.out.print(rs.getString("PartNumber") + " | ");
				System.out.print(rs.getString("PartName") + " | ");
				System.out.print(rs.getString("Vendor") + " | ");
				System.out.print(rs.getString("ExternalPartNumber") + " | ");
				System.out.print(rs.getString("Quantity") + " | ");
				System.out.print(rs.getString("Unit") + " | ");
				System.out.print(rs.getString("Location") + "\n");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	

		
		// Close all JDBC objects
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Done with the search");
	}

}

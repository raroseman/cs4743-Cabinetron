package assignment2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DB_InsertTableData {
	MysqlDataSource ds = new MysqlDataSource();
	Connection conn; // the database connection handle
	String URL = "jdbc:mysql://devcloud.fulgentcorp.com:3306/eay250";
	String username = "eay250";
	String password = "HnGe29Bwm7NBLvsgCGX8";
	String SQL;
	Statement stmt; // the statement handle for the connection
	ResultSet rs; // the results of an executed statement
	
	
	void Setup() {
		ds.setURL(URL);
		ds.setUser(username);
		ds.setPassword(password);
		
		// Create a database connection object
		try {
			if (ds != null) {
				conn = ds.getConnection();
				stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Insert();
	}
	
	void Insert() {
		SQL = "INSERT INTO Units (Unit) VALUES ";
		SQL += "('Unknown'), ('Linear Feet'), ('Pieces');";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "INSERT INTO Locations (Location) VALUES ";
		SQL += "('Unknown'), ('Facility 1 Warehouse 1'), ('Facility 1 Warehouse 2'), ('Pieces')";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
//		SQL = "INSERT INTO Parts (PartNumber, PartName, UnitID) VALUES ";
//		SQL += "('A-42', 'Bilbo', 3), ('B-64', 'Frodo', 2) ";
		SQL = "INSERT INTO Parts (PartNumber, PartName, UnitID, ExternalPartNumber) VALUES ";
		for (int i = 1; i < 25; i++) {
			SQL += "('A"+i+"', 'MyPart"+i+"', 3, 'EX"+i+"'), ";
		}
		SQL += "('A99', 'MyPart99', 3, 'EX99') ";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "INSERT INTO InventoryItems (PartID, LocationID, Quantity) VALUES ";
		SQL += "(1, 3, 1), (2, 2, 2) ";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "INSERT INTO Inventory (InventoryItemID) VALUES ";
		SQL += "(1), (2) ";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		// Close all JDBC objects
		try {
			if (conn != null)
				conn.close();
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Done inserting placeholder data into tables");
	}
}

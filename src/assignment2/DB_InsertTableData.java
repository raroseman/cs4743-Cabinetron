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
		SQL += "('Unknown'), ('Facility 1 Warehouse 1'), ('Facility 1 Warehouse 2'), ('Facility 2')";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		String vendor = "ACME, Inc.";
		SQL = "INSERT INTO Parts (PartNumber, PartName, UnitID, ExternalPartNumber, Vendor) VALUES ";
		for (int i = 1; i < 50; i++) {
			if (i % 2 == 0) {
				if (i % 4 == 0) {
					SQL += "('A"+i+"', 'MyPart"+i+"', 2, 'EX"+i+"', '"+vendor+"'), ";
				}
				else {
					SQL += "('A"+i+"', 'MyPart"+i+"', 3, 'EX"+i+"', '"+vendor+"'), ";
				}
			}
			else {
				SQL += "('A"+i+"', 'MyPart"+i+"', 3, 'EX"+i+"', ''), ";
			}
		}
		SQL += "('C4', 'PartC4', 3, 'EX-C499', '') ";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "INSERT INTO InventoryItems (PartID, LocationID, Quantity) VALUES ";
		for (int i = 25; i > 0; i--) {
			SQL += "("+i+", 3, "+i+"), ";
		}
		SQL += "(1, 2, 1) ";
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

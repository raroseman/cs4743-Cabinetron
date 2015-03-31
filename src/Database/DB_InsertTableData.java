package Database;

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
	
	
	public void Setup() {
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
					SQL += "('P"+i+"', 'MyPart"+i+"', 2, 'EX"+i+"', '"+vendor+"'), ";
				}
				else {
					SQL += "('P"+i+"', 'MyPart"+i+"', 3, 'EX"+i+"', '"+vendor+"'), ";
				}
			}
			else {
				SQL += "('P"+i+"', 'MyPart"+i+"', 3, 'EX"+i+"', ''), ";
			}
		}
		SQL += "('PC4', 'PartC4', 3, 'EX-C499', '') ";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "INSERT INTO ProductTemplates (ProductNumber, Description) VALUES ";

		SQL += "('AProduct1', 'A poor product.'), ";
		SQL += "('AProduct2', 'A mediocre product.'), ";
		SQL += "('AProduct3', 'A good product.'), ";
		SQL += "('AProduct4', 'A great product.'), ";
		SQL += "('AProduct5', 'An exceptional product.') ";

		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "INSERT INTO ProductTemplateParts (ProductTemplateID, PartID, Quantity) VALUES ";
		SQL += "(1, 1, 10), ";
		SQL += "(1, 2, 5), ";
		SQL += "(1, 3, 2), ";
		SQL += "(1, 4, 1), ";
		SQL += "(2, 1, 20), ";
		SQL += "(2, 2, 12), ";
		SQL += "(2, 3, 2), ";
		SQL += "(3, 3, 2), ";
		SQL += "(3, 5, 2), ";
		SQL += "(3, 7, 2), ";
		SQL += "(3, 11, 2), ";
		SQL += "(4, 6, 5), ";
		SQL += "(5, 8, 1), ";
		SQL += "(5, 9, 1), ";
		SQL += "(5, 10, 1) ";

		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "INSERT INTO InventoryItems (PartID, LocationID, Quantity, ProductTemplateID) VALUES ";
		for (int i = 25; i > 0; i--) {
			if (i % 3 == 0) {
				SQL += "("+i+", 2, "+i+", NULL), ";
			}
			else if (i % 3 == 1){
				SQL += "("+i+", 3, "+i+", NULL), ";
			}
			else {
				SQL += "("+i+", 4, "+i+", NULL), ";
			}
		}
		SQL += "(NULL, 2, 1, 1) ";
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

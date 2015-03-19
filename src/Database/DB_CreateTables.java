package Database;

import com.mysql.jdbc.jdbc2.optional.*;

import java.sql.*;

public class DB_CreateTables {
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
			if (ds != null)
				conn = ds.getConnection();
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Drop table if it existed
		
		SQL = "DROP TABLE IF EXISTS ProductTemplateParts";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "DROP TABLE IF EXISTS ProductTemplates";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "DROP TABLE IF EXISTS Inventory"; // depends on InventoryItems
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "DROP TABLE IF EXISTS InventoryItems"; // depends on Parts, Locations
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "DROP TABLE IF EXISTS Parts"; // depends on Units
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		
		SQL = "DROP TABLE IF EXISTS Units";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "DROP TABLE IF EXISTS Locations";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		// Create all tables for assignment 3
		

		SQL = "CREATE TABLE Units (";
		SQL += "ID INT(10) NOT NULL AUTO_INCREMENT, ";
		SQL += "Unit VARCHAR(64) NOT NULL UNIQUE, ";
		SQL += "PRIMARY KEY (ID) )";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "CREATE TABLE Locations (";
		SQL += "ID INT(10) NOT NULL AUTO_INCREMENT, ";
		SQL += "Location VARCHAR(64) NOT NULL UNIQUE, ";
		SQL += "PRIMARY KEY (ID) )";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "CREATE TABLE Parts (";
		SQL += "ID INT(10) NOT NULL AUTO_INCREMENT, ";
		SQL += "PartNumber VARCHAR(20) NOT NULL UNIQUE, ";
		SQL += "PartName VARCHAR(255) NOT NULL, ";
		SQL += "Vendor VARCHAR(255), ";
		SQL += "UnitID INT(10) NOT NULL DEFAULT 0, ";
		SQL += "ExternalPartNumber VARCHAR(50), ";
		SQL += "Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, ";
		SQL += "PRIMARY KEY (ID), ";
		SQL += "FOREIGN KEY (UnitID) REFERENCES Units(ID) )";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "CREATE TABLE InventoryItems (";
		SQL += "ID INT(10) NOT NULL AUTO_INCREMENT, ";
		SQL += "PartID INT(10) NOT NULL, ";
		SQL += "LocationID INT(10) NOT NULL, ";
		SQL += "Quantity INT(10) NOT NULL, ";
		SQL += "Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, ";
		SQL += "PRIMARY KEY (ID), ";
		SQL += "FOREIGN KEY (PartID) REFERENCES Parts(ID), ";
		SQL += "FOREIGN KEY (LocationID) REFERENCES Locations(ID), ";
		SQL += "CONSTRAINT PartLocation UNIQUE (PartID, LocationID) )";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "CREATE TABLE ProductTemplates (";
		SQL += "ID INT(10) NOT NULL AUTO_INCREMENT, ";
		SQL += "ProductNumber VARCHAR(20) NOT NULL UNIQUE, ";
		SQL += "Description VARCHAR(255) NOT NULL, ";
		SQL += "Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, ";
		SQL += "PRIMARY KEY (ID) )";
		try {
			stmt.execute(SQL);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		SQL = "CREATE TABLE ProductTemplateParts (";
		SQL += "ID INT(10) NOT NULL AUTO_INCREMENT, ";
		SQL += "ProductTemplateID INT(10) NOT NULL, ";
		SQL += "PartID INT(10) NOT NULL, ";
		SQL += "Quantity INT(10) NOT NULL, ";
		SQL += "Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, ";
		SQL += "PRIMARY KEY (ID), ";
		SQL += "FOREIGN KEY (ProductTemplateID) REFERENCES ProductTemplates(ID), ";
		SQL += "FOREIGN KEY (PartID) REFERENCES Parts(ID), ";
		SQL += "CONSTRAINT ProductTemplatePart UNIQUE (PartID, ProductTemplateID) )";
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
		System.out.println("Done creating tables");
	}
}

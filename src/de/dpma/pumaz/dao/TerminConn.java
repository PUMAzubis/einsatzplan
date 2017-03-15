package de.dpma.pumaz.dao;

/*
     Derby - WwdEmbedded.java

       Licensed to the Apache Software Foundation (ASF) under one
           or more contributor license agreements.  See the NOTICE file
           distributed with this work for additional information
           regarding copyright ownership.  The ASF licenses this file
           to you under the Apache License, Version 2.0 (the
           "License"); you may not use this file except in compliance
           with the License.  You may obtain a copy of the License at

             http://www.apache.org/licenses/LICENSE-2.0

           Unless required by applicable law or agreed to in writing,
           software distributed under the License is distributed on an
           "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
           KIND, either express or implied.  See the License for the
           specific language governing permissions and limitations
           under the License.    

*/
/*											 
 **  This sample program is described in the Getting Started With Derby Manual
*/
//   ## INITIALIZATION SECTION ##
//  Include the java SQL classes 
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import de.dpma.pumaz.StartApp;

public class TerminConn {

	// ## DEFINE VARIABLES SECTION ##
	// define the driver to use
	String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	// the database name
	String dbName = "testDB";
	// String dbName = "terminDB";
	// define the Derby connection URL to use
	String connectionURL = "jdbc:derby:" + dbName + ";create=true";
	Connection conn = null;
	Statement s;
	PreparedStatement psInsert;
	ResultSet resultSet;
	String mainTable = "Termin";
	String printLine = "  __________________________________________________";
	String createString = "CREATE TABLE " + mainTable
			+ " (TERMIN_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
			+ "   CONSTRAINT Termin_PK PRIMARY KEY, "
			+ " TERMINNAME VARCHAR(32) NOT NULL, STARTDATUM DATE, ENDDATUM DATE, COLOR VARCHAR(255)) ";
	// ENTRY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
	String insertString = "INSERT INTO " + mainTable
			+ " (TERMINNAME, STARTDATUM, ENDDATUM, COLOR) values (?, ? , ?, ?)";
	String searchByString = "select TERMINNAME, STARTDATUM, ENDDATUM, COLOR from " + mainTable + " order by TERMINNAME";
	String deleteString = "DROP TABLE " + mainTable;
	StartApp startApp;

	public Connection establishConnection() {
		try {
			// Create (if needed) and connect to the database.
			// The driver is loaded automatically.
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("Connected to database " + dbName);

			// Beginning of the primary catch block: prints stack trace
		} catch (Throwable e) {
			/*
			 * Catch all exceptions and pass them to the
			 * Throwable.printStackTrace method
			 */
			System.out.println(" . . . exception thrown:");
			e.printStackTrace(System.out);
		} // ## DATABASE SHUTDOWN SECTION ##

		return conn;
	}

	public Connection getConnection() {
		return conn;
	}

	public void insertTermin(String terminname, LocalDate localD, LocalDate localD2, String color) {
		Date date = Date.valueOf(localD);
		Date date2 = Date.valueOf(localD2);
		// JDBC code sections
		// Beginning of Primary DB access section
		// ## BOOT DATABASE SECTION ##
		try {
			s = conn.createStatement();
			// Call utility method to check if table exists.
			if (doesTableExist(conn, mainTable)) { // soll false sein
				System.err.println("Tabelle existiert nicht??");
				psInsert = conn.prepareStatement(createString);
				psInsert.execute();
				System.err.println("Tabelle exisitiert doch");
			}

			// Prepare the insert statement to use
			psInsert = conn.prepareStatement(insertString);

			// Check if it is time to EXIT, if not insert the data
			if (!terminname.equals("")) {
				// Insert the text entered into the WISH_ITEM table
				psInsert.setString(1, terminname);
				psInsert.setDate(2, date);
				psInsert.setDate(3, date2);
				psInsert.setString(4, color);
				psInsert.executeUpdate();

				// Select all records in the TERMIN table
				resultSet = s.executeQuery(searchByString);

				// Loop through the ResultSet and print the data
				System.out.println(printLine);
				while (resultSet.next()) {
					System.out.println(
							"Terminname ist " + resultSet.getString(1) + " und er geht von " + resultSet.getDate(2)
									+ " bis " + resultSet.getDate(3) + ". Farbe ist  " + resultSet.getString(4));
				}
				System.out.println(printLine);
				// Close the resultSet
				resultSet.close();
				psInsert.close();
				s.close();

				// System.err.println("Tabelle " + mainTable + " wird versucht
				// zu löschen.");
				// psInsert = conn.prepareStatement(deleteString);
				// psInsert.execute();
				// System.err.println("Tabelle " + mainTable + " gelöscht.");
			} // END of IF block
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
		}
		/***
		 * In embedded mode, an application should shut down Derby. Shutdown
		 * throws the XJ015 exception to confirm success.
		 ***/
	}

	public void closeConnection(Connection conn) {
		try {
			conn.close();
			if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
				boolean gotSQLExc = false;
				try {
					DriverManager.getConnection("jdbc:derby:;shutdown=true");
				} catch (SQLException se) {
					if (se.getSQLState().equals("XJ015")) {
						gotSQLExc = true;
					}
				}
				if (!gotSQLExc) {
					System.out.println("Database did not shut down normally");
				} else {
					System.out.println("Database shut down normally");
				}
				System.out.println("Getting Started With Derby JDBC program ending.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Closed connection");
	}
	// Release the resources (clean up )

	public static boolean doesTableExist(Connection conn, String tablename) throws SQLException {
		try (ResultSet resSet = conn.getMetaData().getTables(null, null, tablename, null)) {
			while (resSet.next()) {
				System.out.println(resSet.next());
				String table = resSet.getString("TERMIN");
				System.out.println(table);
				if (table.toLowerCase().equals(tablename.toLowerCase())) {
					System.out.println("Tabellenname ist " + table);
					return true;
				}
			}
		} catch (SQLException e) {
			System.err.println("SQLException in doesTableExist");
			e.printStackTrace();
		}
		System.err.println("doesTableExist wirft false");
		return false;
	}
}
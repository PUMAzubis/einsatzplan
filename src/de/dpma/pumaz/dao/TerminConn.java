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
import de.dpma.pumaz.model.Termin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class TerminConn {

	// ## DEFINE VARIABLES SECTION ##
	// define the driver to use
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	// the database name
	private String dbName = "constraintDB";
	// String dbName = "terminDB";
	// define the Derby connection URL to use
	private String connectionURL = "jdbc:derby:" + dbName + ";create=true";
	private Connection conn = null;
	private Statement s;
	private PreparedStatement psInsert;
	private ResultSet resultSet;
	private String terminTable = "TERMIN";
	private String printLine = "  __________________________________________________";
//	private String createString = "CREATE TABLE " + terminTable
//			+ " (TERMIN_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
//			+ "   CONSTRAINT Termin_PK PRIMARY KEY, "
//			+ " TERMINNAME VARCHAR(32) NOT NULL, STARTDATUM DATE, ENDDATUM DATE, COLOR VARCHAR(255)) ";
	private String createString = "CREATE TABLE " + terminTable
			+ " (TERMIN_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
			+ "   CONSTRAINT Termin_PK PRIMARY KEY, "
			+ " TERMINNAME VARCHAR(32) NOT NULL, STARTDATUM DATE, ENDDATUM DATE, COLOR VARCHAR(255), AZUBI_FK INTEGER, CONSTRAINT AZUBI_FK FOREIGN KEY(AZUBI_FK) REFERENCES APP.AZUBI(AZUBI_ID)) ";
	private String insertString = "INSERT INTO " + terminTable
			+ " (TERMINNAME, STARTDATUM, ENDDATUM, COLOR) values (?, ? , ?, ?)";
	private String searchByString = "select TERMIN_ID, TERMINNAME, STARTDATUM, ENDDATUM, COLOR from " + terminTable + " order by TERMIN_ID";
	private String deleteString = "DROP TABLE " + terminTable;
	private String countString = "SELECT COUNT(*) FROM " + terminTable;
	private StartApp startApp;

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
		Date dateStart = Date.valueOf(localD);
		Date dateEnd = Date.valueOf(localD2);
		// JDBC code sections
		// Beginning of Primary DB access section
		// ## BOOT DATABASE SECTION ##
		try {
			s = conn.createStatement();
			// Call utility method to check if table exists.
			if (!doesTableExist(terminTable)) { // soll false sein
				System.out.println("TABELLE WIRD ANGELEGT");
				psInsert = conn.prepareStatement(createString);
				psInsert.execute();
			}
			// Prepare the insert statement to use
			psInsert = conn.prepareStatement(insertString);
			// Check if it is time to EXIT, if not insert the data
			if (!terminname.equals("")) {
				// Insert the text entered into the WISH_ITEM table
				psInsert.setString(1, terminname);
				psInsert.setDate(2, dateStart);
				psInsert.setDate(3, dateEnd);
				psInsert.setString(4, color);
				psInsert.executeUpdate();

				// Select all records in the TERMIN table
				resultSet = s.executeQuery(searchByString);

				// Loop through the ResultSet and print the data
				System.out.println(printLine);
				while (resultSet.next()) {
					System.out.println(
//							"Terminname ist " + resultSet.getString(1) + " und er geht von " + resultSet.getDate(2)
//									+ " bis " + resultSet.getDate(3) + ". Farbe ist  " + resultSet.getString(4));
							"Terminid ist " + resultSet.getInt(1) + " und er heißt " + resultSet.getString(2)
							+ " und er geht von " + resultSet.getDate(3) + " bis"+ resultSet.getDate(4)
							+ ". Farbe ist  " + resultSet.getString(4));
				}
				System.out.println(printLine);
				// Close the resultSet
				// Release the resources (clean up )
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

	/**
	 * Überprüft, ob eine Tabelle bereits existiert, falls nicht, legt er sie an und gibt einen boolean zurück.
	 * @param conn
	 * @param tablename
	 * @param psInsert
	 * @param createString
	 * @return true, falls die Tabelle existiert. Sonst false.
	 * @throws SQLException
	 */
	public boolean doesTableExist(String tablename) throws SQLException {
		try (ResultSet resSet = conn.getMetaData().getTables(null, null, tablename, new String[]{"TABLE", "VIEW"})) {
			if (resSet.next()) {
				resSet.close();
				return true;
			}else{
				resSet.close();
				return false;
			}
		} catch (SQLException e) {
			System.err.println("SQLException in doesTableExist");
			e.printStackTrace();
		}
		System.err.println("doesTableExist wirft true");
		return false;
	}
	
	/**
	 * Liest die Information eines Termins aus der Datenbank aus und gibt ihn zurück.
	 * @return Liste, die mit Einträgen aus der Datenbank gefüllt wird.
	 */
	public ObservableList<Termin> getDBTermin(){
		ObservableList<Termin> list = FXCollections.observableArrayList();
		list = startApp.getTerminList();
		try {
			s = conn.createStatement();
			// Select all records in the TERMIN table
			resultSet = s.executeQuery(searchByString);

			// Loop through the ResultSet and print the data
			while (resultSet.next()) {
				String s = resultSet.getString(5);
				Color color = Color.web(s);
				Termin termin = new Termin(resultSet.getString(2),resultSet.getDate(3).toLocalDate(), resultSet.getDate(4).toLocalDate(),  color); 
				StartApp.addTermin(termin);
			}
			resultSet.close();
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Überprüft wie viele Termine in der Tabelle stehen.
	 * @return anzahl der Termine
	 */
	public int anzahlTermin(){
		int rowCount = 0;
		try {
			s = conn.createStatement();
			resultSet = s.executeQuery(countString);
			resultSet.next();
			rowCount = resultSet.getInt(1);
			System.out.println("anzahlTermine " + rowCount);	
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowCount;
	}
	
	public void setApp(StartApp startApp){
		this.startApp = startApp;
	}
}
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

public class TerminDAO {

	// ## DEFINE VARIABLES SECTION ##
	// define the driver to use
	
	private Statement s;
	private PreparedStatement psInsert;
	private ResultSet resultSet;
	private String terminTable = "TERMIN";

	public void insertTermin(String terminname, LocalDate localD, LocalDate localD2, String color, int id, Connection conn) {
		String createString = "CREATE TABLE " + terminTable
				+ " (TERMIN_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
				+ "   CONSTRAINT Termin_PK PRIMARY KEY, "
				+ " TERMINNAME VARCHAR(32) NOT NULL, STARTDATUM DATE, ENDDATUM DATE, COLOR VARCHAR(255), AZUBI_FK INTEGER, CONSTRAINT AZUBI_FK FOREIGN KEY(AZUBI_FK) REFERENCES APP.AZUBI(AZUBI_ID)) ";
		String insertString = "INSERT INTO " + terminTable
				+ " (TERMINNAME, STARTDATUM, ENDDATUM, COLOR, AZUBI_FK) values (?, ?, ?, ?, ?)";
		String searchByString = "select * from " + terminTable + " order by TERMIN_ID";
		String printLine = "  __________________________________________________";
		Date dateStart = Date.valueOf(localD);
		Date dateEnd = Date.valueOf(localD2);
		// JDBC code sections
		// Beginning of Primary DB access section
		// ## BOOT DATABASE SECTION ##
		try {
			s = conn.createStatement();
			// Call utility method to check if table exists.
			if (!doesTableExist(terminTable, conn)) { // soll false sein
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
				psInsert.setInt(5, id);
				psInsert.executeUpdate();

				// Select all records in the TERMIN table
				resultSet = s.executeQuery(searchByString);

				// Loop through the ResultSet and print the data
				System.out.println(printLine);
				while (resultSet.next()) {
					System.out.println(
							"Terminid ist " + resultSet.getInt(1) + ". Name: " + resultSet.getString(2)
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
	
	public void updateTermin(Termin termin, Connection conn){
		String updateString= "UPDATE " + terminTable + " SET STARTDATUM = (?), ENDDATUM = (?), COLOR = (?) WHERE TERMIN_ID = (?)";
		Date date = Date.valueOf(termin.getStartDatum());
		Date date2 = Date.valueOf(termin.getEndDatum());
		String color = termin.getFarbe().toString();
		try {
			psInsert = conn.prepareStatement(updateString);
			
			psInsert.setDate(1, date);
			psInsert.setDate(2, date2);
			psInsert.setString(3, color);
			psInsert.setInt(4, termin.getTermin_id());
			psInsert.executeUpdate();
			
			psInsert.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTermin(int id, Connection conn){
		String deleteString = "DELETE from " + terminTable + " WHERE TERMIN_ID = (?)";
		try {
			psInsert = conn.prepareStatement(deleteString);
			
			psInsert.setInt(1, id);
			psInsert.executeUpdate();
			
			psInsert.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Überprüft, ob eine Tabelle bereits existiert, und gibt true zurück, wenn dem so ist.
	 * 
	 * @param tablename
	 * @param conn - die Verbindung auf der die Operationen durchgeführt werden.
	 * @return boolean, true, wenn die Tabelle existiert. Sonst false.
	 * @throws SQLException
	 */
	public boolean doesTableExist(String tablename, Connection conn) throws SQLException {
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
	 * 
	 * @return Liste, die mit Einträgen aus der Datenbank gefüllt wird.
	 */
	public ObservableList<Termin> getDBTermin(String name, String vorname, Connection conn){
		String joinString = "Select TERMIN.* from TERMIN left outer JOIN AZUBI ON AZUBI.AZUBI_ID = TERMIN.AZUBI_FK WHERE Name = (?) AND VORNAME = (?)";
		ObservableList<Termin> list = FXCollections.observableArrayList();
		list = StartApp.getTerminList();
		try {
			s = conn.createStatement();
			// Select all records in the TERMIN table
//			resultSet = s.executeQuery(searchByString);
			psInsert = conn.prepareStatement(joinString);
			psInsert.setString(1, name);
			psInsert.setString(2, vorname);
			
			resultSet = psInsert.executeQuery();

			// Loop through the ResultSet and print the data
			while (resultSet.next()) {
				String s = resultSet.getString(5);
				Color color = Color.web(s);
				Termin termin = new Termin(resultSet.getString(2),resultSet.getDate(3).toLocalDate(), resultSet.getDate(4).toLocalDate(),  color, resultSet.getInt(1)); 
				StartApp.addTermin(termin);
			}
			resultSet.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int getTerminID(Termin termin, Connection conn)
	{
		String IDString = "Select TERMIN_ID from " + terminTable + " WHERE TERMINNAME = (?) AND STARTDATUM = (?) AND ENDDATUM = (?) AND COLOR = (?)";
		Date ld = Date.valueOf(termin.getStartDatum());
		Date ld2 = Date.valueOf(termin.getEndDatum());
		String color = termin.getFarbe().toString();
		int id = 0;
		try {
			psInsert = conn.prepareStatement(IDString);
			psInsert.setString(1, termin.getTerminName());
			psInsert.setDate(2, ld);
			psInsert.setDate(3, ld2);
			psInsert.setString(4, color);
			
			resultSet = psInsert.executeQuery();
			
			if(resultSet.next()){
				id = resultSet.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	/**
	 * Überprüft wie viele Termine in der Tabelle stehen.
	 * 
	 * @return anzahl der Termine
	 */
	public int anzahlTermin(Connection conn){
		String countString = "SELECT COUNT(*) FROM " + terminTable;
		int rowCount = 0;
		try {
			s = conn.createStatement();
			resultSet = s.executeQuery(countString);
			resultSet.next();
			rowCount = resultSet.getInt(1);
			System.out.println("anzahlTermine " + rowCount);	
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCount;
	}
	
}
package de.dpma.pumaz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dpma.pumaz.model.Auszubildender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AzubiDAO {

	// ## DEFINE VARIABLES SECTION ##
		private Connection conn = null;
		private Statement s;
		private PreparedStatement psInsert;
		private ResultSet resultSet;
		private String mainTable = "AZUBI";
		private String printLine = "  __________________________________________________";
		private String createString = "CREATE TABLE " + mainTable
				+ " (AZUBI_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
				+ "   CONSTRAINT AZUBI_PK PRIMARY KEY, "
				+ " NAME VARCHAR(32) NOT NULL, VORNAME VARCHAR(32) NOT NULL, LEHRJAHR INT, AUSBILDUNGSBERUF VARCHAR(255) NOT NULL) ";
		private String insertString = "INSERT INTO " + mainTable
				+ " (NAME, VORNAME, LEHRJAHR, AUSBILDUNGSBERUF) values (?, ? , ?, ?)";
		private String searchByString = "select AZUBI_ID, NAME, VORNAME, LEHRJAHR, AUSBILDUNGSBERUF from " + mainTable + " order by AZUBI_ID";
		private String deleteString = "DROP TABLE " + mainTable;
		private String countString = "SELECT COUNT(*) FROM " + mainTable;
		
		/**
		 * Fügt einen Auszubildenden die Tabelle AZUBI ein.
		 * @param azubi
		 * @param con
		 */
		public void insertAzubi(Auszubildender azubi, Connection con) {
			this.conn = con;
			// JDBC code sections
			// Beginning of Primary DB access section
			// ## BOOT DATABASE SECTION ##
			try {
				s = conn.createStatement();
				// Call utility method to check if table exists.
				if (!doesTableExist(mainTable)) { // soll false sein
					psInsert = conn.prepareStatement(createString);
					psInsert.execute();
				}
				
				// Prepare the insert statement to use
				psInsert = conn.prepareStatement(insertString);

				// Check if it is time to EXIT, if not insert the data
				if (!azubi.getNachname().equals("") && !azubi.getVorname().equals("")) {
					// Insert the text entered into the WISH_ITEM table
					psInsert.setString(1, azubi.getNachname());
					psInsert.setString(2, azubi.getVorname());
					psInsert.setInt(3, azubi.getLehrjahr());
					psInsert.setString(4, azubi.getAusbildungsberuf());
					System.out.println("azubi.Ausbildung " +azubi.getAusbildungsberuf() );
					psInsert.executeUpdate();

					// Select all records in the TERMIN table
					resultSet = s.executeQuery(searchByString);

					// Loop through the ResultSet and print the data
					System.out.println(printLine);
					while (resultSet.next()) {
						System.out.println(
								"AZUBI ID " + resultSet.getInt(1) + " und er heißt " + resultSet.getString(2)
								+ " " + resultSet.getString(3) + " im Lehrjahr "+ resultSet.getInt(4)
								+ " und macht " + resultSet.getString(5));
					}
					System.out.println(printLine);
					// Close the resultSet
					// Release the resources (clean up )
					resultSet.close();
					psInsert.close();
					s.close();

//					 System.err.println("Tabelle " + mainTable + " wird versucht
//					 zu löschen.");
//					 psInsert = conn.prepareStatement(deleteString);
//					 psInsert.execute();
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
		
		/**
		 * Überprüft, ob eine Tabelle bereits existiert, falls nicht, wird false zurückgegeben.
		 * @param conn
		 * @param tablename
		 * @param psInsert
		 * @param createString
		 * @return true, falls die Tabelle existiert. Sonst false.
		 * @throws SQLException
		 */
	public boolean doesTableExist(String tablename) throws SQLException {
		try (ResultSet resSet = conn.getMetaData().getTables(null, null, tablename, new String[]{"TABLE","VIEW"})) {
			System.out.println("cknd" + resSet.getMetaData().getColumnName(1));
			if (resSet.next()) {
				resSet.close();
				return true;
			}
			else{
				resSet.close();
				return false;
			}
//				SOOOOOO EEEEIIIIIIIN DREEEEEEEEEEEEEEEEEEECK: System.out.println("ResultSet " + resSet.getString("AZUBI"));
//				String table = resSet.getString("AZUBI");
//				if (table.equals(tablename)) {
//					System.out.println("Tabellenname ist " + table);
//					return true;
//				}
//			}
		} catch (SQLException e) {
			System.err.println("SQLException in doesTableExist");
			e.printStackTrace();
		}
		System.err.println("doesTableExist wirft false");
		return false;
	}
	
	public ObservableList<Auszubildender> getDBAzubi(){
		ObservableList<Auszubildender> list = FXCollections.observableArrayList();
		try {
			s = conn.createStatement();
			// Select all records in the TERMIN table
			resultSet = s.executeQuery(searchByString);
			// Loop through the ResultSet and print the data
			while (resultSet.next()) {
				System.out.println("Name " + resultSet.getString(2));
				Auszubildender azubi = new Auszubildender(resultSet.getString(2),resultSet.getString(3), resultSet.getInt(4),  resultSet.getString(5)); 
				list.add(azubi);
			}
			resultSet.close();
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}

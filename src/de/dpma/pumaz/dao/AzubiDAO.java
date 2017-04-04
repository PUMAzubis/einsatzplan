package de.dpma.pumaz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.model.Auszubildender;

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
				+ "   CONSTRAINT Termin_PK PRIMARY KEY, "
				+ " NAME VARCHAR(32) NOT NULL, VORNAME VARCHAR(32) NOT NULL, LEHRJAHR INT, AUSBILDUNGSBERUF VARCHAR(255)) ";
		private String insertString = "INSERT INTO " + mainTable
				+ " (NAME, VORNAME, LEHRJAHR, AUSBILDUNGUNGSBERUF) values (?, ? , ?, ?)";
		
		private String searchByString = "select AZUBI_ID, NAME, VORNAME, LEHRJAHR, AUSBILDUNGSBERUF from " + mainTable + " order by AZUBI_ID";
		private String deleteString = "DROP TABLE " + mainTable;
		private String countString = "SELECT COUNT(*) FROM AZUBI";
		private StartApp startApp;
		
		public void insertAzubi(Auszubildender azubi, Connection con) {
			this.conn = con;
			// JDBC code sections
			// Beginning of Primary DB access section
			// ## BOOT DATABASE SECTION ##
			try {
				s = conn.createStatement();
				// Call utility method to check if table exists.
//				if (doesTableExist(conn, mainTable)) { // soll false sein
//					System.err.println("Tabelle existiert nicht??");
//					psInsert = conn.prepareStatement(createString);
//					psInsert.execute();
//					System.err.println("Tabelle exisitiert doch");
//				}

				doesTableExist(conn, mainTable, psInsert, createString);
				
				// Prepare the insert statement to use
				psInsert = conn.prepareStatement(insertString);

				// Check if it is time to EXIT, if not insert the data
				if (!azubi.getNachname().equals("") && !azubi.getVorname().equals("")) {
					// Insert the text entered into the WISH_ITEM table
					psInsert.setString(1, azubi.getNachname());
					psInsert.setString(2, azubi.getVorname());
					psInsert.setInt(3, azubi.getLehrjahr());
					psInsert.setString(4, azubi.getAusbildungsberuf());
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
		
		public static boolean doesTableExist(Connection conn, String tablename, PreparedStatement psInsert, String createString) throws SQLException {
			tablename = tablename.toLowerCase();
			try (ResultSet resSet = conn.getMetaData().getTables(null, null, tablename, null)) {
				while (resSet.next()) {
					System.out.println(resSet.next());
					String table = resSet.getString("AZUBI");
					System.out.println(table);
					if (table.toLowerCase().equals(tablename)) {
						System.out.println("Tabellenname ist " + table);
					}else{
						psInsert = conn.prepareStatement(createString);
						psInsert.execute();
					}
					return true;
				}
			} catch (SQLException e) {
				System.err.println("SQLException in doesTableExist");
				e.printStackTrace();
			}
			System.err.println("doesTableExist wirft false");
			return true;
		}
}

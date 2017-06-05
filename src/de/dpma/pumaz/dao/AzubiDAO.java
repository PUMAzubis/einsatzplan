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
	private Statement s;
	private PreparedStatement psInsert;
	private ResultSet resultSet;
	private String azubiTable = "AZUBI";
	private int id;
	private TerminDAO tc;

	/**
	 * Fügt einen Auszubildenden die Tabelle AZUBI ein.
	 * @param azubi
	 * @param con
	 */
	public void insertAzubi(Auszubildender azubi, Connection conn) {
		String createString = "CREATE TABLE " + azubiTable
				+ " (AZUBI_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)"
				+ "   CONSTRAINT AZUBI_PK PRIMARY KEY, "
				+ " NAME VARCHAR(32) NOT NULL, VORNAME VARCHAR(32) NOT NULL, LEHRJAHR INT, AUSBILDUNGSBERUF VARCHAR(255) NOT NULL) ";
		String insertString = "INSERT INTO " + azubiTable
				+ " (NAME, VORNAME, LEHRJAHR, AUSBILDUNGSBERUF) values (?, ? , ?, ?)";
		String printLine = "  __________________________________________________";
		String generatedColumns[] = { "AZUBI_ID" };
		// JDBC code sections
		// Beginning of Primary DB access section
		// ## BOOT DATABASE SECTION ##
		try {
			s = conn.createStatement();
			// Call utility method to check if table exists.
			if (!doesTableExist(azubiTable, conn)) { // soll false sein
				psInsert = conn.prepareStatement(createString);
				psInsert.execute();
			}

			// Prepare the insert statement to use
			psInsert = conn.prepareStatement(insertString, generatedColumns);

			// Check if it is time to EXIT, if not insert the data
			if (!azubi.getNachname().equals("") && !azubi.getVorname().equals("")) {
				// Insert the text entered into the WISH_ITEM table
				psInsert.setString(1, azubi.getNachname());
				psInsert.setString(2, azubi.getVorname());
				psInsert.setInt(3, azubi.getLehrjahr());
				psInsert.setString(4, azubi.getAusbildungsberuf());
				psInsert.executeUpdate();

				// Select all records in the TERMIN table
				resultSet = psInsert.getGeneratedKeys();
				if (resultSet.next())
					id = resultSet.getInt(1);

				// Loop through the ResultSet and print the data
				System.out.println(printLine);
				while (resultSet.next()) {
					System.out.println("AZUBI ID " + resultSet.getInt(1) + " und er heißt " + resultSet.getString(2)
							+ " " + resultSet.getString(3) + " im Lehrjahr " + resultSet.getInt(5) + " und macht "
							+ resultSet.getString(4));
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

	/**
	 * Überprüft, ob eine Tabelle bereits existiert, falls nicht, wird false
	 * zurückgegeben.
	 * 
	 * @param tablename
	 * @return true, falls die Tabelle existiert. Sonst false.
	 * @throws SQLException
	 */
	public boolean doesTableExist(String tablename, Connection conn) throws SQLException {
		try (ResultSet resSet = conn.getMetaData().getTables(null, null, tablename, new String[] { "TABLE", "VIEW" })) {
			System.out.println("TableTyp: " + resSet.getMetaData().getColumnName(1));
			if (resSet.next()) {
				resSet.close();
				return true;
			} else {
				resSet.close();
				return false;
			}
		} catch (SQLException e) {
			System.err.println("SQLException in doesTableExist");
			e.printStackTrace();
		}
		System.err.println("doesTableExist wirft false");
		return false;
	}

	public ObservableList<Auszubildender> getDBAzubi(Connection con) {
		String searchByString = "select AZUBI_ID, NAME, VORNAME, AUSBILDUNGSBERUF, LEHRJAHR from " + azubiTable
				+ " order by AZUBI_ID";
		ObservableList<Auszubildender> list = FXCollections.observableArrayList();
		try {
			s = con.createStatement();
			// Select all records in the TERMIN table
			resultSet = s.executeQuery(searchByString);
			// Loop through the ResultSet and print the data
			while (resultSet.next()) {
				Auszubildender azubi = new Auszubildender(resultSet.getString(2), resultSet.getString(3),
						resultSet.getInt(5), resultSet.getString(4));
				list.add(azubi);
			}
			resultSet.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ObservableList<String> getDBAzubiName(Connection conn) {
		String searchByString = "select AZUBI_ID, NAME, VORNAME, AUSBILDUNGSBERUF, LEHRJAHR from " + azubiTable
				+ " order by AZUBI_ID";

		ObservableList<String> list = FXCollections.observableArrayList();
		try {
			s = conn.createStatement();
			// Select all records in the TERMIN table
			resultSet = s.executeQuery(searchByString);
			// Loop through the ResultSet and print the data
			while (resultSet.next()) {
//				System.out.println("Name " + resultSet.getString(2));
				String name = resultSet.getString(2) + ", " + resultSet.getString(3) + ", " + resultSet.getString(4)+ ", "+ resultSet.getInt(5) + ". Lehrjahr.";
				list.add(name);
			}
			resultSet.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Auszubildender getAzubiInfo(int id, Connection conn){
		String azubiString = "select AZUBI_ID, NAME, VORNAME, LEHRJAHR, AUSBILDUNGSBERUF from " + azubiTable
				+ " where AZUBI_ID = (?)";
		Auszubildender azubi = null;
		try {
			psInsert = conn.prepareStatement(azubiString);
			psInsert.setInt(1, id);
			
			resultSet = psInsert.executeQuery();
			
			if(resultSet.next()){
				azubi = new Auszubildender(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return azubi;
	}

	public int getAzubiID() {
		return id;
	}
	
	public int getAzubiIDDB(String name, String vorname, int lehrjahr, String ausbildungsberuf, Connection conn){
		String IDString = "Select AZUBI_ID from " + azubiTable + " WHERE NAME = (?) AND VORNAME = (?) AND LEHRJAHR = (?) AND AUSBILDUNGSBERUF = (?)";
		int id = 0;
		try {
			psInsert = conn.prepareStatement(IDString);
			
			psInsert.setString(1, name);
			psInsert.setString(2, vorname);
			psInsert.setInt(3, lehrjahr);
			psInsert.setString(4, ausbildungsberuf);
			
			resultSet = psInsert.executeQuery();
			
			if(resultSet.next()){
				id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public TerminDAO getConnection() {
		return tc;
	}
}

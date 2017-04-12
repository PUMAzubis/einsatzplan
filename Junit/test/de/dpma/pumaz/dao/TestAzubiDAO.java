package test.de.dpma.pumaz.dao;
import java.sql.Connection;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.dao.AzubiDAO;
import de.dpma.pumaz.dao.TerminConn;
import de.dpma.pumaz.model.Auszubildender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestAzubiDAO {

	public static void main(String[] args) {
		StartApp start = new StartApp();
		TerminConn tc = start.getTerminConn();
		Connection con = tc.getConnection();
		Auszubildender azubi = new Auszubildender("Test", "Fall", 1, "INKA");
		AzubiDAO ad = new AzubiDAO();
		ObservableList<String> list = FXCollections.observableArrayList();
		start = start.getSA();
		tc = start.getTerminConn();
		
//		list = ad.getDBAzubiName(con);
//		
//		for(int i = 0; i < list.size(); i++)
//			System.out.println("Name " + list.get(i));
		
		ad.insertAzubi(azubi, con);
		}
	}



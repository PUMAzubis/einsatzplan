package test.de.dpma.pumaz.dao;
import java.sql.Connection;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.dao.AzubiDAO;
import de.dpma.pumaz.dao.TerminConn;
import de.dpma.pumaz.model.Auszubildender;
import de.dpma.pumaz.model.Termin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestAzubiDAO {

	public static void main(String[] args) {
		StartApp start = new StartApp();
		TerminConn tc = start.getTerminConn();
		Connection con;
		Auszubildender azubi = new Auszubildender("Test", "Fall", 1, "INKA");
		AzubiDAO ad = new AzubiDAO();
		ObservableList<Auszubildender> list = FXCollections.observableArrayList();
		start = start.getSA();
		tc = start.getTerminConn();
		con = tc.getConnection();
		list = ad.getDBAzubi();
		
//		for(int i = 0; i < list.size(); i++)
//			System.out.println("Azubi " + list.get(i));
//		}
		
		ad.insertAzubi(azubi, tc.getConnection());
	}
}

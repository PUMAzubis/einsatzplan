package de.dpma.pumaz;

import java.io.IOException;
import java.sql.Connection;

import de.dpma.pumaz.control.RootController;
import de.dpma.pumaz.dao.TerminConn;
import de.dpma.pumaz.model.Termin;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartApp extends Application{

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private TerminConn terminConn = new TerminConn();
	public static RootController rootController;
	
	private static ObservableList<Termin> terminList = FXCollections.observableArrayList();

	public static void main(String[] args) {
		launch(args);
	}
	
	public StartApp(){

//		terminList.add(new Termin("Berufsschule"));
//		terminList.add(new Termin("Gesundheitstag"));
//		terminList.add(new Termin("Kindergarten", LocalDate.of(2012, 12, 20), LocalDate.of(2013, 2, 12)));
	}
	
	public TerminConn getTerminConn(){
		return terminConn;
	}
	
	/**
	 * Fügt der Terminliste einen Termin hinzu.
	 * @param termin
	 */
	public static void addTermin(Termin termin){
		terminList.add(termin);
	}
	
	/**
	 * Ersetzt den Termin, der an dieser Stelle steht.
	 * @param termin
	 */
	public static void replaceTermin(Termin termin, int index){
		terminList.remove(index);
		terminList.add(index, termin);
	}
	
	/**
	 * Löscht aus der Liste an der Stelle des Übergabeparameter @index den Eintrag hinaus.
	 * @param index
	 */
	public static void removeTermin(int index){
		terminList.remove(index);
	}
	
	/**
	 * 
	 */
	public static void getFirstIndex(){
		terminList.get(0);
	}
	
	/**
	 * 
	 */
	public ObservableList<Termin> getList(){
		return terminList;
	}
	
    /**
     * Returns the data as an observable list of Termin. 
     * @return
     */
    public ObservableList<Termin> getTerminList() {
        return terminList;
    }
    
    public static boolean isEmpty(){
    	if(terminList.isEmpty()){
    		return true;
    	}
    	return false;
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Einsatzplan");
		terminConn.establishConnection();
		
		initRootLayout();
	}
	
	/**
	 * Initializes the root layout.
	 * 
	 */
	public void initRootLayout(){
		try{
			//Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
			rootLayout = (AnchorPane) loader.load();

			rootController = loader.getController();
			rootController.setStart(this);
			
			//Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt die Main-Stage zurück
	 * @return
	 */
	public Stage getPrimaryStage(){
		return primaryStage;
	}

}

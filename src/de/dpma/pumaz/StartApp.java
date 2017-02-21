package de.dpma.pumaz;

import java.io.IOException;
import java.time.LocalDate;

import de.dpma.pumaz.control.RootController;
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
	public static RootController rootController;
	
	private static ObservableList<Termin> terminList = FXCollections.observableArrayList();

	public static void main(String[] args) {
		launch(args);
	}
	
	public StartApp(){

		terminList.add(new Termin("Berufsschule"));
		terminList.add(new Termin("Gesundheitstag"));
		terminList.add(new Termin("Kindergarten", LocalDate.of(2012, 12, 20), LocalDate.of(2013, 2, 12)));
	}
	
	public static void addTermin(Termin termin){
		terminList.add(termin);
	}
	
	public static void removeTermin(int index){
		terminList.remove(index);
	}
	
    /**
     * Returns the data as an observable list of Termin. 
     * @return
     */
    public ObservableList<Termin> getTerminList() {
        return terminList;
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Einsatzplan");
		
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

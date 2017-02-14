package de.dpma.pumaz;

import java.io.IOException;

import de.dpma.pumaz.model.Auszubildender;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Start extends Application{
	/**
     * The data as an observable list of Persons.
     */
    private ObservableList<Auszubildender> personData = FXCollections.observableArrayList();
	private Stage primaryStage;
	private AnchorPane rootLayout;

	public static void main(String[] args) {
		launch(args);
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
			loader.setLocation(Start.class.getResource("view/RootLayout.fxml"));
			rootLayout = (AnchorPane) loader.load();
			
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

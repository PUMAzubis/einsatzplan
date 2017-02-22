package de.dpma.pumaz.control;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.model.Termin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootController {
	
	@FXML
	private ComboBox<String> comboboxBeruf;
	@FXML 
	private ComboBox<Integer> comboboxAusbildung;
	@FXML
	private ComboBox <Integer> comboboxEinsatzjahr;
	@FXML
    private TextField nameField;
	@FXML
	private TableView<Termin> terminTable;
	@FXML
	private TableColumn<Termin, String> terminNameColumn;
	@FXML
	private TableColumn<Termin, String> terminVonColumn;
	@FXML
	private TableColumn<Termin, String> terminBisColumn;
	List<String> ausbildungsberuf = Arrays.asList("Fachinformatiker", "INKA", "VFA", "KFB", "FAMI", "Schreiner", "Elektroniker");
	List<Integer> ausbildungsjahre = Arrays.asList(1, 2, 3, 4);
	List<Integer> einsatzplanjahre = new ArrayList<Integer>();
	
	private Stage dialogStage;
    // Reference to the main application.
    private StartApp startApp;
	
    /**
     * Initialisiert die Controller-Klasse. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
	public void initialize(){
		  // Initialize the person table with the two columns.
		terminTable.setVisible(true);
        terminNameColumn.setCellValueFactory(cellData -> cellData.getValue().terminNameProperty());
        terminVonColumn.setCellValueFactory(cellData -> cellData.getValue().startDatumNameProperty());
        terminBisColumn.setCellValueFactory(cellData -> cellData.getValue().endDatumNameProperty());

		setEinsatzjahre();
		comboboxBeruf.getItems().addAll(ausbildungsberuf);
		comboboxEinsatzjahr.getItems().addAll(einsatzplanjahre);
		comboboxAusbildung.getItems().addAll(ausbildungsjahre);
	}
	
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
    /**
     * Setzt die Einsatzplanjahre in die Combobox. Immer -5 bis +5 Jahre des heutigen Jahres.
     */
	public void setEinsatzjahre(){
		int i, j = 0, k = -5;
		for(i = (LocalDate.now().minusYears(5).getYear()); i < LocalDate.now().plusYears(5).getYear(); i++, j++, k++){
			if((LocalDate.now().minusYears(5).getYear()) < LocalDate.now().getYear() ){
				einsatzplanjahre.add(j, (LocalDate.now().minusYears(k).getYear()));
			}else if(LocalDate.now().getYear() < LocalDate.now().plusYears(5).getYear()){
				einsatzplanjahre.add(j, LocalDate.now().plusYears(k).getYear());
			}else{
				einsatzplanjahre.add(j, LocalDate.now().getYear());
			}
		}
	}
	
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setStart(StartApp startApp) {
        this.startApp = startApp;

        // Add observable list data to the table
        terminTable.setItems(startApp.getTerminList());
    }
	
    /**
     * Öffnet ein neues Fenster, in welchem man Termine anlegen kann.
     */
	@FXML
	public void handleNewEntry(){
		try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("../view/TerminEditDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();
	        
	        // Create the dialog Stage.
	        dialogStage = new Stage();
	        dialogStage.setTitle("Neuer Eintrag");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);
	        
	        TerminEditController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        
	        dialogStage.showAndWait();
	        
		} catch (IOException e) {
	        e.printStackTrace();  
	    }
	}
	
	/**
	 * Löscht bei Auswahl einer Zeile einen Termin aus der Liste.
	 */
	@FXML
	private void handleDeleteTermin(){
		System.out.println(this + "  handleDeleteTermin");
		Termin termin = terminTable.getSelectionModel().getSelectedItem();
		int index = terminTable.getSelectionModel().getSelectedIndex();
		StartApp.removeTermin(index);
	}

    /**
     * Schließt die gesamte Anwendung.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}

package de.dpma.pumaz.control;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.dao.AzubiDAO;
import de.dpma.pumaz.dao.TerminConn;
import de.dpma.pumaz.model.Auszubildender;
import de.dpma.pumaz.model.Termin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootController {
	
	@FXML
	private ComboBox<String> comboboxBeruf;
	@FXML 
	private ComboBox<Integer> comboboxAusbildungsjahr = new ComboBox<Integer>();
	@FXML
	private ComboBox <Integer> comboboxEinsatzjahr;
	@FXML
    private TextField nameField;
	@FXML
	private TextField vornameField;	
	@FXML	
	private TableView<Termin> terminTable = new TableView<Termin>();
	@FXML
	private TableColumn<Termin, String> terminNameColumn;
	@FXML
	private TableColumn<Termin, String> terminVonColumn;
	@FXML
	private TableColumn<Termin, String> terminBisColumn;
	@FXML
	private TableColumn<Termin, Color> colorColumn;
	
	List<String> ausbildungsberuf = Arrays.asList("Fachinformatiker", "INKA", "VFA", "KFB", "FAMI", "Schreiner", "Elektroniker");
	List<Integer> ausbildungsjahre = Arrays.asList(1, 2, 3, 4);
	List<Integer> einsatzplanjahre = new ArrayList<Integer>();
	
	private Stage dialogStage;
    // Reference to the main application.
	private StartApp startApp;
    private TerminEditController controller;
	
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
        colorColumn.setCellValueFactory(cellData -> cellData.getValue().colorProperty());

		setEinsatzjahre();
		comboboxBeruf.getItems().addAll(ausbildungsberuf);
		comboboxEinsatzjahr.getItems().addAll(einsatzplanjahre);
		comboboxAusbildungsjahr.getItems().addAll(ausbildungsjahre);
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
     * Soll den Index zurückgeben, um eine Überwachung des Bearbeitungsfensters zu gewährleisten.
     * @return Zeile der Tabelle
     */
    public int getIndex(){
    	return terminTable.getSelectionModel().getSelectedIndex();
    }
	
    /**
     * Setzt die Einsatzplanjahre in die Combobox. Immer -5 bis +5 Jahre des heutigen Jahres.
     */
	private void setEinsatzjahre(){
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
     * @param startApp
     */
    public void setStart(StartApp startApp) {
        this.startApp = startApp;

        // Add observable list data to the table
        terminTable.setItems(startApp.getTerminList());
    }
    
    private Auszubildender getAzubi(){
    	Auszubildender azubi = null;
    	String name = nameField.getText();
    	String vorname = vornameField.getText();
    	Integer ausbildungsJahr = comboboxAusbildungsjahr.getValue();
    	String beruf = comboboxBeruf.getValue();
    	if(!name.equals("") && !vorname.equals("")){
    		azubi = new Auszubildender(name, vorname, ausbildungsJahr, beruf);
    	}
    	return azubi;
    }
    
    /**
     * Speichert die Einträge der Tabelle in der Datenbank.
     */
    @FXML
    private void saveTable(){
    	TerminConn tc = startApp.getTerminConn();
    	AzubiDAO aDAO = new AzubiDAO();
    	
    	if(getAzubi() != null){
    		aDAO.insertAzubi(getAzubi(), tc.getConnection());
    	}
    	
    	for (Termin termin : startApp.getTerminList()) {
    		String str = termin.getStartDatumName();
    		String str2 = termin.getEndDatumName();
    		termin.setStartDatum(str);
    		termin.getStartDatum();
    		termin.setEndDatum(str2);
    		termin.getEndDatum();
			tc.insertTermin(termin.getTerminName(), termin.getStartDatum(), termin.getEndDatum(), termin.getFarbe().toString());
		}
    	tc.anzahlTermin();
    }
    
	/**
	 * Beim Klicken des Knopfes werden die Termine aus der Tabelle in eine ArrayList gespeichert. Diese wird direkt in die 
	 * Tabelle übernommen.
	 */
    @FXML
    private void loadTable(){
    	TerminConn tc = startApp.getTerminConn();
    	tc.getDBTermin();
    }

    /**
     * Öffnet ein neues Fenster, in welchem man Termine anlegen kann.
     */
	@FXML
	private void handleNewEntry(){
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
	 * Bearbeitet bei Auswahl einer Zeile den Termin aus der Liste.
	 */
	@FXML
	private void handleEditTermin(){
		Termin termin = terminTable.getSelectionModel().getSelectedItem();
		int index = getIndex();
		if(index == -1){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Kein Termin ausgewählt");
			alert.setContentText("Bitte wählen Sie einen Termin zum Bearbeiten aus!");
			alert.showAndWait();
		}else{
			try {
		        // Load the fxml file and create a new stage for the popup dialog.
		        FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("../view/TerminEditDialog.fxml"));
		        AnchorPane root = (AnchorPane) loader.load();
		        
		        // Create the dialog Stage.
		        dialogStage = new Stage();
		        dialogStage.setTitle("Eintrag bearbeiten");
		        dialogStage.initModality(Modality.WINDOW_MODAL);
		        
		        controller = loader.getController();
		        controller.setEntries(index, termin);
		        controller.setDialogStage(dialogStage);
		        
		        Scene scene = new Scene(root);
		        dialogStage.setScene(scene);
		        dialogStage.showAndWait();
			} catch (IOException e) {
		        e.printStackTrace();  
		    }
		}
	}
	
	/**
	 * Löscht bei Auswahl einer Zeile einen Termin aus der Liste.
	 */
	@FXML
	private void handleDeleteTermin(){
		int index = getIndex();
		if(index != -1){
			StartApp.removeTermin(index);
		}else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Kein Termin ausgewählt");
			alert.setContentText("Bitte wählen Sie einen Termin zum Bearbeiten aus!");
			alert.showAndWait();
		}
	}

    /**
     * Schließt die gesamte Anwendung.
     */
    @FXML
    private void handleExit() {
    	TerminConn tc = startApp.getTerminConn();
    	tc.closeConnection(tc.getConnection());
        System.exit(0);
    }
}

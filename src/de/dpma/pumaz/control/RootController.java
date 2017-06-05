package de.dpma.pumaz.control;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.dao.AzubiDAO;
import de.dpma.pumaz.dao.TerminDAO;
import de.dpma.pumaz.model.Auszubildender;
import de.dpma.pumaz.model.Termin;
import de.dpma.pumaz.util.DataBaseConnection;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tab;
	@FXML
	private Tab tab2;
	
	private boolean edited = false;
    // Reference to the main application.
	private StartApp startApp;
	
    /**
     * Initialisiert die Dropdown-Felder, welche für die Erstellung eines Auszubildenden nötig sind.
     */
	@FXML
	public void initialize(){
		List<String> ausbildungsberuf = Arrays.asList("Fachinformatiker", "INKA", "VFA", "KFB", "FAMI", "Schreiner", "Elektroniker");
		List<Integer> ausbildungsjahre = Arrays.asList(1, 2, 3, 4);
		
		// Initialize the person table with the two columns.
		terminTable.setVisible(true);
        terminNameColumn.setCellValueFactory(cellData -> cellData.getValue().terminNameProperty());
        terminVonColumn.setCellValueFactory(cellData -> cellData.getValue().startDatumNameProperty());
        terminBisColumn.setCellValueFactory(cellData -> cellData.getValue().endDatumNameProperty());
        colorColumn.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        
		comboboxBeruf.getItems().addAll(ausbildungsberuf);
		comboboxEinsatzjahr.getItems().addAll(einsatzjahrList());
		comboboxAusbildungsjahr.getItems().addAll(ausbildungsjahre);
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
	private List<Integer> einsatzjahrList(){
		List<Integer> einsatzplanjahre = new ArrayList<Integer>();
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
		return einsatzplanjahre;
	}
	
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param startApp
     */
    public void setStart(StartApp startApp) {
    	
    	tab2.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if (tab2.isSelected()) {
					startApp.getPrimaryStage().setResizable(true);
				}else{
					startApp.getPrimaryStage().setHeight(370);
					startApp.getPrimaryStage().setWidth(500);
					startApp.getPrimaryStage().setResizable(false);
				}
			}
		});
    	
        this.startApp = startApp;
        // Add observable list data to the table
        terminTable.setItems(StartApp.getTerminList());
    }
    
    /**
     * Gibt einen Auszubildenden zum Speichern für die Datenbank zurück, wobei die einzelnen Werte hierbei 
     * auf der ersten Seite erstellt wurden.
     * @return Auszubildenden
     */
    private Auszubildender getAzubi(){
    	Auszubildender azubi = null;
    	String name = nameField.getText();
    	String vorname = vornameField.getText();
    	Integer ausbildungsJahr = comboboxAusbildungsjahr.getValue();
    	String beruf = comboboxBeruf.getValue();
    	if(!name.equals("") && !vorname.equals("")){
    		azubi = new Auszubildender(name, vorname, ausbildungsJahr, beruf);
    	}
    	else{
    		Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Kein Auszubildender ausgewählt");
			alert.setContentText("Bitte geben Sie Informationen zu einem Auszubildenden an!");
			alert.showAndWait();
    	}
    	return azubi;
    }
    
    /**
     * Speichert die Einträge der Termin-Tabelle und den entsprechenden Auszubildenden in der Datenbank.
     */
    @FXML
    private void saveTable(){
    	DataBaseConnection dbc = new DataBaseConnection();
		Connection conn = dbc.establishConnection();
		TerminDAO tc = new TerminDAO();
    	AzubiDAO aDAO = new AzubiDAO();
    	int azubi_id = 0;
    	
    	if(getAzubi() != null && edited == false){
    		aDAO.insertAzubi(getAzubi(), conn);
    		azubi_id = aDAO.getAzubiID();
    	}
    	
    	for (Termin termin : StartApp.getTerminList()) {
    		String str = termin.getStartDatumName();
    		String str2 = termin.getEndDatumName();
    		
    		termin.setStartDatum(str);
    		termin.getStartDatum();
    		termin.setEndDatum(str2);
    		termin.getEndDatum();
    		
    		if(edited == false){
    			tc.insertTermin(termin.getTerminName(), termin.getStartDatum(), termin.getEndDatum(), termin.getFarbe().toString(), azubi_id, conn);
    		}else{
    			if(termin.getTermin_id() != 0)
    			tc.updateTermin(termin, conn);
    			else
    			tc.insertTermin(termin.getTerminName(), termin.getStartDatum(), termin.getEndDatum(), termin.getFarbe().toString(), azubi_id, conn);
    		}
		}
    	setEdited(false);
    	tc.anzahlTermin(conn);
    	dbc.closeConnection(conn);
    }
    
	/**
	 * Beim Klicken des Knopfes werden die Termine aus der Tabelle in eine ArrayList gespeichert. Diese wird direkt in die 
	 * Tabelle übernommen.
	 */
    @FXML
    private void loadTable(){
    	Stage loadStage;
    	try{
    	
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/LoadAzubiDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        // Create the dialog Stage.
        loadStage = new Stage();
        loadStage.setTitle("Neuer Eintrag");
        loadStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        loadStage.setScene(scene);
        
        LoadAzubiController controller = loader.getController();
        controller.setDialogStage(loadStage);
        controller.setRoot(this);
        
        terminTable.getItems().clear();
        nameField.clear();
        vornameField.clear();
        comboboxAusbildungsjahr.getSelectionModel().clearSelection();
        
        loadStage.showAndWait();
    	} catch(IOException e){
    		e.printStackTrace();
    	}
    }

    /**
     * Öffnet ein neues Fenster, in welchem man Termine anlegen kann.
     */
	@FXML
	private void handleNewEntry(){
		Stage dialogStage;
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
		Stage dialogStage;
		
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
		        
		        TerminEditController controller = loader.getController();
		        
		        controller.setEntries(index, termin);
		        controller.setDialogStage(dialogStage);
		        controller.setRoot(this);
		        controller.setTermin_ID(termin.getTermin_id());
		        
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
        System.exit(0);
    }
    
    /**
     * Setzt die Informationen eines Auszubildenden in die freien Felder hinein, nachdem man in {@link LoadAzubiController}
     * den Bestätigen-Knopf gedrückt hat.
     * @param azubi
     */
    public void setAzubiInfo(Auszubildender azubi){
    	nameField.setText(azubi.getNachname());
    	vornameField.setText(azubi.getVorname());
    	comboboxBeruf.setValue(azubi.getAusbildungsberuf());
    	comboboxAusbildungsjahr.setValue(azubi.getLehrjahr());
    }
    
    public void setEdited(boolean edit){
    	edited = edit;
    }
    
    public boolean isEdited(){
    	return edited;
    }
}

package de.dpma.pumaz.control;

import java.util.ArrayList;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.model.Termin;
import de.dpma.pumaz.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TerminEditController {
	
	@FXML
	private TextField terminNameField = new TextField();
	@FXML
	private DatePicker datePickerVon = new DatePicker();
	@FXML
	private DatePicker datePickerBis = new DatePicker();
	@FXML
	private ColorPicker colorPicker = new ColorPicker(Color.ALICEBLUE);
	@FXML
	private GridPane gridPane = new GridPane();
	private ArrayList<DatePicker> datePickerListVon = new ArrayList<DatePicker>();
	private ArrayList<DatePicker> datePickerListBis = new ArrayList<DatePicker>();
	private RootController rc;
	
	private int index = -1;
	private Stage stage;
//	private int rows = 1;
	private int counter = 0;
	private int termin_id = 0;
	
    /**
     * Initialisiert die Controller-Klasse. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
	
    /**
     * Setze den Status des Dialogs.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.stage = dialogStage;
    }

    /**
     * Legt einen neuen Termin an, solange der Terminname und beide Daten vorhanden sind. Außerdem darf das Enddatum nicht weiter zurückliegen,
     * als das Startdatum. Ansonsten gibt es eine Fehlermeldung.
     */
    @FXML
    private void handleSubmit(){
    	if(index == -1){
    		if(terminNameField.getText() == null || terminNameField.getText().equals("")){
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Terminname fehlt");
    		alert.setContentText("Sie haben keinen Terminnamen angegeben. Bitte fügen Sie einen hinzu.");
    		alert.showAndWait();
	    	}
    		if(datePickerVon.getValue() == null || datePickerBis.getValue() == null){
			Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Datum fehlt");
    		alert.setContentText("Sie haben kein Datum angegeben. Bitte fügen Sie eines hinzu.");
    		alert.showAndWait();
    		}
    		else if(datePickerVon.getValue().isAfter(datePickerBis.getValue())) {
			Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Bitte Datum korrigieren.");
    		alert.setContentText("Ihr Enddatum liegt weiter in der Vergangenheit als ihr Startdatum. Bitte korrigieren Sie das.");
    		alert.showAndWait();
    		}else{
	    		if(counter == 0){
	    		StartApp.addTermin(new Termin(terminNameField.getText(), datePickerVon.getValue(), datePickerBis.getValue(), colorPicker.getValue()));
	    		}else{
	    			StartApp.addTermin(new Termin(terminNameField.getText(), datePickerVon.getValue(), datePickerBis.getValue(), colorPicker.getValue()));
	    			for(int i = 0; i < datePickerListVon.size(); i++){
	    				StartApp.addTermin(new Termin(terminNameField.getText(), datePickerListVon.get(i).getValue(), datePickerListBis.get(i).getValue(), colorPicker.getValue()));
	    			}
	    		}
	    		stage.close();
	    	}
    	}else{
    		rc.setEdited(true);
    		StartApp.replaceTermin(new Termin(terminNameField.getText(), datePickerVon.getValue(), datePickerBis.getValue(), colorPicker.getValue(), termin_id), index);
    		stage.close();
    	} 
    }
    
    /**
     * Setzt die Einträge zum Bearbeiten in das Termin-Edit-Fenster
     * @param index
     * @param termin
     */
    public void setEntries(int index, Termin termin){
    	if(termin == null ){
    		System.out.println("Kein Objekt von Termin: ");
    	}else{
    		this.index = index;
    		System.out.println(this + " " + index);
    		terminNameField.setText(termin.getTerminName());
    		datePickerVon.setValue(DateUtil.parse(termin.getStartDatumName()));
    		datePickerBis.setValue(DateUtil.parse(termin.getEndDatumName()));
    		colorPicker.setValue(termin.getFarbe());
    	}
    }
    
    /**
     * Fügt der Oberfläche weitere DatePicker hinzu und vergrößert gleichzeitig das Fenster.
     */
	@FXML
	public void addDatePicker() {
		int columns = 0;
		int rows = gridPane.getChildren().size()/2;
		System.out.println("Row: " + rows);
		stage.setHeight(stage.getHeight()+30);
		DatePicker dpV = new DatePicker();
		DatePicker dpB = new DatePicker();
		datePickerListVon.add(dpV);
		datePickerListBis.add(dpB);
		gridPane.add(datePickerListVon.get(counter), columns, rows);
		columns++;
		gridPane.add(datePickerListBis.get(counter), columns, rows);
		++counter;
		System.out.println("Counter " + counter);
	}
	
	/**
	 * Entfernt die per addDatePicker in {@link TerminEditController} hinzugefügten DatePicker. 
	 */
	@FXML
	public void removeDatePicker() {

		if(gridPane.getChildren().size() == 2){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Mindestanzahl verbleibend");
			alert.setContentText("Es müssen mindestens zwei DatePicker verbleiben.");
			alert.showAndWait();
		}else{	
			gridPane.getChildren().remove(datePickerListVon.size());
			gridPane.getChildren().remove(datePickerListBis.size());
			System.out.println("Jetzt ist es richtig?");
			datePickerListVon.remove(datePickerListVon.size());
			datePickerListBis.remove(datePickerListBis.size());
			stage.setHeight(stage.getHeight()-30);
		}
	}

    /**
     * Schließt das Dialogfenster.
     */
    @FXML
    private void handleExit() {
    	stage.close();
    }
    
    public void setRoot(RootController rc){
    	this.rc = rc;
    }
    
    public void setTermin_ID(int id){
    	this.termin_id = id;
    }
    
    public int getIndex(){
    	return index;
    }
    
    
}

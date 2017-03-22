package de.dpma.pumaz.control;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.model.Termin;
import de.dpma.pumaz.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TerminEditController {
	
	@FXML
	private TextField terminNameField = new TextField("sdfgfdss");
	@FXML
	private DatePicker datePickerVon = new DatePicker();
	@FXML
	private DatePicker datePickerBis = new DatePicker();
	@FXML
	private ColorPicker colorPicker = new ColorPicker(Color.ALICEBLUE);
	
	private int index = -1;
	private Stage stage;
	
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
     * Legt einen neuen Termin an.
     */
    @FXML
    private void handleSubmit(){
    	if(index == -1){
    		if(terminNameField.getText() == null || terminNameField.getText().equals("")){
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setHeaderText("Terminname fehlt");
    		alert.setContentText("Sie haben keinen Terminnamen angegeben. Bitte fügen Sie einen hinzu.");
    		alert.showAndWait();
	    	}else{
	    		System.out.println(index);
	    		StartApp.addTermin(new Termin(terminNameField.getText(), datePickerVon.getValue(), datePickerBis.getValue(), colorPicker.getValue()));
	    		stage.close();
	    	}
    	}else{
    		System.out.println(this + " " + index);
    		StartApp.replaceTermin(new Termin(terminNameField.getText(), datePickerVon.getValue(), datePickerBis.getValue(), colorPicker.getValue()), index);
    		stage.close();
    	} 
    }
    
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
     * Schließt das Dialogfenster.
     */
    @FXML
    private void handleExit() {
    	stage.close();
    }
}

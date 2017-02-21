package de.dpma.pumaz.control;

import de.dpma.pumaz.StartApp;
import de.dpma.pumaz.model.Termin;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TerminEditController {
	
	@FXML
	private TextField terminNameField;
	@FXML
	private DatePicker datePickerVon;
	@FXML
	private DatePicker datePickerBis;
	
	private Stage stage;
	
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
	
    /**
     * Sets the stage of this dialog.
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
    	System.out.println(terminNameField.getText());
    	System.out.println(datePickerVon.getValue());
    	System.out.println(datePickerBis.getValue());
    	StartApp.addTermin(new Termin(terminNameField.getText(), datePickerVon.getValue(), datePickerBis.getValue()));
    	
    }

    /**
     * Schließt die Anwendung
     */
    @FXML
    private void handleExit() {
    	stage.close();
    }
}

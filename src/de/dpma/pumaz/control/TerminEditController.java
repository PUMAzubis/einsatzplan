package de.dpma.pumaz.control;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TerminEditController {
	
	@FXML
	private TextField terminNameField;
	
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
     * Closes the application.
     */
    @FXML
    private void handleExit() {
    	stage.close();
    }
}

package de.dpma.pumaz.control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class RootController {
	
	@FXML
	private ComboBox<String> comboboxBeruf;
	@FXML 
	private ComboBox<Integer> comboboxAusbildung;
	@FXML
	private ComboBox <Integer> comboboxEinsatzjahr;
	List<String> ausbildungsberuf = Arrays.asList("Fachinformatiker", "INKA", "VFA", "KFB", "FAMI", "Schreiner", "Elektroniker");
	List<Integer> ausbildungsjahre = Arrays.asList(1, 2, 3, 4);
	List<Integer> einsatzplanjahre = new ArrayList<Integer>();
	
	@FXML
	public void initialize(){
		setEinsatzjahre();
		comboboxBeruf.getItems().addAll(ausbildungsberuf);
		comboboxEinsatzjahr.getItems().addAll(einsatzplanjahre);
		comboboxAusbildung.getItems().addAll(ausbildungsjahre);
	}
	
	public void setEinsatzjahre(){
		int i, j = 0, k = -5;
		for(i = (LocalDate.now().minusYears(5).getYear()); i < LocalDate.now().plusYears(5).getYear(); i++, j++, k++){
			System.out.println(i + " ter Durchgang");
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
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}

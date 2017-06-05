package de.dpma.pumaz.model;

import java.time.LocalDate;

import de.dpma.pumaz.util.DateUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class Termin {

	private int termin_id;
	private ObjectProperty<String> terminName = new SimpleObjectProperty<String>();
	private ObjectProperty<String> startDatumName = new SimpleObjectProperty<String>();
	private ObjectProperty<String> endDatumName = new SimpleObjectProperty<String>();
	private ObjectProperty<LocalDate> startDatum = new SimpleObjectProperty<LocalDate>();
	private ObjectProperty<LocalDate> endDatum = new SimpleObjectProperty<LocalDate>();
	private ObjectProperty<Color> farbe = new SimpleObjectProperty<Color>();

	/**
	 * Konstruktor zum Erstellen eines Termins bevor er in die Datenbank gespeichert wird.
	 * @param terminName
	 * @param localDate1
	 * @param localdate2
	 * @param color
	 */
	public Termin(String terminName, LocalDate localDate1, LocalDate localdate2, Color color){
		setTerminName(terminName);
		setStartDatumName(localDate1);
		setEndDatumName(localdate2);
		setFarbe(color);
	}
	
	/**
	 * Konstruktor zum Erstellen eines Termin, nach dem Laden aus der Datenbank.
	 * @param terminName
	 * @param localDate1
	 * @param localdate2
	 * @param color
	 * @param id - Wird aus der Datenbank von TERMIN_ID übergeben, um den Termin in der Datenbank
	 * einwandfrei zu identifizieren.
	 */
	public Termin(String terminName, LocalDate localDate1, LocalDate localdate2, Color color, int id){
		setTerminName(terminName);
		setStartDatumName(localDate1);
		setEndDatumName(localdate2);
		setFarbe(color);
		setTermin_id(id);
	}

	/***************************************************************************************
	 * Getter für den Terminnamen
	 */
	public String getTerminName(){
		return terminName.get();
	}

	public void setTerminName(String terminName) {
		this.terminName.set(terminName);
	}
	
	public ObjectProperty<String> terminNameProperty() {
		return terminName;
	}
	
	/***************************************************************************************
     * Methoden, um den Startwert als ObjectProperty<LocalDate> zu setzen
     */
	public LocalDate getStartDatum() {
        return startDatum.get();
    }

    public void setStartDatum(String startString) {
        this.startDatum.set((DateUtil.parse(startString)));
    }
    
    public ObjectProperty<LocalDate> startDatumProperty() {
        return startDatum;
    }
    
	/***************************************************************************************
     * Methoden, um den Startwert als ObjectProperty<String> zu setzen
     */
	public ObjectProperty<String> startDatumNameProperty() {
		return startDatumName;
	}
	
	public String getStartDatumName(){
		return startDatumName.get();
	}

	public void setStartDatumName(LocalDate startDatumName) {
		this.startDatumName.set((DateUtil.format(startDatumName)));
	}
    
	/***************************************************************************************
     * Methoden, um den Endwert als ObjectProperty<LocalDate> zu setzen
     */
    public LocalDate getEndDatum() {
        return endDatum.get();
    }

    public void setEndDatum(String endString) {
        this.endDatum.set((DateUtil.parse(endString)));
    }

    public ObjectProperty<LocalDate> endDatumProperty() {
        return endDatum;
    }
    
	/***************************************************************************************
     * Methoden, um den Startwert als ObjectProperty<String> zu setzen
     */
    public ObjectProperty<String> endDatumNameProperty() {
		return endDatumName;
	}

	public void setEndDatumName(LocalDate endDatumName) {
		this.endDatumName.set((DateUtil.format(endDatumName)));
	}
	
	public String getEndDatumName(){
		return endDatumName.get();
	}
	
	/***************************************************************************************
     * Methoden, um die Farbe als ObjectProperty<Color> zu setzen
     */
	public ObjectProperty<Color> colorProperty(){
		return farbe;
	}

	public Color getFarbe() {
		return farbe.get();
	}

	public void setFarbe(Color farbe) {
		this.farbe.set(farbe);
	}

	/**
	 * 
	 */
	public int getTermin_id() {
		return termin_id;
	}

	public void setTermin_id(int termin_id){
		this.termin_id = termin_id;
	}
    
}

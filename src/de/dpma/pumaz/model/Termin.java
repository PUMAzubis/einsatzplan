package de.dpma.pumaz.model;

import java.time.LocalDate;

import de.dpma.pumaz.util.DateUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class Termin {

	private ObjectProperty<String> terminName = new SimpleObjectProperty<String>();
	private ObjectProperty<String> startDatumName = new SimpleObjectProperty<String>();
	private ObjectProperty<String> endDatumName = new SimpleObjectProperty<String>();
	private ObjectProperty<LocalDate> startDatum = null;
	private ObjectProperty<LocalDate> endDatum = null;
	private ObjectProperty<Color> farbe = new SimpleObjectProperty<Color>();
	
	public Termin(){
		this(null);
	}
	
	public Termin(String terminName){
		this.terminName = new SimpleObjectProperty<String>(terminName);
		setStartDatumName(LocalDate.of(2012, 2, 21));
		setEndDatumName(LocalDate.of(2014, 11, 1));
		this.startDatumName = startDatumNameProperty();
		this.endDatumName = endDatumNameProperty();
	}
	
	public Termin(String terminName, LocalDate localDate1, LocalDate localdate2){
		this.terminName = new SimpleObjectProperty<String>(terminName);
		setStartDatumName(localDate1);
		this.startDatumName = startDatumNameProperty();
		setEndDatumName(localdate2);
		this.endDatumName = endDatumNameProperty();
	}
	
	public Termin(String terminName, LocalDate localDate1, LocalDate localdate2, Color color){
		this.terminName = new SimpleObjectProperty<String>(terminName);
		setStartDatumName(localDate1);
		this.startDatumName = startDatumNameProperty();
		setEndDatumName(localdate2);
		this.endDatumName = endDatumNameProperty();
		setFarbe(color);
		this.farbe = colorProperty();
	}

	public String getTerminName(){
		return terminName.get();
	}

	public void setTerminName(String terminName) {
		this.terminName.set(terminName);
	}
	
	public ObjectProperty<String> terminNameProperty() {
		return terminName;
	}
	
	public LocalDate getStartDatum() {
        return startDatum.get();
    }

    public void setStartDatum(LocalDate startDatum) {
        this.startDatum.set(startDatum);
    }

    public ObjectProperty<LocalDate> startDatumProperty() {
        return startDatum;
    }
    
    public LocalDate getEndDatum() {
        return endDatum.get();
    }

    public void setEndDatum(LocalDate endDatum) {
        this.endDatum.set(endDatum);
    }

    public ObjectProperty<LocalDate> endDatumProperty() {
        return endDatum;
    }

	public ObjectProperty<String> startDatumNameProperty() {
		return startDatumName;
	}
	
	public String getStartDatumName(){
		return startDatumName.get();
	}

	public void setStartDatumName(LocalDate startDatumName) {
		this.startDatumName.set((DateUtil.format(startDatumName)));
	}

	public ObjectProperty<String> endDatumNameProperty() {
		return endDatumName;
	}

	public void setEndDatumName(LocalDate endDatumName) {
		this.endDatumName.set((DateUtil.format(endDatumName)));
	}
	
	public String getEndDatumName(){
		return endDatumName.get();
	}
	
	public ObjectProperty<Color> colorProperty(){
		return farbe;
	}

	public Color getFarbe() {
		return farbe.get();
	}

	public void setFarbe(Color farbe) {
		this.farbe.set(farbe);
	}
    
}

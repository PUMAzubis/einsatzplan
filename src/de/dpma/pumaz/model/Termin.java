package de.dpma.pumaz.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public class Termin {

	private StringProperty terminName;
	private final ObjectProperty<LocalDate> startDatum = null;
	private final ObjectProperty<LocalDate> endDatum = null;

	public String getTerminName(){
		return terminName.get();
	}

	public void setTerminName(StringProperty terminName) {
		this.terminName = terminName;
	}
	
	public StringProperty terminNameProperty() {
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
	
}

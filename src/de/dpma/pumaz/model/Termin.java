package de.dpma.pumaz.model;

import java.time.LocalDate;

import de.dpma.pumaz.util.DateUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Termin {

	private ObjectProperty<String> terminName = new SimpleObjectProperty<String>();
	private ObjectProperty<String> startDatumName = new SimpleObjectProperty<String>();
	private ObjectProperty<String> endDatumName = new SimpleObjectProperty<String>();
	private ObjectProperty<LocalDate> startDatum = null;
	private ObjectProperty<LocalDate> endDatum = null;
	
	public Termin(){
		this(null);
	}
	
	public Termin(String terminName){
		this.terminName = new SimpleObjectProperty<String>(terminName);
//		this.startDatum = new SimpleObjectProperty<LocalDate>(LocalDate.of(2012, 2, 21));
//		this.endDatum = new SimpleObjectProperty<LocalDate>(LocalDate.of(2016, 2, 21));
		setStartDatumName(LocalDate.of(2012, 2, 21));
		setEndDatumName(LocalDate.of(2014, 11, 1));
		System.out.println(this + " " + getStartDatumName());
		System.out.println(this + " " + getEndDatumName());
		this.startDatumName = startDatumNameProperty();
		this.endDatumName = endDatumNameProperty();
	}
	
	public Termin(String terminName, LocalDate localDate1, LocalDate localdate2){
//		setTerminName(terminName);
//		System.out.println(getTerminName());
//		this.terminName = terminNameProperty();
		this.terminName = new SimpleObjectProperty<String>(terminName);
		System.out.println(getTerminName());
		setStartDatumName(localDate1);
		System.out.println(getStartDatumName());
		this.startDatumName = startDatumNameProperty();
		setEndDatumName(localdate2);
		System.out.println(getEndDatumName());
		this.endDatumName = endDatumNameProperty();		
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
    
}

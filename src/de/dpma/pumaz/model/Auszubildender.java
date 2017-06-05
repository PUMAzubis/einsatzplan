package de.dpma.pumaz.model;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Auszubildender {

	private int id = 0;
	private StringProperty vorname = new SimpleStringProperty();
    private StringProperty nachname = new SimpleStringProperty();
    private int lehrjahr = 0;
    private StringProperty ausbildungsberuf = new SimpleStringProperty();
    private ObjectProperty<LocalDate> einsatzplanjahr = null;
    
    /**
     * Konstruktor zum Erstellen eines Auszubildenden vor dem Einfügen in die Datenbank.
     * @param name
     * @param vorname
     * @param lehrjahr
     * @param ausbildungsberuf
     */
    public Auszubildender(String name, String vorname, int lehrjahr, String ausbildungsberuf){
    	setFirstName(vorname);
    	this.vorname = vornameProperty();
    	setNachname(name);
    	this.nachname = nachnameProperty();
    	setLehrjahr(lehrjahr);
    	this.lehrjahr = getLehrjahr();
    	setAusbildungsberuf(ausbildungsberuf);
    	this.ausbildungsberuf = ausbildungsBerufProperty();
    }
    
    /**
     * Konstruktor zum Erstellen eines Auszubildenden, der aus der Datenbank herausgeholt wurde.
     * @param id - Ist die AZUBI_ID aus der Datenbank, welche mit Autoincrement erstellt wurde, um den Auszubildenden
     * einwandfrei zu identifizieren.
     * @param name
     * @param vorname
     * @param lehrjahr
     * @param ausbildungsberuf
     */
    public Auszubildender(int id, String name, String vorname, int lehrjahr, String ausbildungsberuf){
    	setAzubiId(id);
    	setFirstName(vorname);
    	this.vorname = vornameProperty();
    	setNachname(name);
    	this.nachname = nachnameProperty();
    	setLehrjahr(lehrjahr);
    	this.lehrjahr = getLehrjahr();
    	setAusbildungsberuf(ausbildungsberuf);
    	this.ausbildungsberuf = ausbildungsBerufProperty();
    }
    
    public String getVorname() {
        return vorname.get();
    }

    public void setFirstName(String firstName) {
        this.vorname.set(firstName);
    }

    public StringProperty vornameProperty() {
        return vorname;
    }
    
    public String getNachname() {
        return nachname.get();
    }

    public void setNachname(String lastName) {
        this.nachname.set(lastName);
    }

    public StringProperty nachnameProperty() {
        return nachname;
    }
    
    public LocalDate getEinsatzplanjahr() {
        return einsatzplanjahr.get();
    }

    public void setEinsatzplanjahr(LocalDate einsatzplanjahr) {
        this.einsatzplanjahr.set(einsatzplanjahr);
    }

    public ObjectProperty<LocalDate> einsatzplanjahrProperty() {
        return einsatzplanjahr;
    }

	public int getLehrjahr() {
		return lehrjahr;
	}
	
	public void setLehrjahr(int lehrjahr) {
        this.lehrjahr = lehrjahr;
    }

	public StringProperty ausbildungsBerufProperty() {
		return ausbildungsberuf;
	}
	
	public void setAusbildungsberuf(String ausbildungsberuf){
		this.ausbildungsberuf.set(ausbildungsberuf);
	}
	
	public String getAusbildungsberuf(){
		return ausbildungsberuf.get();
	}
	
	public void setAzubiId(int id){
		this.id = id;
	}
	
	public int getAzubiId(){
		return id;
	}
    
    
    
}

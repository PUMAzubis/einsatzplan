package de.dpma.pumaz.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Auszubildender {

	private int id = 0;
	private StringProperty vorname = new SimpleStringProperty();
    private StringProperty nachname = new SimpleStringProperty();
    private int lehrjahr = 0;
    private StringProperty ausbildungsberuf = new SimpleStringProperty();
    private int einsatzplanjahr = 0;
    
    /**
     * Konstruktor zum Erstellen eines Auszubildenden vor dem Einfügen in die Datenbank.
     * @param name
     * @param vorname
     * @param lehrjahr
     * @param ausbildungsberuf
     * @param einsatzplanjahr
     */
    public Auszubildender(String name, String vorname, int lehrjahr, String ausbildungsberuf, int einsatzjahr){
    	setFirstName(vorname);
    	this.vorname = vornameProperty();
    	setNachname(name);
    	this.nachname = nachnameProperty();
    	setLehrjahr(lehrjahr);
    	this.lehrjahr = getLehrjahr();
    	setAusbildungsberuf(ausbildungsberuf);
    	this.ausbildungsberuf = ausbildungsBerufProperty();
    	setEinsatzplanjahr(einsatzjahr);
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
    public Auszubildender(int id, String name, String vorname, int lehrjahr, String ausbildungsberuf, int einsatzjahr){
    	setAzubiId(id);
    	setFirstName(vorname);
    	this.vorname = vornameProperty();
    	setNachname(name);
    	this.nachname = nachnameProperty();
    	setLehrjahr(lehrjahr);
    	this.lehrjahr = getLehrjahr();
    	setAusbildungsberuf(ausbildungsberuf);
    	this.ausbildungsberuf = ausbildungsBerufProperty();
    	setEinsatzplanjahr(einsatzjahr);
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
    
    public int getEinsatzplanjahr() {
        return einsatzplanjahr;
    }

    public void setEinsatzplanjahr(int einsatzplanjahr) {
        this.einsatzplanjahr = einsatzplanjahr;
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

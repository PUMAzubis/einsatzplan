package de.dpma.pumaz.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public class Auszubildender {

	private final StringProperty vorname = null;
    private final StringProperty nachname = null;
    private final IntegerProperty ausbildungsjahr = null;
    private final ObjectProperty<LocalDate> einsatzplanjahr = null;
    
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
    
    public Integer getAusbildungsjahr() {
        return ausbildungsjahr.get();
    }

    public void setAusbildungsjahr(Integer ausbildungsjahr) {
        this.ausbildungsjahr.set(ausbildungsjahr);
    }

    public IntegerProperty ausbildungsjahrProperty() {
        return ausbildungsjahr;
    }
    
    
}

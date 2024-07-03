package com.managereventi.managereventi.model.mo;

import java.util.Date;

public class Candidature {

    private int Id;
    private String Nome;
    private String Cognome;
    private String Email;
    private String Telefono;
    private Date DataNascita;
    private String Citta;
    private String Posizione;
    private String Descrizione;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public Date getDataNascita() {
        return DataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        DataNascita = dataNascita;
    }

    public String getCitta() {
        return Citta;
    }

    public void setCitta(String citta) {
        Citta = citta;
    }

    public String getPosizione() {
        return Posizione;
    }

    public void setPosizione(String posizione) {
        Posizione = posizione;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public void setDataNascita(String dataNascita) {
    }
}

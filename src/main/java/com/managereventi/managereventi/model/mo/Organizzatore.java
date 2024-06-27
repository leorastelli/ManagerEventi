package com.managereventi.managereventi.model.mo;

public class Organizzatore {
    private String idOrganizzatore;
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private String codiceAutorizzazione;

    // Metodi getter e setter per ogni campo
    public String getIdOrganizzatore() {
        return idOrganizzatore;
    }

    public void setIdOrganizzatore(String idOrganizzatore) {
        this.idOrganizzatore = idOrganizzatore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceAutorizzazione() {
        return codiceAutorizzazione;
    }

    public void setCodiceAutorizzazione(String codiceAutorizzazione) {
        this.codiceAutorizzazione = codiceAutorizzazione;
    }


}
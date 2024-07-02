package com.managereventi.managereventi.model.mo;

public class Newsletter {
    private Utente utente;
    private String email;

    // Metodi getter e setter per ogni campo
    public Utente getIdUtente() {
        return utente;
    }

    public void setIdUtente(Utente utente) {
        this.utente = utente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

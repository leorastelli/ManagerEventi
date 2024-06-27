package com.managereventi.managereventi.model.mo;

public class Newsletter {
    private Utente utente;
    private Evento evento;

    // Metodi getter e setter per ogni campo
    public Utente getIdUtente() {
        return utente;
    }

    public void setIdUtente(Utente utente) {
        this.utente = utente;
    }

    public Evento getIdEvento() {
        return evento;
    }

    public void setIdEvento(Evento evento) {
        this.evento = evento;
    }

}

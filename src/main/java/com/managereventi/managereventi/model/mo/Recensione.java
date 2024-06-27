package com.managereventi.managereventi.model.mo;

public class Recensione {
    private String idRecensione;
    private String descrizione;
    private Integer stelle;
    private Utente utente;
    private Esibizione esibizione;
    private Evento evento;


    // Metodi getter e setter per ogni campo
    public String getIdRecensione() {
        return idRecensione;
    }

    public void setIdRecensione(String idRecensione) {
        this.idRecensione = idRecensione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getStelle() {
        return stelle;
    }

    public void setStelle(Integer stelle) {
        this.stelle = stelle;
    }

    public Utente getIdUtente() {
        return utente;
    }

    public void setIdUtente(Utente utente) {
        this.utente = utente;
    }

    public Esibizione getIdEsibizione() {
        return esibizione;
    }

    public void setIdEsibizione(Esibizione esibizione) {
        this.esibizione = esibizione;
    }

    public Evento getIdEvento() {
        return evento;
    }

    public void setIdEvento(Evento evento) {
        this.evento = evento;
    }
}

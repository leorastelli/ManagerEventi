package com.managereventi.managereventi.model.mo;


public class Biglietto {
    private String idBiglietto;
    private Long prezzo;
    private String tipo;
    private Integer stato;
    private Utente utente;
    private Esibizione esibizione;
    private Evento evento;

    private Integer Posto;


    // Metodi getter e setter per ogni campo
    public String getIdBiglietto() {
        return idBiglietto;
    }

    public void setIdBiglietto(String idBiglietto) {
        this.idBiglietto = idBiglietto;
    }

    public Long getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Long prezzo) {
        this.prezzo = prezzo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getStato() {
        return stato;
    }

    public void setStato(Integer stato) {
        this.stato = stato;
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

    public Integer getPosto() {
        return Posto;
    }

    public void setPosto(Integer posto) {
        Posto = posto;
    }
}
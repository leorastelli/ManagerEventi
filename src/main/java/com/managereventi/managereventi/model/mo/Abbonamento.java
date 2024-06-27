package com.managereventi.managereventi.model.mo;


public class Abbonamento {
    private String idAbbonamento;
    private Long prezzo;
    private String tipo;
    private Integer entrate;
    private Utente utente;
    private Evento evento;



    // Metodi getter e setter per ogni campo
    public String getIdAbbonamento() {
        return idAbbonamento;
    }

    public void setIdAbbonamento(String idAbbonamento) {
        this.idAbbonamento = idAbbonamento;
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

    public Integer getEntrate() {
        return entrate;
    }

    public void setEntrate(Integer entrate) {
        this.entrate = entrate;
    }

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

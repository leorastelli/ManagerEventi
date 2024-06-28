package com.managereventi.managereventi.model.mo;

import java.sql.Date;

public class Evento {
    private String idEvento;
    private String nome;
    private String descrizione;
    private Date dataInizio;
    private Date dataFine;
    private Integer numEsibizioni;
    private Organizzatore organizzatore;
    private Esibizione[] esibizioni;

    // Metodi getter e setter per ogni campo
    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public Integer getNumEsibizioni() {
        return numEsibizioni;
    }

    public void setNumEsibizioni(Integer numEsibizioni) {
        this.numEsibizioni = numEsibizioni;
    }

    public Organizzatore getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(Organizzatore organizzatore) {
        this.organizzatore = organizzatore;
    }

    public Esibizione[] getEsibizioni() {
        return esibizioni;
    }

    public void setEsibizioni(Esibizione[] esibizioni) {
        this.esibizioni = esibizioni;
    }
}


package com.managereventi.managereventi.model.mo;

import java.sql.Blob;
import java.sql.Time;
import java.sql.Date;

public class Esibizione {
    private String idEsibizione;
    private String nome;
    private String descrizione;
    private Time durata;
    private Time oraInizio;
    private String genere;
    private Integer numeroArtisti;
    private Organizzatore organizzatore;
    private Evento evento;
    private Luogo luogo;
    private Integer postiDisponibili;
    private Blob immagine;
    private Date DataEsibizione;



    // Metodi getter e setter per ogni campo
    public String getIdEsibizione() {
        return idEsibizione;
    }

    public void setIdEsibizione(String idEsibizione) {
        this.idEsibizione = idEsibizione;
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

    public Time getDurata() {
        return durata;
    }

    public void setDurata(Time durata) {
        this.durata = durata;
    }

    public Time getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Time oraInizio) {
        this.oraInizio = oraInizio;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public Integer getNumeroArtisti() {
        return numeroArtisti;
    }

    public void setNumeroArtisti(Integer numeroArtisti) {
        this.numeroArtisti = numeroArtisti;
    }

    public Organizzatore getOrganizzatore() {
        return organizzatore;
    }

    public void setIdOrganizzatore(Organizzatore organizzatore) {
        this.organizzatore = organizzatore;
    }

    public Evento getIdEvento() {
        return evento;
    }

    public void setIdEvento(Evento evento) {
        this.evento = evento;
    }

    public Luogo getIdLuogo() {
        return luogo;
    }

    public void setIdLuogo(Luogo luogo) {
        this.luogo = luogo;
    }

    public Integer getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(Integer postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public Blob getImmagine() {
        return immagine;
    }

    public void setImmagine(Blob immagine) {
        this.immagine = immagine;
    }

    public Date getDataEsibizione() {
        return DataEsibizione;
    }

    public void setDataEsibizione(Date dataEsibizione) {
        DataEsibizione = dataEsibizione;
    }
}


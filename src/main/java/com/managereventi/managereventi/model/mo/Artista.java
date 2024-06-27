package com.managereventi.managereventi.model.mo;

public class Artista {
    private String codiceSIAE;
    private String nome;
    private String cognome;
    private String genere;
    private Esibizione esibizione;
    private Artista sostituto;
    private Evento evento;


    // Metodi getter e setter per ogni campo
    public String getCodiceSIAE() {
        return codiceSIAE;
    }

    public void setCodiceSIAE(String codiceSIAE) {
        this.codiceSIAE = codiceSIAE;
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

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public Esibizione getIdEsibizione() {
        return esibizione;
    }

    public void setIdEsibizione(Esibizione esibizione) {
        this.esibizione = esibizione;
    }

    public Artista getIdSostituto() {
        return sostituto;
    }

    public void setIdSostituto(Artista sostituto) {
        this.sostituto = sostituto;
    }

    public Evento getIdEvento() {
        return evento;
    }

    public void setIdEvento(Evento evento) {
        this.evento = evento;
    }

}
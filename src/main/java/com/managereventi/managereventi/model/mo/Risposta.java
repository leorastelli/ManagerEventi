package com.managereventi.managereventi.model.mo;

public class Risposta {
   private Domanda Domanda;
   private int IdRisposta;
   private String Descrizione;
   private Utente Utente;


    public Domanda getDomanda() {
        return Domanda;
    }

    public void setDomanda(Domanda Domanda) {
        this.Domanda = Domanda;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String Descrizione) {
        this.Descrizione = Descrizione;
    }

    public Utente getUtente() {
        return Utente;
    }

    public void setUtente(Utente Utente) {
        this.Utente = Utente;
    }

    public int getIdRisposta() {
        return IdRisposta;
    }

    public void setIdRisposta(int IdRisposta) {
        this.IdRisposta = IdRisposta;
    }

}
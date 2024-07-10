package com.managereventi.managereventi.model.mo;

import java.util.List;

public class Domanda {
   private int IdDomanda;
   private String Descrizione;
   private Utente Utente;

   private List<Risposta> Risposte;

    public int getIdDomanda() {
        return IdDomanda;
    }

    public void setIdDomanda(int IdDomanda) {
        this.IdDomanda = IdDomanda;
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

    public List<Risposta> getRisposte() {
        return Risposte;
    }

    public void setRisposte(List<Risposta> Risposte) {
        this.Risposte = Risposte;
    }

}
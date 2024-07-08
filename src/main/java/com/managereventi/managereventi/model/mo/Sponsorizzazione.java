package com.managereventi.managereventi.model.mo;

import java.sql.Blob;

public class Sponsorizzazione {
    private Azienda azienda;
    private Evento evento;
    private Blob logo;
    private Long costo;

    // Metodi getter e setter per ogni campo
    public Azienda getPartitaIVA() {
        return azienda;
    }

    public void setPartitaIVA(Azienda azienda) {
        this.azienda = azienda;
    }

    public Evento getIdEvento() {
        return evento;
    }

    public void setIdEvento(Evento evento) {
        this.evento = evento;
    }


    public Blob getLogo() {
        return logo;
    }

    public void setLogo(Blob logo) {
        this.logo = logo;
    }
    public Long getCosto() {
        return costo;
    }

    public void setCosto(Long costo) {
        this.costo = costo;
    }

}

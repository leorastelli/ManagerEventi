package com.managereventi.managereventi.model.mo;

public class Sponsorizzazione {
    private Azienda azienda;
    private Evento evento;
    private String logo;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getCosto() {
        return costo;
    }

    public void setCosto(Long costo) {
        this.costo = costo;
    }

}

package com.managereventi.managereventi.model.mo;

public class Luogo {
    private String idLuogo;
    private String via;
    private Integer numCivico;
    private String cap;
    private String provincia;
    private String stato;
    private String città;
    private Integer capienza;
    private String tipologia;


    // Metodi getter e setter per ogni campo
    public String getIdLuogo() {
        return idLuogo;
    }

    public void setIdLuogo(String idLuogo) {
        this.idLuogo = idLuogo;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public Integer getNumCivico() {
        return numCivico;
    }

    public void setNumCivico(Integer numCivico) {
        this.numCivico = numCivico;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getCittà() {
        return città;
    }

    public void setCittà(String città) {
        this.città = città;
    }

    public Integer getCapienza() {
        return capienza;
    }

    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

}

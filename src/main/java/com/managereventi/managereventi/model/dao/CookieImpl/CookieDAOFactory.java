package com.managereventi.managereventi.model.dao.CookieImpl;

import com.managereventi.managereventi.model.dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class CookieDAOFactory extends DAOFactory{

    private Map factoryParameters;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }
    @Override
    public void beginTransaction() {

        try {
            this.request=(HttpServletRequest) factoryParameters.get("request");
            this.response=(HttpServletResponse) factoryParameters.get("response");;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {

    }

    @Override
    public void rollbackTransaction() {

    }

    @Override
    public void closeTransaction() {

    }

    @Override
    public AziendaDAO getAziendDAO() {
        return null;
    }

    @Override
    public ArtistaDAO getArtistaDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EventoDAO getEventoDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbbonamentoDAO getAbbonamentoDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BigliettoDAO getBigliettoDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EsibizioneDAO getEsibizioneDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LuogoDAO getLuogoDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NewsletterDAO getNewsletterDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OrganizzatoreDAO getOrganizzatoreDAO() {
        return new OrganizzatoreDAOCookieImpl(request,response);
    }

    @Override
    public RecensioneDAO getRecensioneDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SponsorizzazioneDAO getSponsorizzazioneDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOCookieImpl(request,response);
    }

    @Override
    public CandidatureDAO getCandidaturaDAO() {
        return null;
    }

}

package com.managereventi.managereventi.model.dao;

import com.managereventi.managereventi.model.dao.CookieImpl.CookieDAOFactory;
import com.managereventi.managereventi.model.dao.mysqlJDBCImpl.mysqlJDBCDAOFactory;

import java.util.Map;

public abstract class DAOFactory {

  // List of DAO types supported by the factory
  public static final String MYSQLJDBCIMPL = "mysqlJDBCImpl";
  public static final String COOKIEIMPL= "CookieImpl";

  public abstract void beginTransaction();
  public abstract void commitTransaction();
  public abstract void rollbackTransaction();
  public abstract void closeTransaction();
  
  public abstract AziendaDAO getAziendDAO();

  public abstract ArtistaDAO getArtistaDAO();

  public abstract EventoDAO getEventoDAO();

  public abstract AbbonamentoDAO getAbbonamentoDAO();

  public abstract BigliettoDAO getBigliettoDAO();

  public abstract EsibizioneDAO getEsibizioneDAO();

  public abstract LuogoDAO getLuogoDAO();

  public  abstract NewsletterDAO getNewsletterDAO();

  public abstract OrganizzatoreDAO getOrganizzatoreDAO();

  public abstract RecensioneDAO getRecensioneDAO();

  public abstract SponsorizzazioneDAO getSponsorizzazioneDAO();

  public abstract UtenteDAO getUtenteDAO();

  public static DAOFactory getDAOFactory(String whichFactory,Map factoryParameters) {

    if (whichFactory.equals(MYSQLJDBCIMPL)) {
      return new mysqlJDBCDAOFactory(factoryParameters);
    } else if (whichFactory.equals(COOKIEIMPL)) {
      return new CookieDAOFactory(factoryParameters);
    } else {
      return null;
    }
  }

  public abstract CandidatureDAO getCandidaturaDAO();

  public abstract DomandaDAO getDomandaDAO();

    public abstract RispostaDAO getRispostaDAO();

}


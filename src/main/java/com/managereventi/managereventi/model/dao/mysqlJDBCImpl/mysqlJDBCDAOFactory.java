package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.*;

import com.managereventi.managereventi.model.dao.CookieImpl.OrganizzatoreDAOCookieImpl;
import com.managereventi.managereventi.services.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class mysqlJDBCDAOFactory extends DAOFactory{

    private Map factoryParameters;

    private Connection connection;

    public mysqlJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }


    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {

        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AziendaDAO getAziendDAO() {
        return new AziendaDAOmysqlJDBCImpl(connection);
    }

    @Override
    public ArtistaDAO getArtistaDAO() {
        return new ArtistaDAOmysqlJDBCImpl(connection);
    }

    @Override
    public AbbonamentoDAO getAbbonamentoDAO() {
        return new AbbonamentoDAOmysqlJDBCImpl(connection);
    }

    @Override
    public BigliettoDAO getBigliettoDAO() {
        return new BigliettoDAOmysqlJDBCImpl(connection);
    }

    @Override
    public EsibizioneDAO getEsibizioneDAO() {
        return new EsibizioneDAOmysqlJDBCImpl(connection);
    }

    @Override
    public EventoDAO getEventoDAO() {
        return new EventoDAOmysqlJDBCImpl(connection);
    }

    @Override
    public LuogoDAO getLuogoDAO() {
        return new LuogoDAOmysqlJDBCImpl(connection);
    }

    @Override
    public NewsletterDAO getNewsletterDAO() {
        return new NewsletterDAOmysqlJDBCImpl(connection);
    }

    @Override
    public OrganizzatoreDAO getOrganizzatoreDAO() {
        return new OrganizzatoreDAOmysqlJDBCImpl(connection);
    }

    @Override
    public RecensioneDAO getRecensioneDAO() {
        return new RecensioneDAOmysqlJDBCImpl(connection);
    }

    @Override
    public SponsorizzazioneDAO getSponsorizzazioneDAO() {
        return new SponsorizzazioneDAOmysqlJDBCImpl(connection);
    }

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOmysqlJDBCImpl(connection);
    }
}

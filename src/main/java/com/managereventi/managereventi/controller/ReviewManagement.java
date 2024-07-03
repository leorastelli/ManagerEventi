package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.DAOFactory;
import com.managereventi.managereventi.model.dao.EventoDAO;
import com.managereventi.managereventi.model.dao.RecensioneDAO;
import com.managereventi.managereventi.model.dao.UtenteDAO;
import com.managereventi.managereventi.model.mo.Evento;
import com.managereventi.managereventi.model.mo.Recensione;
import com.managereventi.managereventi.model.mo.Utente;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReviewManagement {

    private ReviewManagement(){}

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        List<Recensione> recensioni;
        List <String> eventi;

        Logger logger = LogService.getApplicationLogger();

        try{

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);

            daoFactory.beginTransaction();

            RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            recensioni = recensioneDAO.findAll();
            eventi = eventoDAO.getNomiEventi();

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("recensioni", recensioni);
            request.setAttribute("eventi", eventi);
            request.setAttribute("viewUrl", "reviewManagement/viewRecensioni");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }

        }
    }

    public static void filter(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        Utente loggedUser;
        List<Recensione> recensioni;
        List<String> eventi;

        Logger logger = LogService.getApplicationLogger();

        try {
            Map<String, Object> sessionFactoryParameters = new HashMap<>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            eventi = eventoDAO.getNomiEventi();

            String nomeEvento = request.getParameter("nomeEvento");
            String stelleString = request.getParameter("numeroStelle");

            if (nomeEvento != null && !nomeEvento.isEmpty()) {
                String idEvento = eventoDAO.getEventoByNome(nomeEvento);
                if (stelleString != null && !stelleString.isBlank()) {
                    int stelle = Integer.parseInt(stelleString);
                    recensioni = recensioneDAO.getRecensioniByEventoStelle(idEvento, stelle);
                } else {
                    recensioni = recensioneDAO.getRecensioniByEvento(idEvento);
                }
            } else if (stelleString != null && !stelleString.isBlank()) {
                recensioni = recensioneDAO.getRecensioniByStelle(Integer.parseInt(stelleString));
            }
            else {
                recensioni = recensioneDAO.findAll();
            }

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("recensioni", recensioni);
            request.setAttribute("eventi", eventi);
            request.setAttribute("viewUrl", "reviewManagement/viewRecensioni");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

}
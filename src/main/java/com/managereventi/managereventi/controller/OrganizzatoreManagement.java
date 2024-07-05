package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OrganizzatoreManagement {

    private OrganizzatoreManagement(){

    }
    public static void modifyOrganizzatore(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            OrganizzatoreDAO organizzatoreDAO = daoFactory.getOrganizzatoreDAO();
            Organizzatore organizzatore = organizzatoreDAO.getOrganizzatoreById(request.getParameter("idorganizzatore"));

            loggedOrganizzatore.setNome(request.getParameter("nome"));
            loggedOrganizzatore.setCognome(request.getParameter("cognome"));
            loggedOrganizzatore.setEmail(request.getParameter("email"));
            loggedOrganizzatore.setPassword(request.getParameter("password"));
            loggedOrganizzatore.setIdOrganizzatore(request.getParameter("idorganizzatore"));
            loggedOrganizzatore.setCodiceAutorizzazione(request.getParameter("codiceautorizzazione"));

            try {

                organizzatoreDAO.updateOrganizzatore(loggedOrganizzatore);
                sessionOrganizzatoreDAO.updateOrganizzatore(loggedOrganizzatore);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);
            request.setAttribute("viewUrl", "adminManagement/homeAdmin");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        }


    }

    public static void view(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;

        List<Evento> eventi;
        List<Esibizione> esibizioni;
        List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();
        List<Recensione> recensioni = new ArrayList<>();
        List<Candidature> candidature = new ArrayList<>();

        Logger logger = LogService.getApplicationLogger();

        try{
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();
            RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();
            CandidatureDAO candidatureDAO = daoFactory.getCandidaturaDAO();


            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            eventi = eventoDAO.getEventiByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());
            esibizioni = esibizioneDAO.getEsibizioniByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());

            for(Evento evento : eventi){
                sponsorizzazioni.addAll(sponsorizzazioneDAO.getSponsorizzazioniByEvento(evento.getIdEvento()));
                recensioni.addAll(recensioneDAO.getRecensioniByEvento(evento.getIdEvento()));
            }


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("eventi", eventi);
            request.setAttribute("esibizioni", esibizioni);
            request.setAttribute("sponsorizzazioni", sponsorizzazioni);
            request.setAttribute("recensioni", recensioni);
            request.setAttribute("viewUrl", "adminManagement/homeAdmin");

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

    public static void logon(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Organizzatore loggedOrganizzatore;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String codiceAuth = request.getParameter("codiceaut");


            OrganizzatoreDAO organizzatoreDAO = daoFactory.getOrganizzatoreDAO();
            Organizzatore organizzatore = organizzatoreDAO.getOrganizzatoreById(username);

            if (organizzatore == null || !organizzatore.getPassword().equals(password) || !organizzatore.getCodiceAutorizzazione().equals(codiceAuth)) {
                    sessionOrganizzatoreDAO.deleteOrganizzatore(null);
                    applicationMessage = "Username e password errati!";
                    loggedOrganizzatore=null;
            }
            else {
                loggedOrganizzatore = sessionOrganizzatoreDAO.createOrganizzatore(organizzatore);
            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedUser", null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("applicationMessage", applicationMessage);
            request.setAttribute("viewUrl", "homeManagement/view");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }


    public static void searchCandidature(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;

        List<Candidature> candidature = new ArrayList<>();

        Logger logger = LogService.getApplicationLogger();

        try{
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            CandidatureDAO candidatureDAO = daoFactory.getCandidaturaDAO();

            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            candidature = candidatureDAO.getCandidatureByPosizione(request.getParameter("position"));

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);
            request.setAttribute("candidature", candidature);
            request.setAttribute("viewUrl", "adminManagement/homeAdmin");

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


    private static void commonView(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request){
        List<Evento> eventi;
        List<Esibizione> esibizioni;
        List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();
        List<Recensione> recensioni = new ArrayList<>();
        Organizzatore loggedOrganizzatore;

        OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();

        EventoDAO eventoDAO = daoFactory.getEventoDAO();
        EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
        SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();
        RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();


        loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
        eventi = eventoDAO.getEventiByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());
        esibizioni = esibizioneDAO.getEsibizioniByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());

        for(Evento evento : eventi){
            List<Sponsorizzazione> sponsorizzazioniEvento = sponsorizzazioneDAO.getSponsorizzazioniByEvento(evento.getIdEvento());
            if (sponsorizzazioniEvento != null) {
                sponsorizzazioni.addAll(sponsorizzazioniEvento);
            }

            List<Recensione> recensioniEvento = recensioneDAO.getRecensioniByEvento(evento.getIdEvento());
            if (recensioniEvento != null) {
                recensioni.addAll(recensioniEvento);
            }
        }

        request.setAttribute("loggedOn",loggedOrganizzatore!=null);
        request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
        request.setAttribute("eventi", eventi);
        request.setAttribute("esibizioni", esibizioni);
        request.setAttribute("sponsorizzazioni", sponsorizzazioni);
        request.setAttribute("recensioni", recensioni);

    }
}
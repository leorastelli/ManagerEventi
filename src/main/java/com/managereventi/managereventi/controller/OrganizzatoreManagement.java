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

    public static void view(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;

        List<Evento> eventi;
        List<Esibizione> esibizioni;
        List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();
        List<Recensione> recensioni = new ArrayList<>();

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
            EventoDAO sessionEventoDAO = sessionDAOFactory.getEventoDAO();
            EsibizioneDAO sessionEsibizioneDAO = sessionDAOFactory.getEsibizioneDAO();
            SponsorizzazioneDAO sessionSponsorizzazioneDAO = sessionDAOFactory.getSponsorizzazioneDAO();
            RecensioneDAO sessionRecensioneDAO = sessionDAOFactory.getRecensioneDAO();

            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            eventi = sessionEventoDAO.getEventiByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());
            esibizioni = sessionEsibizioneDAO.getEsibizioniByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());

            for(Evento evento : eventi){
                sponsorizzazioni.addAll(sessionSponsorizzazioneDAO.getSponsorizzazioniByEvento(evento.getIdEvento()));
                recensioni.addAll(sessionRecensioneDAO.getRecensioniByEvento(evento.getIdEvento()));
            }


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("eventi", eventi);
            request.setAttribute("esibizioni", esibizioni);
            request.setAttribute("sponsorizzazioni", sponsorizzazioni);
            request.setAttribute("recensioni", recensioni);
            request.setAttribute("viewUrl", "organizzatoreManagement/viewOrganizzatore");

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
        EventoDAO sessionEventoDAO = sessionDAOFactory.getEventoDAO();
        EsibizioneDAO sessionEsibizioneDAO = sessionDAOFactory.getEsibizioneDAO();
        SponsorizzazioneDAO sessionSponsorizzazioneDAO = sessionDAOFactory.getSponsorizzazioneDAO();
        RecensioneDAO sessionRecensioneDAO = sessionDAOFactory.getRecensioneDAO();

        loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
        eventi = sessionEventoDAO.getEventiByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());
        esibizioni = sessionEsibizioneDAO.getEsibizioniByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());

        for(Evento evento : eventi){
            sponsorizzazioni.addAll(sessionSponsorizzazioneDAO.getSponsorizzazioniByEvento(evento.getIdEvento()));
            recensioni.addAll(sessionRecensioneDAO.getRecensioniByEvento(evento.getIdEvento()));
        }

        request.setAttribute("loggedOn",loggedOrganizzatore!=null);
        request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
        request.setAttribute("eventi", eventi);
        request.setAttribute("esibizioni", esibizioni);
        request.setAttribute("sponsorizzazioni", sponsorizzazioni);
        request.setAttribute("recensioni", recensioni);

    }
}
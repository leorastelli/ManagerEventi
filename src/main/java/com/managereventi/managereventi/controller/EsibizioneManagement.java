package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EsibizioneManagement {

    private EsibizioneManagement(){
        // Costruttore privato
    }

    public static void view(HttpServletRequest request, HttpServletResponse response){


        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        Azienda loggedAzienda;
        Esibizione esibizione;
        Evento evento;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            AziendaDAO sessionAziendaDAO = sessionDAOFactory.getAziendDAO();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();


            loggedUser = sessionUserDAO.findLoggedUser();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            esibizione = esibizioneDAO.getEsibizioneById((request.getParameter("idEsibizione")));
            evento = eventoDAO.getEventoById(request.getParameter("idEvento"));

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null || loggedOrganizzatore!=null || loggedAzienda!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("loggedAzienda", loggedAzienda);
            request.setAttribute("esibizione", esibizione);
            request.setAttribute("evento", evento);
            request.setAttribute("viewUrl", "esibizioniManagement/homeEsibizioni");

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

    public static void gotoBiglietti(HttpServletRequest request, HttpServletResponse response){


        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Esibizione esibizione;
        Evento evento;
        Luogo luogo;
        List<String> biglietti;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();


            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            LuogoDAO luogoDAO = daoFactory.getLuogoDAO();


            loggedUser = sessionUserDAO.findLoggedUser();


            esibizione = esibizioneDAO.getEsibizioneById((request.getParameter("idEsibizione")));
            luogo = luogoDAO.getLuogoById(esibizione.getIdLuogo().getIdLuogo());
            evento = eventoDAO.getEventoById(request.getParameter("idEvento"));
            biglietti = bigliettoDAO.getPostiOccupatiEsibizione(esibizione.getIdEsibizione());


            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("esibizione", esibizione);
            request.setAttribute("evento", evento);
            request.setAttribute("biglietti", biglietti);
            request.setAttribute("tipoLuogo", luogo.getTipologia());
            request.setAttribute("viewUrl", "paymentManagement/paginaBiglietto");

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

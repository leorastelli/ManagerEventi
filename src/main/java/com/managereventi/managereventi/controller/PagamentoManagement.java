package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PagamentoManagement {

    private PagamentoManagement(){

    }

    public static void gotoAbbonamento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        Utente loggedUser;
        Evento evento;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();


            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            evento = eventoDAO.getEventoById(request.getParameter("idEvento"));

            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedUser = sessionUserDAO.findLoggedUser();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("evento", evento);
            request.setAttribute("viewUrl", "paymentManagement/paginaAbbonamento");

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

    public static void gotopagamentoAbbonamento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;
        Abbonamento abbonamento = new Abbonamento();
        String applicationMessage = null;


        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            Evento evento = eventoDAO.getEventoById(request.getParameter("idEvento"));

           abbonamento.setIdAbbonamento(RandomString(10));
           abbonamento.setIdUtente(loggedUser);
           abbonamento.setPrezzo(Long.valueOf(request.getParameter("prezzo")));


            Integer entrate = request.getParameter("entrate") != null ? Integer.parseInt(request.getParameter("numEntrate")) : 0;

            if(entrate > 0) {
                abbonamento.setEntrate(entrate);
                abbonamento.setTipo("Ridotto");
            } else{

                Date dataInizio = evento.getDataInizio();
                Date dataFine = evento.getDataFine();

                LocalDate inizio = dataInizio.toLocalDate();
                LocalDate fine = dataFine.toLocalDate();

                Period period = Period.between(inizio, fine);

                abbonamento.setEntrate(period.getDays());
                abbonamento.setTipo("Intero");
            }





            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("evento",evento);
            request.setAttribute("abbonamento",abbonamento);
            request.setAttribute("viewUrl", "aziendaManagement/PagamentoAzienda");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
                if (daoFactory != null) daoFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
                if (daoFactory != null) daoFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }



    private static String RandomString(int n){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}

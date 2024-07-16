package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.sql.Blob;
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
           Long prezzo = Long.parseLong(request.getParameter("prezzo"));
           abbonamento.setPrezzo(prezzo);


           String numEntrate = request.getParameter("numEntrate");
           Integer entrate = Integer.parseInt(numEntrate);

            if(entrate > 0) {
                abbonamento.setEntrate(entrate);
                abbonamento.setTipo("Ridotto");
            } else{

                Date dataInizio = evento.getDataInizio();
                Date dataFine = evento.getDataFine();

                LocalDate inizio = dataInizio.toLocalDate();
                LocalDate fine = dataFine.toLocalDate();

                Period period = Period.between(inizio, fine);

                abbonamento.setEntrate(period.getDays() +1);
                abbonamento.setTipo("Intero");
            }





            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("evento",evento);
            request.setAttribute("abbonamento",abbonamento);
            request.setAttribute("viewUrl", "paymentManagement/pagamentoAbbonamento");

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

    public static void pagamentoAbbonamento (HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        String applicationMessage = null;
        DAOFactory daoFactory = null;
        Evento evento;
        Abbonamento abbonamento;


        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            abbonamento = new Abbonamento();

            abbonamento.setIdAbbonamento(request.getParameter("idAbbonamento"));
            abbonamento.setIdUtente(loggedUser);
            abbonamento.setTipo(request.getParameter("tipo"));
            abbonamento.setEntrate(Integer.parseInt(request.getParameter("numEntrate")));
            abbonamento.setPrezzo(Long.parseLong(request.getParameter("prezzo")));

            evento = eventoDAO.getEventoById(request.getParameter("idEvento"));
            abbonamento.setIdEvento(evento);

            try{
                abbonamentoDAO.createAbbonamento(abbonamento);
                request.setAttribute("viewUrl", "homeManagement/PagamentoSuccesso");
            }
            catch (Exception e) {
                //throw new RuntimeException(e);
                request.setAttribute("viewUrl", "homeManagement/ErrorPage");
            }



            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();



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

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
import java.util.*;
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

    public static void gotoPagamentoBiglietto(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        List<Biglietto> biglietti = new ArrayList<>();

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
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();

            Esibizione esibizione = esibizioneDAO.getEsibizioneById(request.getParameter("idEsibizione"));
            Evento evento = eventoDAO.getEventoById(request.getParameter("idEvento"));


            int cont = Integer.parseInt(request.getParameter("numPosti"));

            for (int i = 0; i < cont; i++) {
                Biglietto biglietto = new Biglietto();  // Crea un nuovo oggetto biglietto per ogni iterazione
                biglietto.setIdBiglietto(RandomString(10));
                biglietto.setIdUtente(loggedUser);
                biglietto.setIdEsibizione(esibizione);
                biglietto.setIdEvento(evento);
                biglietto.setPosto(0);

                Integer categoria = Integer.parseInt(request.getParameter("categoria"));
                if (categoria == 1) {
                    biglietto.setTipo("Parterre");
                    biglietto.setPrezzo(50L);
                } else {
                    biglietto.setTipo("Parterre VIP");
                    biglietto.setPrezzo(100L);
                }

                biglietti.add(biglietto);
            }

            String allSelectedSeats = request.getParameter("allSelectedSeats");

            if (allSelectedSeats != null && !allSelectedSeats.isEmpty()) {
                // Dividi il valore in un array di stringhe
                String[] selectedSeatsArray = allSelectedSeats.split(",");

                for (int i = 0; i < selectedSeatsArray.length; i++) {
                    Biglietto biglietto = new Biglietto();  // Crea un nuovo oggetto biglietto per ogni iterazione
                    biglietto.setIdBiglietto(RandomString(10));
                    biglietto.setIdEvento(evento);
                    biglietto.setIdEsibizione(esibizione);
                    biglietto.setIdUtente(loggedUser);

                    int posto = Integer.parseInt(selectedSeatsArray[i]);
                    biglietto.setPosto(posto);

                    // Assegna il prezzo e il tipo in base al valore di 'posto'
                    if (posto >= 1 && posto <= 54) {
                        biglietto.setPrezzo(90L);
                        biglietto.setTipo("Tribuna Sinistra");
                    } else if (posto >= 101 && posto <= 154) {
                        biglietto.setPrezzo(90L);
                        biglietto.setTipo("Tribuna Destra");
                    } else if (posto >= 201 && posto <= 338) {
                        biglietto.setPrezzo(70L);
                        biglietto.setTipo("Tribuna Frontale");
                    }

                    biglietti.add(biglietto);
                }

            }




            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("evento",evento);
            request.setAttribute("esibizione",esibizione);
            request.setAttribute("biglietti",biglietti);
            request.setAttribute("viewUrl", "paymentManagement/pagamentoBiglietto");

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

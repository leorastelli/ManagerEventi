package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventiManagement {

    private EventiManagement(){
        // Costruttore privato
    }

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        Azienda loggedAzienda;
        List<Evento> eventi;

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


            loggedUser = sessionUserDAO.findLoggedUser();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();


            eventi = eventoDAO.getAllEventi();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null || loggedOrganizzatore!=null || loggedAzienda!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("loggedAzienda", loggedAzienda);
            request.setAttribute("eventi", eventi);
            request.setAttribute("viewUrl", "eventiManagement/homeEventi");

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

    public static void gotoEvento(HttpServletRequest request, HttpServletResponse response){
        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        Azienda loggedAzienda;
        Evento evento;
        List<Esibizione> esibizioni;
        List<Sponsorizzazione> sponsorizzazioni;

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
            SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();

            loggedUser = sessionUserDAO.findLoggedUser();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            evento = eventoDAO.getEventoById(request.getParameter("idEvento"));
            esibizioni = esibizioneDAO.getEsibizioniByEvento(evento.getIdEvento());
            sponsorizzazioni = sponsorizzazioneDAO.getSponsorizzazioniByEvento(evento.getIdEvento());

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null || loggedOrganizzatore!=null || loggedAzienda!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("loggedAzienda", loggedAzienda);
            request.setAttribute("evento", evento);
            request.setAttribute("esibizioni", esibizioni);
            request.setAttribute("sponsorizzazioni", sponsorizzazioni);
            request.setAttribute("viewUrl", "eventiManagement/paginaEvento");

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

    public static void gotoCreaEvento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        List<String> luoghi;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            LuogoDAO luogoDAO = daoFactory.getLuogoDAO();
            luoghi = luogoDAO.getAllLuoghi();


            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();


            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("luoghi", luoghi);
            request.setAttribute("viewUrl", "eventiManagement/creaEvento");

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

    public static void creaEvento(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;
        List<Evento> eventi;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);


            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            LuogoDAO luogoDAO = daoFactory.getLuogoDAO();
            eventi = eventoDAO.getAllEventi();

            Evento evento = new Evento();
            evento.setIdEvento(RandomString(10));
            evento.setNome(request.getParameter("nome"));
            evento.setDescrizione(request.getParameter("descrizione"));
            evento.setIdEvento(RandomString(10));
            evento.setOrganizzatore(loggedOrganizzatore);
            String dataInizioString = request.getParameter("datainizio");


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(dataInizioString);
            java.sql.Date dataInizio = new java.sql.Date(parsedDate.getTime());
            evento.setDataInizio(dataInizio);

            String dataFineString = request.getParameter("datafine");
            Date parsedDateFine = sdf.parse(dataFineString);
            java.sql.Date dataFine = new java.sql.Date(parsedDateFine.getTime());
            evento.setDataFine(dataFine);


            Part filePart = request.getPart("logo");
            InputStream fileContent = filePart.getInputStream();

            // Converti l'InputStream in un Blob
            Blob logoBlob = inputStreamToBlob(fileContent);
            evento.setImmagine(logoBlob);

            eventoDAO.createEvento(evento);

            String nomeEsibizione = request.getParameter("nomeEsibizione");

            if (nomeEsibizione != null && !nomeEsibizione.isEmpty()) {

                Esibizione esibizione = new Esibizione();
                Luogo luogo = luogoDAO.getLuogoById(request.getParameter("luogo"));
                esibizione.setNome(request.getParameter("nomeEsibizione"));
                esibizione.setDescrizione(request.getParameter("descrizioneEsibizione"));
                esibizione.setIdEsibizione(RandomString(10));
                esibizione.setIdEvento(evento);
                esibizione.setIdLuogo(luogo);
                esibizione.setIdOrganizzatore(loggedOrganizzatore);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime oraInizio = LocalTime.parse(request.getParameter("orainizio"), formatter);
                java.sql.Time sqlOraInizio = Time.valueOf(oraInizio);

                esibizione.setOraInizio(sqlOraInizio);

                LocalTime durata = LocalTime.parse(request.getParameter("durata"), formatter);
                java.sql.Time sqlDurata = Time.valueOf(durata);
                esibizione.setDurata(sqlDurata);

                esibizione.setGenere(request.getParameter("genere"));


                Part filePart1 = request.getPart("logoEsibizione");
                InputStream fileContent1 = filePart1.getInputStream();

                // Converti l'InputStream in un Blob
                Blob logoBlob1 = inputStreamToBlob(fileContent1);
                esibizione.setImmagine(logoBlob1);

                try{
                    esibizioneDAO.createEsibizione(esibizione);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }



            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",true);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("eventi", eventi);
            request.setAttribute("viewUrl", "eventiManagement/homeEventi");

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

    public static void creaEsibizione(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;
        List<Evento> eventi;
        Evento evento;
        List<Esibizione> esibizioni;
        List<Sponsorizzazione> sponsorizzazioni;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);


            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            LuogoDAO luogoDAO = daoFactory.getLuogoDAO();
            SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();

            eventi = eventoDAO.getAllEventi();

            String nomeEsibizione = request.getParameter("nomeEsibizione");

            if (nomeEsibizione != null && !nomeEsibizione.isEmpty()) {

                Esibizione esibizione = new Esibizione();
                Luogo luogo = luogoDAO.getLuogoById(request.getParameter("luogo"));
                esibizione.setNome(request.getParameter("nomeEsibizione"));
                esibizione.setDescrizione(request.getParameter("descrizioneEsibizione"));
                esibizione.setIdEsibizione(RandomString(10));
                esibizione.setIdEvento(eventoDAO.getEventoById(request.getParameter("idevento")));
                esibizione.setIdLuogo(luogo);
                esibizione.setIdOrganizzatore(loggedOrganizzatore);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime oraInizio = LocalTime.parse(request.getParameter("orainizio"), formatter);
                java.sql.Time sqlOraInizio = Time.valueOf(oraInizio);

                esibizione.setOraInizio(sqlOraInizio);

                LocalTime durata = LocalTime.parse(request.getParameter("durata"), formatter);
                java.sql.Time sqlDurata = Time.valueOf(durata);
                esibizione.setDurata(sqlDurata);

                esibizione.setGenere(request.getParameter("genere"));


                Part filePart1 = request.getPart("logoEsibizione");
                InputStream fileContent1 = filePart1.getInputStream();

                // Converti l'InputStream in un Blob
                Blob logoBlob1 = inputStreamToBlob(fileContent1);
                esibizione.setImmagine(logoBlob1);

                try{
                    esibizioneDAO.createEsibizione(esibizione);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }

            evento = eventoDAO.getEventoById(request.getParameter("idevento"));
            esibizioni = esibizioneDAO.getEsibizioniByEvento(evento.getIdEvento());
            sponsorizzazioni = sponsorizzazioneDAO.getSponsorizzazioniByEvento(evento.getIdEvento());


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",true);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("eventi", eventi);
            request.setAttribute("evento", evento);
            request.setAttribute("esibizioni", esibizioni);
            request.setAttribute("sponsorizzazioni", sponsorizzazioni);
            request.setAttribute("viewUrl", "eventiManagement/paginaEvento");

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

    public static void gotoCreaEsibizione(HttpServletRequest request, HttpServletResponse response){

        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        List<String> luoghi;
        Evento evento;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            LuogoDAO luogoDAO = daoFactory.getLuogoDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            evento = eventoDAO.getEventoById(request.getParameter("idEvento"));
            luoghi = luogoDAO.getAllLuoghi();


            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();


            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("luoghi", luoghi);
            request.setAttribute("evento", evento);
            request.setAttribute("viewUrl", "esibizioniManagement/creaEsibizione");

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

    private static Blob inputStreamToBlob(InputStream inputStream) {
        try {
            byte[] bytes = inputStream.readAllBytes();  // Leggi tutto il contenuto dell'input stream in un array di byte
            return new javax.sql.rowset.serial.SerialBlob(bytes);  // Crea un Blob utilizzando SerialBlob
        } catch (IOException | SQLException e) {
            e.printStackTrace();  // Gestisci l'eccezione appropriatamente
            return null;
        } finally {
            try {
                inputStream.close();  // Chiudi l'InputStream
            } catch (IOException e) {
                e.printStackTrace();  // Gestisci l'eccezione nel caso di errore di chiusura
            }
        }
    }
}

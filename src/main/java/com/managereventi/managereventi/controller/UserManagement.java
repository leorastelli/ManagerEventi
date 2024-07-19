package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManagement {

    private UserManagement(){}

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        List<Biglietto > biglietti;
        List<Abbonamento> abbonamenti;
        List<Recensione> recensioni;
        List<String> nomiEventi;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();
            BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            Utente utente = utenteDAO.getUtenteById(loggedUser.getIdUtente());

            biglietti = bigliettoDAO.getBigliettiUtente(loggedUser);
            abbonamenti = abbonamentoDAO.getAbbonamentiUtente(loggedUser);
            recensioni = recensioneDAO.getRecensioniByUtente(loggedUser.getIdUtente());
            nomiEventi = eventoDAO.getNomiEventibyId(bigliettoDAO.getIdEventiUtente(utente));

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("biglietti", biglietti);
            request.setAttribute("abbonamenti", abbonamenti);
            request.setAttribute("recensioni", recensioni);
            request.setAttribute("pastEvents", nomiEventi);
            request.setAttribute("viewUrl", "homeManagement/AreaPersonaleUtente");

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

    public static void deleteUtente(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionUserDAO.deleteUtente(loggedUser);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            NewsletterDAO newsletterDAO = daoFactory.getNewsletterDAO();
            utenteDAO.deleteUtente(loggedUser);
            newsletterDAO.unsubscribeFromNewsletter(loggedUser.getIdUtente(), loggedUser.getEmail());


            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h3>Ci dispiace che tu abbia deciso di abbandonarci!" + loggedUser.getIdUtente() + "</h3>"
                    + "<p>I tuoi biglietti e abbonamenti rimarranno validi. Li trovi all'interno delle e-mail che ti abbiamo" +
                    " inviato al momento dell'acquisto</p>"
                    + "<p>PrimEvent</p>";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(loggedUser.getEmail()));
            message.setSubject("Disattivazione account");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            HomeManagement.logout(request, response);

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

    public static void modifyUtente(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;

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

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.getUtenteById(request.getParameter("idutente"));

            loggedUser.setNome(request.getParameter("nome"));
            loggedUser.setCognome(request.getParameter("cognome"));
            loggedUser.setEmail(request.getParameter("email"));
            loggedUser.setPassword(request.getParameter("password"));

            try {
                utenteDAO.updateUtente(loggedUser);
                sessionUserDAO.updateUtente(loggedUser);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);

            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "homeManagement/AreaPersonaleUtente");

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

    public static void registration(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;

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

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = new Utente();

            utente.setNome(request.getParameter("nome"));
            utente.setCognome(request.getParameter("cognome"));
            utente.setEmail(request.getParameter("email"));
            utente.setPassword(request.getParameter("password"));
            utente.setIdUtente(request.getParameter("username"));


            try{
                utenteDAO.createUtente(utente);
                request.setAttribute("viewUrl", "homeManagement/view");
            }
            catch (Exception e) {
                //throw new RuntimeException(e);
                request.setAttribute("viewUrl", "homeManagement/ErrorPage");
            }

            String newsletterParam = request.getParameter("newsletter");

            if (newsletterParam != null ) {
                Newsletter newsletter = new Newsletter();
                NewsletterDAO newsletterDAO = daoFactory.getNewsletterDAO();
                newsletterDAO.subscribeToNewsletter(utente.getIdUtente(), utente.getEmail());
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h3>Grazie per la registrazione " + utente.getNome() + "</h3>";

            if (newsletterParam != null){
                htmlContent += "<p>Ti sei iscritto alla nostra newsletter</p>";
            }

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(utente.getEmail()));
            message.setSubject("Registrazione avvenuta con successo");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

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

    public static  void stampaAbbonamento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Abbonamento abbonamento;
        DAOFactory daoFactory = null;

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

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            abbonamento = abbonamentoDAO.getAbbonamentoById(request.getParameter("idAbbonamento"));
            Evento evento = eventoDAO.getEventoById(abbonamento.getIdEvento().getIdEvento());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            File qrCodeFile = PagamentoManagement.generateQRCodeFile(abbonamento.getIdAbbonamento(), 150, 150);
            byte[] fileContent = Files.readAllBytes(qrCodeFile.toPath());
            String base64Encoded = Base64.getEncoder().encodeToString(fileContent);

            request.setAttribute("abbonamento", abbonamento);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("qrcode", base64Encoded);
            request.setAttribute("evento", evento);
            request.setAttribute("viewUrl", "homeManagement/StampaAbbonamento");

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
    public static  void stampaBiglietto(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Biglietto biglietto;
        DAOFactory daoFactory = null;

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

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            biglietto = bigliettoDAO.getBigliettoById(request.getParameter("idBiglietto"));
            Esibizione esibizione = esibizioneDAO.getEsibizioneById(biglietto.getIdEsibizione().getIdEsibizione());
            Evento evento = eventoDAO.getEventoById(esibizione.getIdEvento().getIdEvento());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            File qrCodeFile = PagamentoManagement.generateQRCodeFile(biglietto.getIdBiglietto(), 150, 150);
            byte[] fileContent = Files.readAllBytes(qrCodeFile.toPath());
            String base64Encoded = Base64.getEncoder().encodeToString(fileContent);

            request.setAttribute("biglietto", biglietto);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("qrcode", base64Encoded);
            request.setAttribute("evento", evento);
            request.setAttribute("esibizione", esibizione);
            request.setAttribute("viewUrl", "homeManagement/StampaBiglietto");

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

    public static void deleteBiglietto(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;

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

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();
            Utente utente = utenteDAO.getUtenteById(loggedUser.getIdUtente());

            bigliettoDAO.deleteBiglietto(request.getParameter("idBiglietto"));


            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h3>Ci dispiace che tu non riesca a partecipare!" + utente.getNome() + "</h3>"
                    + "<p>Stiamo provvedendo ad effettuare il rimborso</p>"
                    + "<p>PrimEvent</p>";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(utente.getEmail()));
            message.setSubject("Procedura di Reso");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);
            request.setAttribute("viewUrl", "homeManagement/AreaPersonaleUtente");

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

    public static void deleteAbbonamento(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;

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

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
            Utente utente = utenteDAO.getUtenteById(loggedUser.getIdUtente());


            abbonamentoDAO.deleteAbbonamento(request.getParameter("idAbbonamento"));


            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h3>Ci dispiace che tu non riesca a partecipare!" + utente.getIdUtente() + "</h3>"
                    + "<p>Stiamo provvedendo ad effettuare il rimborso</p>"
                    + "<p>PrimEvent</p>";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(utente.getEmail()));
            message.setSubject("Procedura di Reso");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);
            request.setAttribute("viewUrl", "homeManagement/AreaPersonaleUtente");

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

    public static void submitReview(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        DAOFactory daoFactory = null;
        List<Biglietto > biglietti;
        List<Abbonamento> abbonamenti;

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

            RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            Recensione recensione = new Recensione();


            String nomeEvento = request.getParameter("nomeEvento");
            String IdEvento = eventoDAO.getEventoByNome(request.getParameter("nomeEvento"));

            if (nomeEvento == null || nomeEvento.isEmpty()) {
                throw new RuntimeException("Event name cannot be null or empty.");
            }

            Evento evento = eventoDAO.getEventoById(IdEvento);

            recensione.setIdUtente(loggedUser);
            recensione.setIdEvento(evento);
            recensione.setIdRecensione(RandomString(6));
            recensione.setDescrizione(request.getParameter("descrizione"));
            recensione.setStelle(Integer.parseInt(request.getParameter("rating")));

            recensioneDAO.createRecensione(recensione);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);
            request.setAttribute("viewUrl", "homeManagement/AreaPersonaleUtente");

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
        List<Biglietto > biglietti;
        List<Abbonamento> abbonamenti;
        List<Recensione> recensioni;
        List<String> nomiEventi;
        Utente loggedUser;

        UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
        loggedUser = sessionUserDAO.findLoggedUser();

        UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
        RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();
        BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();
        AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
        EventoDAO eventoDAO = daoFactory.getEventoDAO();

        Utente utente = utenteDAO.getUtenteById(loggedUser.getIdUtente());

        biglietti = bigliettoDAO.getBigliettiUtente(loggedUser);
        abbonamenti = abbonamentoDAO.getAbbonamentiUtente(loggedUser);
        recensioni = recensioneDAO.getRecensioniByUtente(loggedUser.getIdUtente());
        nomiEventi = eventoDAO.getNomiEventibyId(bigliettoDAO.getIdEventiUtente(utente));

        request.setAttribute("loggedOn",loggedUser!=null);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("biglietti", biglietti);
        request.setAttribute("abbonamenti", abbonamenti);
        request.setAttribute("recensioni", recensioni);
        request.setAttribute("pastEvents", nomiEventi);
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

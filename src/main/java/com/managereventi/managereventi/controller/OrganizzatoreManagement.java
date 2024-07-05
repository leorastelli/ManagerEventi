package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;
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
            loggedOrganizzatore.setIdOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());
            loggedOrganizzatore.setCodiceAutorizzazione(request.getParameter("codiceaut"));

            try {
                organizzatoreDAO.updateOrganizzatore(loggedOrganizzatore);
                sessionOrganizzatoreDAO.updateOrganizzatore(loggedOrganizzatore);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);

            request.setAttribute("loggedOrganizzatore",loggedOrganizzatore);
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

    public static void registration(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);


            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();

            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            OrganizzatoreDAO organizzatoreDAO = daoFactory.getOrganizzatoreDAO();
            Organizzatore organizzatore = new Organizzatore();

            organizzatore.setNome(request.getParameter("nome"));
            organizzatore.setCognome(request.getParameter("cognome"));
            organizzatore.setEmail(request.getParameter("email"));
            organizzatore.setPassword(request.getParameter("password"));
            organizzatore.setIdOrganizzatore(request.getParameter("username"));
            organizzatore.setCodiceAutorizzazione(request.getParameter("codiceaut"));

            try{
                organizzatoreDAO.createOrganizzatore(organizzatore);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h1>Grazie per la registrazione come organizzatore " + organizzatore.getIdOrganizzatore() + "</h1>";


            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(organizzatore.getEmail()));
            message.setSubject("Registrazione come organizzatore avvenuta con successo");

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
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("viewUrl", "homeManagement/view");

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

    public static void deleteRecensione(HttpServletRequest request, HttpServletResponse response){

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

            RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();

            recensioneDAO.deleteRecensione(request.getParameter("idRecensione"));

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            commonView(daoFactory, sessionDAOFactory, request);

            request.setAttribute("loggedOrganizzatore",loggedOrganizzatore);
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

    public static void deleteEvento(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;
        List<String> emailList = new ArrayList<>();

        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

            eventoDAO.deleteEvento(request.getParameter("idEvento"));
            emailList = utenteDAO.getEmailByEvento(request.getParameter("idEvento"));

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h1>Ci dispiace ma il tuo evento " + request.getParameter("idEvento") + " è stato annullato. Provvederemo ad eseguire il rimborso</h1>";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));

                InternetAddress[] recipientAddresses = new InternetAddress[emailList.size()];
                for (int i = 0; i < emailList.size(); i++) {
                    recipientAddresses[i] = new InternetAddress(emailList.get(i));
                }
                message.setRecipients(Message.RecipientType.TO, recipientAddresses);

                message.setSubject("Il tuo evento è stato annullato!");

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(htmlContent, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                message.setContent(multipart);

                Transport.send(message);

                System.out.println("Email inviate con successo!");

            } catch (MessagingException e) {
                e.printStackTrace();
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
                // Handle rollback errors
            }
            throw new RuntimeException(e);
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
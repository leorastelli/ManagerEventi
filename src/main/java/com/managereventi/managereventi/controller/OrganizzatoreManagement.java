package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.lang.model.element.NestingKind;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
            //loggedOrganizzatore.setIdOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());

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
        List<String> abbonamentoVenduti = new ArrayList<>();

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
            AbbonamentoDAO abbonamentoDAO = daoFactory.getAbbonamentoDAO();
            RecensioneDAO recensioneDAO = daoFactory.getRecensioneDAO();
            CandidatureDAO candidatureDAO = daoFactory.getCandidaturaDAO();


            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            eventi = eventoDAO.getEventiByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());
            esibizioni = esibizioneDAO.getEsibizioniByOrganizzatore(loggedOrganizzatore.getIdOrganizzatore());

            for(Evento evento : eventi){
                sponsorizzazioni.addAll(sponsorizzazioneDAO.getSponsorizzazioniByEvento(evento.getIdEvento()));
                recensioni.addAll(recensioneDAO.getRecensioniByEvento(evento.getIdEvento()));
                abbonamentoVenduti.add(String.valueOf(abbonamentoDAO.getAbbonamentiVendutiEvento(evento.getIdEvento())));
            }


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("eventi", eventi);
            request.setAttribute("esibizioni", esibizioni);
            request.setAttribute("sponsorizzazioni", sponsorizzazioni);
            request.setAttribute("recensioni", recensioni);
            request.setAttribute("abbonamentiVenduti", abbonamentoVenduti);
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

    public static void modifyEsibizione(HttpServletRequest request, HttpServletResponse response){
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


            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            Esibizione esibizione = esibizioneDAO.getEsibizioneById(request.getParameter("IdEsibizione"));

            esibizione.setNome(request.getParameter("nome-esibizione"));
            esibizione.setDescrizione(request.getParameter("descrizione"));
            esibizione.setGenere(request.getParameter("genere"));
            Time durata = Time.valueOf(LocalTime.parse(request.getParameter("durata"), DateTimeFormatter.ofPattern("HH:mm:ss")));
            esibizione.setDurata(durata);
            Time oraInizio = Time.valueOf(LocalTime.parse(request.getParameter("ora-inizio"), DateTimeFormatter.ofPattern("HH:mm:ss")));
            esibizione.setOraInizio(oraInizio);

            try {
                esibizioneDAO.updateEsibizione(esibizione);

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

    public static void modifyEvento(HttpServletRequest request, HttpServletResponse response){
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


            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            Evento evento = eventoDAO.getEventoById(request.getParameter("idEvento"));

            evento.setNome(request.getParameter("nome-evento"));
            evento.setDescrizione(request.getParameter("descrizione-evento"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Parsing delle date da stringa a java.util.Date
            Date parsedDataInizio = dateFormat.parse(request.getParameter("data-inizio"));
            Date parsedDataFine = dateFormat.parse(request.getParameter("data-fine"));

            // Conversione da java.util.Date a java.sql.Date
            java.sql.Date sqlDataInizio = new java.sql.Date(parsedDataInizio.getTime());
            java.sql.Date sqlDataFine = new java.sql.Date(parsedDataFine.getTime());

            // Imposta le date nell'oggetto evento
            evento.setDataInizio(sqlDataInizio);
            evento.setDataFine(sqlDataFine);

            try {
                eventoDAO.updateEvento(evento);

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
                request.setAttribute("viewUrl", "homeManagement/ErrorPage");

            }
            else {
                loggedOrganizzatore = sessionOrganizzatoreDAO.createOrganizzatore(organizzatore);
                request.setAttribute("viewUrl", "homeManagement/view");

            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedUser", null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("loggedAzienda", null);
            request.setAttribute("applicationMessage", applicationMessage);

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
                //throw new RuntimeException(e);
                request.setAttribute("viewUrl", "homeManagement/ErrorPage");
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

    public static void deleteEsibizione(HttpServletRequest request, HttpServletResponse response) {

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

            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

            esibizioneDAO.deleteEsibizione(request.getParameter("IdEsibizione"));
            emailList = utenteDAO.getEmailByEsibizione(request.getParameter("IdEsibizione"));

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h1>Ci dispiace ma la tua esibizione " + request.getParameter("IdEsibizione") + " è stata annullata. Provvederemo ad eseguire il rimborso</h1>";

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

                message.setSubject("Esibizione annullata!");

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(htmlContent, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                message.setContent(multipart);

                Transport.send(message);

                System.out.println("Email inviate con successo!");

            }   catch (MessagingException e) {

                    e.printStackTrace();
                }

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                commonView(daoFactory, sessionDAOFactory, request);

                request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
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

    public static void sendEmailCandidato(HttpServletRequest request, HttpServletResponse response){

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

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h1>" + "L'organizzatore: " + loggedOrganizzatore.getNome() + loggedOrganizzatore.getCognome() + " ti sta contattando. "
                    + "</h1>"+"<p>" +  request.getParameter("mailtext") + "</p>";


            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(request.getParameter("email")));
            message.setSubject("Candidatura");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();


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

    public static void sendNewsletter(HttpServletRequest request, HttpServletResponse response){

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

            NewsletterDAO newsletterDAO = daoFactory.getNewsletterDAO();

            emailList = newsletterDAO.getMailList("f");

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = request.getParameter("mailtext");

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

                message.setSubject("Newsletter PrimeEvent");

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


        }catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
                // Handle rollback errors
            }
            throw new RuntimeException(e);
        }


        commonView(daoFactory, sessionDAOFactory, request);


        request.setAttribute("viewUrl", "adminManagement/homeAdmin");

    }

    public static void infoEsibizione(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Organizzatore loggedOrganizzatore;
        DAOFactory daoFactory = null;
        Esibizione esibizione = null;
        Evento evento = null;
        List<String> biglietti = new ArrayList<>();
        String numParterre = null;
        String numParterreVIP = null;
        String numTribunaFront = null;
        String numTribunasx = null;
        String numTribunadx = null;
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
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();

            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();
            BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            esibizione = esibizioneDAO.getEsibizioneById(request.getParameter("IdEsibizione"));
            evento = eventoDAO.getEventoById(esibizione.getIdEvento().getIdEvento());
            biglietti = bigliettoDAO.getPostiOccupatiEsibizione(esibizione.getIdEsibizione());

            numParterre = bigliettoDAO.getBigliettiVendutiTipologia("Parterre", evento.getIdEvento(), esibizione.getIdEsibizione());
            numParterreVIP = bigliettoDAO.getBigliettiVendutiTipologia("Parterre VIP", evento.getIdEvento(), esibizione.getIdEsibizione());
            numTribunaFront = bigliettoDAO.getBigliettiVendutiTipologia("Tribuna Frontale", evento.getIdEvento(), esibizione.getIdEsibizione());
            numTribunasx = bigliettoDAO.getBigliettiVendutiTipologia("Tribuna Sinistra", evento.getIdEvento(), esibizione.getIdEsibizione());
            numTribunadx = bigliettoDAO.getBigliettiVendutiTipologia("Tribuna Destra", evento.getIdEvento(), esibizione.getIdEsibizione());


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedOrganizzatore!=null);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("esibizione", esibizione);
            request.setAttribute("evento", evento);
            request.setAttribute("biglietti", biglietti);
            request.setAttribute("numParterre", numParterre);
            request.setAttribute("numParterreVIP", numParterreVIP);
            request.setAttribute("numTribunaFront", numTribunaFront);
            request.setAttribute("numTribunasx", numTribunasx);
            request.setAttribute("numTribunadx", numTribunadx);
            request.setAttribute("viewUrl", "adminManagement/infoEsibizione");

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
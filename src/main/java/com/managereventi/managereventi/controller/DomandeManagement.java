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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DomandeManagement {

    private DomandeManagement() {

    }

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        Azienda loggedAzienda;
        List<Domanda> domande;

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

            DomandaDAO domandaDAO = daoFactory.getDomandaDAO();


            loggedUser = sessionUserDAO.findLoggedUser();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            domande = domandaDAO.getAllDomande();

            for (Domanda domanda : domande) {
                domanda.setRisposte(domandaDAO.getRisposteDomanda(domanda));
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null || loggedOrganizzatore!=null || loggedAzienda!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("loggedAzienda", loggedAzienda);
            request.setAttribute("domande", domande);
            request.setAttribute("viewUrl", "domandeManagement/view");

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

    public static void addDomanda(HttpServletRequest request, HttpServletResponse response){

        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        Azienda loggedAzienda;
        List<Domanda> domande;

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

            DomandaDAO domandaDAO = daoFactory.getDomandaDAO();

            loggedUser = sessionUserDAO.findLoggedUser();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            Domanda addDomanda = new Domanda();
            addDomanda.setUtente(loggedUser);
            addDomanda.setDescrizione(request.getParameter("descrizione"));

            domandaDAO.createDomanda(addDomanda);

            domande = domandaDAO.getAllDomande();

            for (Domanda domanda : domande) {
                domanda.setRisposte(domandaDAO.getRisposteDomanda(domanda));
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null || loggedOrganizzatore!=null || loggedAzienda!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("loggedAzienda", loggedAzienda);
            request.setAttribute("domande", domande);
            request.setAttribute("viewUrl", "domandeManagement/view");

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

    public static void addRisposta(HttpServletRequest request, HttpServletResponse response){


        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;
        Azienda loggedAzienda;
        List<Domanda> domande;

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

            DomandaDAO domandaDAO = daoFactory.getDomandaDAO();
            RispostaDAO rispostaDAO = daoFactory.getRispostaDAO();

            loggedUser = sessionUserDAO.findLoggedUser();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();


            Risposta addRisposta = new Risposta();
            Domanda domanda1 = domandaDAO.getDomandaById(request.getParameter("idDomanda"));
            addRisposta.setDomanda(domanda1);
            addRisposta.setUtente(loggedUser);
            addRisposta.setDescrizione(request.getParameter("descrizioneRisposta"));

            rispostaDAO.createRisposta(addRisposta);

            String mail = domandaDAO.getMailUtente(domanda1);

            domande = domandaDAO.getAllDomande();

            for (Domanda domanda : domande) {
                domanda.setRisposte(domandaDAO.getRisposteDomanda(domanda));
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h3>Hai ricevuto una risposta alla tua domanda</h3><br><p>Risposta ricevuta" +
                    "da parte di " + loggedUser.getIdUtente() + ": "+ request.getParameter("descrizioneRisposta")+  "</p>";


            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            message.setSubject("Ricevuta risposta");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null || loggedOrganizzatore!=null || loggedAzienda!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", loggedOrganizzatore);
            request.setAttribute("loggedAzienda", loggedAzienda);
            request.setAttribute("domande", domande);
            request.setAttribute("viewUrl", "domandeManagement/view");

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

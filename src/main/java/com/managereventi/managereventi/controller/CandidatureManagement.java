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
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CandidatureManagement {

    private CandidatureManagement() {}

    public static void view(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        Utente loggedUser;
        Organizzatore loggedOrganizer;
        Azienda loggedCompany;
        List<Candidature> candidature;

        Logger logger = LogService.getApplicationLogger();

        try {
            Map<String, Object> sessionFactoryParameters = new HashMap<>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            AziendaDAO sessionCompanyDAO = sessionDAOFactory.getAziendDAO();
            OrganizzatoreDAO sessionOrganizerDAO = sessionDAOFactory.getOrganizzatoreDAO();

            loggedOrganizer = sessionOrganizerDAO.finLoggedOrganizzatore();
            loggedCompany = sessionCompanyDAO.findLoggedUser();
            loggedUser = sessionUserDAO.findLoggedUser();

            DAOFactory daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CandidatureDAO candidaturaDAO = daoFactory.getCandidaturaDAO();
            candidature = candidaturaDAO.findAll();

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null || loggedCompany != null || loggedOrganizer != null);
            request.setAttribute("loggedAzienda", loggedCompany);
            request.setAttribute("loggedOrganizzatore", loggedOrganizer);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("candidature", candidature);
            request.setAttribute("viewUrl", "candidatureManagement/viewCandidature");

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

    public static void addCandidatura(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory = null;
        Utente loggedUser;
        DAOFactory daoFactory = null;

        Logger logger = LogService.getApplicationLogger();

        try {
            Map<String, Object> sessionFactoryParameters = new HashMap<>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CandidatureDAO candidaturaDAO = daoFactory.getCandidaturaDAO();
            Candidature candidatura = new Candidature();

            candidatura.setPosizione(request.getParameter("position"));
            candidatura.setNome(request.getParameter("name"));
            candidatura.setCognome(request.getParameter("surname"));
            candidatura.setEmail(request.getParameter("email"));
            candidatura.setDataNascita1(request.getParameter("birthdate"));
            candidatura.setTelefono(request.getParameter("phone"));
            candidatura.setCitta(request.getParameter("city"));
            candidatura.setDescrizione(request.getParameter("description"));

            try{
                candidaturaDAO.createCandidature(candidatura);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            String htmlContent = "<h3>Grazie per la candidatura " + candidatura.getNome() +
                    " " + candidatura.getCognome() +  "</h3>"
                    + "<p> Abbiamo registrato la tua candidatura per la posizione di: </p>" + candidatura.getPosizione();

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(candidatura.getEmail()));
            message.setSubject("Candidatura ricevuta");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            HomeManagement.view(request, response);

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
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

}

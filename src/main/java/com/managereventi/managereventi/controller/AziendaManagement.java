package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.AziendaDAO;
import com.managereventi.managereventi.model.dao.DAOFactory;
import com.managereventi.managereventi.model.dao.OrganizzatoreDAO;
import com.managereventi.managereventi.model.dao.UtenteDAO;
import com.managereventi.managereventi.model.mo.Azienda;
import com.managereventi.managereventi.model.mo.Organizzatore;
import com.managereventi.managereventi.model.mo.Utente;
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
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AziendaManagement {

    private AziendaManagement(){
        //do nothing
    }

    public static void view(HttpServletRequest request, HttpServletResponse response){


    }

    public static void logon(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Azienda loggedAzienda;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            AziendaDAO sessionAziendaDAO = sessionDAOFactory.getAziendDAO();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String partitaIVA = request.getParameter("PartitaIVA");
            String password = request.getParameter("password");


            AziendaDAO aziendaDAO = daoFactory.getAziendDAO();
            Azienda azienda = aziendaDAO.getAziendaByPartitaIVA(partitaIVA);

            if(azienda == null || !azienda.getPassword().equals(password)){
                sessionAziendaDAO.deleteAzienda(null);
                applicationMessage = "Partita IVA e password errati!";
                loggedAzienda=null;
            }
            else {
                loggedAzienda = sessionAziendaDAO.createAzienda(azienda);

            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedAzienda!=null);
            request.setAttribute("loggedUser", null);
            request.setAttribute("loggedOrganizzatore", null);
            request.setAttribute("loggedAzienda", loggedAzienda);
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
        Azienda loggedAzienda;
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
            AziendaDAO sessionAziendaDAO = sessionDAOFactory.getAziendDAO();

            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedUser = sessionUserDAO.findLoggedUser();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            AziendaDAO aziendaDAO = daoFactory.getAziendDAO();
            Azienda azienda = new Azienda();

            azienda.setPartitaIVA(request.getParameter("partitaIVA"));
            azienda.setNome(request.getParameter("nome"));
            azienda.setIndirizzo(request.getParameter("indirizzo"));
            azienda.setCitta(request.getParameter("citta"));
            azienda.setProvincia(request.getParameter("provincia"));
            azienda.setCap(request.getParameter("cap"));
            azienda.setStato(request.getParameter("stato"));
            azienda.setTelefono(request.getParameter("telefono"));
            azienda.setEmail(request.getParameter("email"));
            azienda.setPassword(request.getParameter("password"));

            try{
                aziendaDAO.createAzienda(azienda);
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

            String htmlContent = "<h1>Grazie per la registrazione come azienda sponsor " + azienda.getNome() + "</h1>";


            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(azienda.getEmail()));
            message.setSubject("Registrazione come azienda sponsor avvenuta con successo");

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
            request.setAttribute("loggedAzienda", azienda);
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
}

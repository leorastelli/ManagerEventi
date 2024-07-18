package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.*;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AziendaManagement {

    private AziendaManagement(){
        //do nothing
    }

    public static void view(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Azienda loggedAzienda;
        String applicationMessage = null;
        DAOFactory daoFactory = null;
        List<Sponsorizzazione> sponsorizzazioni;
        List<String> eventi;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            AziendaDAO sessionAziendaDAO = sessionDAOFactory.getAziendDAO();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            sponsorizzazioni = sponsorizzazioneDAO.getSponsorizzazioniByPartitaIVA(loggedAzienda.getPartitaIVA());
            eventi = eventoDAO.getEventiPerSponsorizzazione(loggedAzienda.getPartitaIVA());


            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedAzienda!=null);
            request.setAttribute("loggedAzienda",loggedAzienda);
            request.setAttribute("sponsorizzazioni",sponsorizzazioni);
            request.setAttribute("eventi",eventi);
            request.setAttribute("viewUrl", "aziendaManagement/homeAzienda");

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

    public static void modifyAzienda(HttpServletRequest request, HttpServletResponse response) {
        DAOFactory sessionDAOFactory= null;
        Azienda loggedAzienda;
        DAOFactory daoFactory = null;

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

            AziendaDAO aziendaDAO = daoFactory.getAziendDAO();
            Azienda azienda = aziendaDAO.getAziendaByPartitaIVA(request.getParameter("partitaIVA"));

            loggedAzienda.setNome(request.getParameter("nome"));
            loggedAzienda.setIndirizzo(request.getParameter("indirizzo"));
            loggedAzienda.setCitta(request.getParameter("citta"));
            loggedAzienda.setProvincia(request.getParameter("provincia"));
            loggedAzienda.setCap(request.getParameter("cap"));
            loggedAzienda.setStato(request.getParameter("stato"));
            loggedAzienda.setTelefono(request.getParameter("telefono"));
            loggedAzienda.setEmail(request.getParameter("email"));
            loggedAzienda.setPassword(request.getParameter("password"));


            try {
                aziendaDAO.updateAzienda(loggedAzienda);
                sessionAziendaDAO.updateAzienda(loggedAzienda);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            view(request,response);

            request.setAttribute("loggedOn",loggedAzienda!=null);
            request.setAttribute("loggedAzienda",loggedAzienda);
            request.setAttribute("viewUrl", "aziendaManagement/homeAzienda");

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
                request.setAttribute("viewUrl", "homeManagement/ErrorPage");
            }
            else {
                loggedAzienda = sessionAziendaDAO.createAzienda(azienda);
                request.setAttribute("viewUrl", "homeManagement/view");
            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedAzienda!=null);
            request.setAttribute("loggedUser", null);
            request.setAttribute("loggedOrganizzatore", null);
            request.setAttribute("loggedAzienda", loggedAzienda);
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

    public static void pagamento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Azienda loggedAzienda;
        String applicationMessage = null;
        DAOFactory daoFactory = null;
        List<Sponsorizzazione> sponsorizzazioni;
        List<String> eventi;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            AziendaDAO sessionAziendaDAO = sessionDAOFactory.getAziendDAO();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            Sponsorizzazione sponsorizzazione = new Sponsorizzazione();

            sponsorizzazione.setPartitaIVA(loggedAzienda);
            sponsorizzazione.setCosto(Long.parseLong(request.getParameter("costo")));
            sponsorizzazione.setIdEvento(daoFactory.getEventoDAO().getEventoById(request.getParameter("idEvento")));
            //sponsorizzazione.getIdEvento().setIdEvento(request.getParameter("idEvento"));

            Part filePart = request.getPart("logo");
            InputStream fileContent = filePart.getInputStream();

            // Converti l'InputStream in un Blob
            Blob logoBlob = inputStreamToBlob(fileContent);
            sponsorizzazione.setLogo(logoBlob);

            try{
                sponsorizzazioneDAO.createSponsorizzazione(sponsorizzazione);
                request.setAttribute("viewUrl", "homeManagement/PagamentoSuccesso");
            }
            catch (Exception e) {
                //throw new RuntimeException(e);
                request.setAttribute("viewUrl", "homeManagement/ErrorPage");
            }

            sponsorizzazioni = sponsorizzazioneDAO.getSponsorizzazioniByPartitaIVA(loggedAzienda.getPartitaIVA());
            eventi = eventoDAO.getEventiPerSponsorizzazione(loggedAzienda.getPartitaIVA());

            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();


            request.setAttribute("loggedOn",loggedAzienda!=null);
            request.setAttribute("loggedAzienda",loggedAzienda);
            request.setAttribute("sponsorizzazioni",sponsorizzazioni);
            request.setAttribute("eventi",eventi);



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

    public static void gotoPagamento(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Azienda loggedAzienda;
        DAOFactory daoFactory = null;
        Sponsorizzazione sponsorizzazione = null;
        String applicationMessage = null;
        List<String> eventi;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            AziendaDAO sessionAziendaDAO = sessionDAOFactory.getAziendDAO();
            loggedAzienda = sessionAziendaDAO.findLoggedUser();

            SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            eventi = eventoDAO.getEventiPerSponsorizzazione(loggedAzienda.getPartitaIVA());

            Sponsorizzazione spazio = new Sponsorizzazione();

            spazio.setPartitaIVA(loggedAzienda);
            String scelta = request.getParameter("scelta");

            if ("casuale".equals(scelta)) {
                spazio.setCosto(Long.parseLong("100"));
                Random random = new Random();
                int indiceCasuale = random.nextInt(eventi.size());
                String eventoCasuale = eventi.get(indiceCasuale);
                Evento evento = eventoDAO.getEventiByNome(eventoCasuale);
                spazio.setIdEvento(evento);

            } else if ("scelta".equals(scelta)) {
                spazio.setCosto(Long.parseLong("200"));
                Evento evento = eventoDAO.getEventiByNome(request.getParameter("evento"));
                spazio.setIdEvento(evento);
            }

            //Part filePart = request.getPart("logo");
            //InputStream fileContent = filePart.getInputStream();

            // Converti l'InputStream in un Blob
            //Blob logoBlob = inputStreamToBlob(fileContent);


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedAzienda!=null);
            request.setAttribute("loggedAzienda",loggedAzienda);
            request.setAttribute("sponsorizzazione",sponsorizzazione);
            request.setAttribute("eventi",eventi);
            request.setAttribute("spazio",spazio);
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

    public static void deleteSpazio(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOFactory= null;
        Azienda loggedAzienda;
        DAOFactory daoFactory = null;
        List<Sponsorizzazione> sponsorizzazioni;
        List<String> eventi;


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

            SponsorizzazioneDAO sponsorizzazioneDAO = daoFactory.getSponsorizzazioneDAO();

            String PIva = loggedAzienda.getPartitaIVA();
            String idEvento = request.getParameter("idevento");
            sponsorizzazioneDAO.deleteSponsorizzazione(PIva, idEvento);

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            sponsorizzazioni = sponsorizzazioneDAO.getSponsorizzazioniByPartitaIVA(loggedAzienda.getPartitaIVA());
            eventi = eventoDAO.getEventiPerSponsorizzazione(loggedAzienda.getPartitaIVA());


            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedAzienda!=null);
            request.setAttribute("loggedAzienda",loggedAzienda);
            request.setAttribute("sponsorizzazioni",sponsorizzazioni);
            request.setAttribute("eventi",eventi);
            request.setAttribute("viewUrl", "aziendaManagement/homeAzienda");


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();



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

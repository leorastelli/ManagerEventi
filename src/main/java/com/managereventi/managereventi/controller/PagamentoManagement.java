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
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PagamentoManagement {

    private PagamentoManagement() {

    }

    public static void gotoAbbonamento(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory daoFactory = null;
        DAOFactory sessionDAOFactory = null;
        Organizzatore loggedOrganizzatore;
        Utente loggedUser;
        Evento evento;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();


            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            evento = eventoDAO.getEventoById(request.getParameter("idEvento"));

            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();
            loggedUser = sessionUserDAO.findLoggedUser();

            request.setAttribute("loggedOn", loggedUser != null);
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

    public static void gotopagamentoAbbonamento(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        Utente loggedUser;
        DAOFactory daoFactory = null;
        Abbonamento abbonamento = new Abbonamento();
        String applicationMessage = null;


        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            EventoDAO eventoDAO = daoFactory.getEventoDAO();

            Evento evento = eventoDAO.getEventoById(request.getParameter("idEvento"));

            abbonamento.setIdAbbonamento(RandomString(10));
            abbonamento.setIdUtente(loggedUser);
            Long prezzo = Long.parseLong(request.getParameter("prezzo"));
            abbonamento.setPrezzo(prezzo);


            String numEntrate = request.getParameter("numEntrate");
            Integer entrate = Integer.parseInt(numEntrate);

            if (entrate > 0) {
                abbonamento.setEntrate(entrate);
                abbonamento.setTipo("Ridotto");
            } else {

                Date dataInizio = evento.getDataInizio();
                Date dataFine = evento.getDataFine();

                LocalDate inizio = dataInizio.toLocalDate();
                LocalDate fine = dataFine.toLocalDate();

                Period period = Period.between(inizio, fine);

                abbonamento.setEntrate(period.getDays() + 1);
                abbonamento.setTipo("Intero");
            }


            sessionDAOFactory.commitTransaction();
            daoFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("evento", evento);
            request.setAttribute("abbonamento", abbonamento);
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

        DAOFactory sessionDAOFactory = null;
        Utente loggedUser;
        DAOFactory daoFactory = null;
        String applicationMessage = null;
        List<Biglietto> biglietti = new ArrayList<>();

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
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

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("evento", evento);
            request.setAttribute("esibizione", esibizione);
            request.setAttribute("biglietti", biglietti);
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

    public static void pagamentoAbbonamento(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        Utente loggedUser;
        String applicationMessage = null;
        DAOFactory daoFactory = null;
        Evento evento;
        Abbonamento abbonamento;


        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
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

            try {
                abbonamentoDAO.createAbbonamento(abbonamento);
                request.setAttribute("viewUrl", "homeManagement/PagamentoSuccesso");
            } catch (Exception e) {
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

            try {
                StringBuilder htmlContent = new StringBuilder();
                htmlContent.append("<html>")
                        .append("<head>")
                        .append("<title>Conferma Acquisto Abbonamento</title>")
                        .append("<style>")
                        .append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7; }")
                        .append(".container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; }")
                        .append(".header { text-align: center; padding: 10px 0; border-bottom: 1px solid #dddddd; }")
                        .append(".header h1 { margin: 0; font-size: 24px; color: #333333; }")
                        .append(".content { padding: 20px 0; }")
                        .append(".ticket { border-bottom: 1px solid #dddddd; padding: 10px 0; }")
                        .append(".ticket:last-child { border-bottom: none; }")
                        .append(".ticket p { margin: 5px 0; }")
                        .append("</style>")
                        .append("</head>")
                        .append("<body>")
                        .append("<div class='container'>")
                        .append("<div class='header'>")
                        .append("<h1>Conferma Acquisto Abbonamento</h1>")
                        .append("</div>")
                        .append("<div class='content'>");


                    htmlContent.append("<div class='ticket'>")
                            .append("<p><strong>ID Abbonamento:</strong> ").append(abbonamento.getIdAbbonamento()).append("</p>")
                            .append("<p><strong>ID Utente:</strong> ").append(abbonamento.getIdUtente().getIdUtente()).append("</p>")
                            .append("<p><strong>Nome Evento:</strong> ").append(abbonamento.getIdEvento().getNome()).append("</p>")
                            .append("<p><strong>Numero entrate:</strong> ").append(abbonamento.getEntrate()).append("</p>")
                            .append("<p><strong>Prezzo:</strong> ").append(abbonamento.getPrezzo()).append("€</p>")
                            .append("<p><strong>Tipo:</strong> ").append(abbonamento.getTipo()).append("</p>")
                            .append("</div>");


                htmlContent.append("</div>")
                        .append("<p>Grazie per aver scelto PrimeEventi!</p>")
                        .append("</div>")
                        .append("</body>")
                        .append("</html>");

                // Convert StringBuilder to String
                String htmlString = htmlContent.toString();

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(loggedUser.getEmail()));
                message.setSubject("Acquisto avvenuto con successo!");

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(htmlString, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                    try {
                        File qrCodeFile = generateQRCodeFile(abbonamento.getIdAbbonamento(), 150, 150);
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.attachFile(qrCodeFile);
                        multipart.addBodyPart(attachmentPart);
                    } catch (WriterException | IOException e) {
                        e.printStackTrace();
                    }


                message.setContent(multipart);

                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
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

    public static void pagamentoBiglietto(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        Utente loggedUser;
        String applicationMessage = null;
        DAOFactory daoFactory = null;
        Evento evento;
        Esibizione esibizione;
        List<Biglietto> biglietti = new ArrayList<>();


        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();


            BigliettoDAO bigliettoDAO = daoFactory.getBigliettoDAO();
            EventoDAO eventoDAO = daoFactory.getEventoDAO();
            EsibizioneDAO esibizioneDAO = daoFactory.getEsibizioneDAO();

            String idEvento = request.getParameter("idEvento");
            String idEsibizione = request.getParameter("idEsibizione");

            evento = eventoDAO.getEventoById(idEvento);
            esibizione = esibizioneDAO.getEsibizioneById(idEsibizione);


            String[] idBiglietti = request.getParameterValues("idBiglietto");
            String[] posti = request.getParameterValues("posto");
            String[] prezzi = request.getParameterValues("prezzo");
            String[] tipi = request.getParameterValues("tipo");


            for (int i = 0; i < idBiglietti.length; i++) {
                Biglietto biglietto = new Biglietto();
                biglietto.setIdBiglietto(idBiglietti[i]);
                biglietto.setIdUtente(loggedUser);
                biglietto.setIdEvento(evento);
                biglietto.setIdEsibizione(esibizione);
                biglietto.setPosto(Integer.parseInt(posti[i]));
                biglietto.setPrezzo(Long.parseLong(prezzi[i]));
                biglietto.setTipo(tipi[i]);
                biglietti.add(biglietto);
            }

            try {
                for (int i = 0; i < biglietti.size(); i++) {
                    bigliettoDAO.createBiglietto(biglietti.get(i));
                }
                request.setAttribute("viewUrl", "homeManagement/PagamentoSuccesso");
            } catch (Exception e) {
                throw new RuntimeException(e);
                //request.setAttribute("viewUrl", "homeManagement/ErrorPage");
            }

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "out.virgilio.it");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String username = "primeevent@virgilio.it";
            String password = "Eventiprimi1!";

            try {
                StringBuilder htmlContent = new StringBuilder();
                htmlContent.append("<html>")
                        .append("<head>")
                        .append("<title>Conferma Acquisto Biglietti</title>")
                        .append("<style>")
                        .append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7; }")
                        .append(".container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; }")
                        .append(".header { text-align: center; padding: 10px 0; border-bottom: 1px solid #dddddd; }")
                        .append(".header h1 { margin: 0; font-size: 24px; color: #333333; }")
                        .append(".content { padding: 20px 0; }")
                        .append(".ticket { border-bottom: 1px solid #dddddd; padding: 10px 0; }")
                        .append(".ticket:last-child { border-bottom: none; }")
                        .append(".ticket p { margin: 5px 0; }")
                        .append("</style>")
                        .append("</head>")
                        .append("<body>")
                        .append("<div class='container'>")
                        .append("<div class='header'>")
                        .append("<h1>Conferma Acquisto Biglietti</h1>")
                        .append("</div>")
                        .append("<div class='content'>");

                for (Biglietto biglietto : biglietti) {
                    htmlContent.append("<div class='ticket'>")
                            .append("<p><strong>ID Biglietto:</strong> ").append(biglietto.getIdBiglietto()).append("</p>")
                            .append("<p><strong>ID Utente:</strong> ").append(biglietto.getIdUtente().getIdUtente()).append("</p>")
                            .append("<p><strong>Nome Evento:</strong> ").append(biglietto.getIdEvento().getNome()).append("</p>")
                            .append("<p><strong>Nome Esibizione:</strong> ").append(biglietto.getIdEsibizione().getNome()).append("</p>")
                            .append("<p><strong>Posto:</strong> ").append(biglietto.getPosto().toString()).append("</p>")
                            .append("<p><strong>Prezzo:</strong> ").append(biglietto.getPrezzo()).append("€</p>")
                            .append("<p><strong>Tipo:</strong> ").append(biglietto.getTipo()).append("</p>")
                            .append("</div>");
                }

                htmlContent.append("</div>")
                        .append("<p>Grazie per aver scelto PrimeEventi!</p>")
                        .append("</div>")
                        .append("</body>")
                        .append("</html>");

                // Convert StringBuilder to String
                String htmlString = htmlContent.toString();

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(loggedUser.getEmail()));
                message.setSubject("Acquisto avvenuto con successo!");

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(htmlString, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                // Aggiungi allegati dei QR code
                for (Biglietto biglietto : biglietti) {
                    try {
                        File qrCodeFile = generateQRCodeFile(biglietto.getIdBiglietto(), 150, 150);
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.attachFile(qrCodeFile);
                        multipart.addBodyPart(attachmentPart);
                    } catch (WriterException | IOException e) {
                        e.printStackTrace();
                    }
                }

                message.setContent(multipart);

                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
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

    private static String RandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }


    public static File generateQRCodeFile(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        File tempFile = File.createTempFile("QRCode_" + text, ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", tempFile.toPath());

        return tempFile;
    }
}

package com.managereventi.managereventi.controller;

import com.managereventi.managereventi.model.dao.*;
import com.managereventi.managereventi.model.mo.Evento;
import com.managereventi.managereventi.model.mo.Organizzatore;
import com.managereventi.managereventi.model.mo.Recensione;
import com.managereventi.managereventi.model.mo.Utente;
import com.managereventi.managereventi.services.Config.Configuration;
import com.managereventi.managereventi.services.Logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeManagement {
    private HomeManagement(){

    }

    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;
        Organizzatore loggedOrganizzatore;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();

            loggedUser = sessionUserDAO.findLoggedUser();
            loggedOrganizzatore = sessionOrganizzatoreDAO.finLoggedOrganizzatore();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null || loggedOrganizzatore!=null);
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

    public static void logon(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
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

            String username = request.getParameter("username");
            String password = request.getParameter("password");


            UtenteDAO userDAO = daoFactory.getUtenteDAO();
            Utente user = userDAO.getUtenteById(username);

            if (user == null || !user.getPassword().equals(password)) {
                sessionUserDAO.deleteUtente(null);
                applicationMessage = "Username e password errati!";
                loggedUser=null;
            } else {
                loggedUser = sessionUserDAO.createUtente(user);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("loggedOrganizzatore", null);
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

    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            OrganizzatoreDAO sessionOrganizzatoreDAO = sessionDAOFactory.getOrganizzatoreDAO();

            sessionUserDAO.deleteUtente(null);
            sessionOrganizzatoreDAO.deleteOrganizzatore(null);

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("loggedOrganizzatore", null);
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

    public static void gotoLogin(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("registration", false);
        request.setAttribute("viewUrl", "homeManagement/Registrazione");
    }

    public static void gotoRegistration(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("registration", true);
        request.setAttribute("viewUrl", "homeManagement/Registrazione");
    }

    public static void usertype(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
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

            //String username = request.getParameter("username");
            //String password = request.getParameter("password");
            String userType = request.getParameter("userType");
            Boolean registration = Boolean.parseBoolean(request.getParameter("registration"));

            //UtenteDAO userDAO = daoFactory.getUtenteDAO();
            //Utente user = userDAO.getUtenteById(username);
/*
            if (user == null || !user.getPassword().equals(password)) {
                sessionUserDAO.deleteUtente(null);
                applicationMessage = "Username e password errati!";
                loggedUser=null;
            } else {
                loggedUser = sessionUserDAO.createUtente(user);
            }*/

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            //request.setAttribute("loggedOn",loggedUser!=null);
           // request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage", applicationMessage);

            if (!registration) {
                switch (userType) {
                    case "azienda":
                        request.setAttribute("viewUrl", "homeManagement/login");
                        break;
                    case "utente":
                        request.setAttribute("viewUrl", "homeManagement/LoginAnna");
                        break;
                    case "organizzatore":
                        request.setAttribute("viewUrl", "adminManagement/loginAdmin");
                        break;
                    default:
                        request.setAttribute("viewUrl", "homeManagement/login");
                        break;
                }
            } else {
                switch (userType) {
                    case "azienda":
                        request.setAttribute("viewUrl", "homeManagement/RegistrazioneAzienda");
                        break;
                    case "utente":
                        request.setAttribute("viewUrl", "homeManagement/RegistrazioneUtente");
                        break;
                    case "organizzatore":
                        request.setAttribute("viewUrl", "adminManagement/RegistrazioneAdmin");
                        break;
                    default:
                        request.setAttribute("viewUrl", "homeManagement/view");
                        break;
                }

            }

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


}

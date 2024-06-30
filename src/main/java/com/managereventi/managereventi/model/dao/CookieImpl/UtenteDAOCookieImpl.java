package com.managereventi.managereventi.model.dao.CookieImpl;

import com.managereventi.managereventi.model.dao.UtenteDAO;
import com.managereventi.managereventi.model.mo.Abbonamento;
import com.managereventi.managereventi.model.mo.Biglietto;
import com.managereventi.managereventi.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public class UtenteDAOCookieImpl implements UtenteDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UtenteDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Utente createUtente(Utente utente) {

        Utente loggedUser = new Utente();
        loggedUser.setIdUtente(utente.getIdUtente());
        loggedUser.setNome(utente.getNome());
        loggedUser.setCognome(utente.getCognome());

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;

    }

    @Override
    public Utente getUtenteById(String idUtente) {
        return null;
    }

    @Override
    public List<Utente> getAllUtenti() {
        return null;
    }

    @Override
    public void updateUtente(Utente utente) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(utente));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void deleteUtente(Utente utente) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public Utente findLoggedUser() {
        Cookie[] cookies = request.getCookies();
        Utente loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;
    }

    @Override
    public List<Biglietto> getBigliettiUtente(Utente utente) {
        return null;
    }

    @Override
    public List<Abbonamento> getAbbonamentiUtente(Utente utente) {
        return null;
    }

    private String encode(Utente loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getIdUtente() + "#" + loggedUser.getNome()+ "#" + loggedUser.getCognome();
        return encodedLoggedUser;

    }

    private Utente decode(String encodedLoggedUser) {

        Utente loggedUser = new Utente();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setIdUtente(values[0]);
        loggedUser.setNome(values[1]);
        loggedUser.setCognome(values[2]);

        return loggedUser;

    }
}

package com.managereventi.managereventi.model.dao.CookieImpl;

import com.managereventi.managereventi.model.dao.OrganizzatoreDAO;
import com.managereventi.managereventi.model.mo.Organizzatore;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public class OrganizzatoreDAOCookieImpl implements OrganizzatoreDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public OrganizzatoreDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    @Override
    public Organizzatore createOrganizzatore(Organizzatore organizzatore) {

        Organizzatore loggedUser = new Organizzatore();
        loggedUser.setIdOrganizzatore(organizzatore.getIdOrganizzatore());
        loggedUser.setNome(organizzatore.getNome());
        loggedUser.setCognome(organizzatore.getCognome());

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;

    }

    @Override
    public Organizzatore getOrganizzatoreById(String idOrganizzatore) {
        return null;
    }

    @Override
    public List<Organizzatore> getAllOrganizzatori() {
        return null;
    }

    @Override
    public void updateOrganizzatore(Organizzatore organizzatore) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(organizzatore));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void deleteOrganizzatore(String idOrganizzatore) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public Organizzatore finLoggedOrganizzatore() {

        Cookie[] cookies = request.getCookies();
        Organizzatore loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;

    }

    private String encode(Organizzatore loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getIdOrganizzatore() + "#" + loggedUser.getNome()+ "#" + loggedUser.getCognome();
        return encodedLoggedUser;

    }

    private Organizzatore decode(String encodedLoggedUser) {

        Organizzatore loggedUser = new Organizzatore();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setIdOrganizzatore(values[0]);
        loggedUser.setNome(values[1]);
        loggedUser.setCognome(values[2]);

        return loggedUser;

    }
}

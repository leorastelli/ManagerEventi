package com.managereventi.managereventi.model.dao.CookieImpl;

import com.managereventi.managereventi.model.dao.AziendaDAO;
import com.managereventi.managereventi.model.mo.Azienda;
import com.managereventi.managereventi.model.mo.Utente;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class AziendaDAOCookieImpl implements AziendaDAO {

    HttpServletRequest request;
    HttpServletResponse response;
    public AziendaDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Azienda createAzienda(Azienda azienda) {

        Azienda loggedUser = new Azienda();
        loggedUser.setPartitaIVA(azienda.getPartitaIVA());
        loggedUser.setNome(azienda.getNome());

        Cookie cookie;
        cookie = new Cookie("loggedAzienda", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;
    }

    @Override
    public Azienda getAziendaByPartitaIVA(String partitaIVA) {
        return null;
    }

    @Override
    public List<Azienda> getAllAziende() {
        return null;
    }

    @Override
    public void updateAzienda(Azienda azienda) {

        Cookie cookie;
        cookie = new Cookie("loggedAzienda", encode(azienda));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void deleteAzienda(String partitaIVA) {

        Cookie cookie;
        cookie = new Cookie("loggedAzienda", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);


    }

    @Override
    public Azienda findLoggedUser() {
        return null;
    }


    private String encode(Azienda loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getPartitaIVA() + "#" + loggedUser.getNome();
        encodedLoggedUser = encodedLoggedUser.replaceAll("\\s", "");
        return encodedLoggedUser;

    }

    private Azienda decode(String encodedLoggedUser) {

        Azienda loggedUser = new Azienda();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setPartitaIVA(values[0]);
        loggedUser.setNome(values[1]);

        return loggedUser;

    }
}

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
        loggedUser.setIndirizzo(azienda.getIndirizzo());
        loggedUser.setTelefono(azienda.getTelefono());
        loggedUser.setEmail(azienda.getEmail());
        loggedUser.setPassword(azienda.getPassword());
        loggedUser.setCap(azienda.getCap());
        loggedUser.setCitta(azienda.getCitta());
        loggedUser.setProvincia(azienda.getProvincia());
        loggedUser.setStato(azienda.getStato());

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

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("loggedAzienda")) {
                        return decode(cookie.getValue());
                    }
                }
            }

            return null;
    }


    private String encode(Azienda loggedUser) {

        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getPartitaIVA() + "#" + loggedUser.getNome() + "#" +
                loggedUser.getIndirizzo() + "#" + loggedUser.getTelefono() + "#" + loggedUser.getEmail() + "#" +
                loggedUser.getPassword() + "#" + loggedUser.getCap() + "#" + loggedUser.getCitta() + "#" +
                loggedUser.getProvincia() + "#" + loggedUser.getStato();
        encodedLoggedUser = encodedLoggedUser.replaceAll("\\s", "");
        return encodedLoggedUser;

    }

    private Azienda decode(String encodedLoggedUser) {

        Azienda loggedUser = new Azienda();

        String[] values = encodedLoggedUser.split("#");

        loggedUser.setPartitaIVA(values[0]);
        loggedUser.setNome(values[1]);
        loggedUser.setIndirizzo(values[2]);
        loggedUser.setTelefono(values[3]);
        loggedUser.setEmail(values[4]);
        loggedUser.setPassword(values[5]);
        loggedUser.setCap(values[6]);
        loggedUser.setCitta(values[7]);
        loggedUser.setProvincia(values[8]);
        loggedUser.setStato(values[9]);


        return loggedUser;

    }
}

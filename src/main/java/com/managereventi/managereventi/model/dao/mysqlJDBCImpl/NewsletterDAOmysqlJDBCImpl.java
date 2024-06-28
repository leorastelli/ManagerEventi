package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.LuogoDAO;
import com.managereventi.managereventi.model.dao.NewsletterDAO;
import com.managereventi.managereventi.model.mo.Evento;
import com.managereventi.managereventi.model.mo.Luogo;
import com.managereventi.managereventi.model.mo.Newsletter;
import com.managereventi.managereventi.model.mo.Utente;

import javax.lang.model.element.NestingKind;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class NewsletterDAOmysqlJDBCImpl implements NewsletterDAO {

    Connection conn;

    public NewsletterDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Newsletter read(ResultSet rs) {
        Newsletter newsletter = new Newsletter();
        Utente utente = new Utente();
        Evento evento = new Evento();


        try {

            newsletter.getIdUtente().setIdUtente(rs.getString("IdUtente"));
            newsletter.getIdEvento().setIdEvento(rs.getString("IdEvento"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return newsletter;
    }


    @Override
    public Newsletter subscribeToNewsletter(String idUtente, String idEvento) {

        PreparedStatement ps;

        Newsletter newsletter = null;

        try {
            String sql = "INSERT INTO Newsletter (IdUtente, IdEvento) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idUtente);
            ps.setString(2, idEvento);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void unsubscribeFromNewsletter(String idUtente, String idEvento) {

        PreparedStatement ps;

        try {
            String sql = "DELETE FROM Newsletter WHERE IdUtente = ? AND IdEvento = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idUtente);
            ps.setString(2, idEvento);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public boolean isSubscribed(String idUtente, String idEvento) {
        return false;
    }

    @Override
    public List<String> getSubscribedEvents(String idUtente) {
        return null;
    }

    @Override
    public List<String> getSubscribers(String idEvento) {
        return null;
    }
}

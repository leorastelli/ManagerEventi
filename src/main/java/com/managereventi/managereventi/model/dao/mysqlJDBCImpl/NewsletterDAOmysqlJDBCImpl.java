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

        try {

            newsletter.getIdUtente().setIdUtente(rs.getString("IdUtente"));
            newsletter.setEmail(rs.getString("Email"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return newsletter;
    }


    @Override
    public Newsletter subscribeToNewsletter(String idUtente, String email) {

        PreparedStatement ps;

        Newsletter newsletter = null;

        try {
            String sql = "INSERT INTO Newsletter (IdUtente, Email) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idUtente);
            ps.setString(2, email);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void unsubscribeFromNewsletter(String idUtente, String Email) {

        PreparedStatement ps;

        try {
            String sql = "DELETE FROM Newsletter WHERE IdUtente = ? AND Email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idUtente);
            ps.setString(2, Email);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public boolean isSubscribed(String idUtente, String Email) {
        return false;
    }


    @Override
    public List<String> getMailList(String idUtente) {
        PreparedStatement ps;
        List<String> mailList = null;

        try {
            String sql = "SELECT Email FROM Newsletter WHERE IdUtente = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idUtente);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mailList.add(rs.getString("Email"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mailList;
    }
}

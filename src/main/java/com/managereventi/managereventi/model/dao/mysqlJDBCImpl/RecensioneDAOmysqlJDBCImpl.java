package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.NewsletterDAO;
import com.managereventi.managereventi.model.dao.RecensioneDAO;
import com.managereventi.managereventi.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class RecensioneDAOmysqlJDBCImpl implements RecensioneDAO {

    Connection conn;

    public RecensioneDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Recensione read(ResultSet rs) {
        Recensione recensione = new Recensione();
        Utente utente = new Utente();
        Evento evento = new Evento();
        Esibizione esibizione = new Esibizione();

        try {

            recensione.setIdRecensione(rs.getString("IdRecensione"));
            recensione.setDescrizione(rs.getString("Descrizione"));
            recensione.setStelle(rs.getInt("Stelle"));
            recensione.getIdUtente().setIdUtente(rs.getString("IdUtente"));
            recensione.getIdEsibizione().setIdEsibizione(rs.getString("IdEsibizione"));
            recensione.getIdEvento().setIdEvento(rs.getString("IdEvento"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return recensione;
    }

    @Override
    public Recensione createRecensione(Recensione recensione) {
        PreparedStatement ps;

        try{
            String sql = "INSERT INTO Recensione (IdRecensione, Descrizione, Stelle, IdUtente, IdEsibizione, IdEvento) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, recensione.getIdRecensione());
            ps.setString(2, recensione.getDescrizione());
            ps.setInt(3, recensione.getStelle());
            ps.setString(4, recensione.getIdUtente().getIdUtente());
            ps.setString(5, recensione.getIdEsibizione().getIdEsibizione());
            ps.setString(6, recensione.getIdEvento().getIdEvento());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return recensione;
    }

    @Override
    public Recensione getRecensioneById(String idRecensione) {
        return null;
    }

    @Override
    public List<Recensione> getRecensioniByUtente(String idUtente) {
        return null;
    }

    @Override
    public List<Recensione> getRecensioniByEsibizione(String idEsibizione) {
        return null;
    }

    @Override
    public List<Recensione> getRecensioniByEvento(String idEvento) {
        return null;
    }

    @Override
    public void updateRecensione(Recensione recensione) {

        PreparedStatement ps;

        try{
            String sql = "UPDATE Recensione SET Descrizione = ?, Stelle = ? WHERE IdRecensione = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, recensione.getDescrizione());
            ps.setInt(2, recensione.getStelle());
            ps.setString(3, recensione.getIdRecensione());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void deleteRecensione(String idRecensione) {

        PreparedStatement ps;

        try{
            String sql = "DELETE FROM Recensione WHERE IdRecensione = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idRecensione);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

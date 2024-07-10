package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.DomandaDAO;
import com.managereventi.managereventi.model.mo.Domanda;
import com.managereventi.managereventi.model.mo.Risposta;
import com.managereventi.managereventi.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DomandaDAOmysqlJDBCImpl implements DomandaDAO {

    Connection conn;

    public DomandaDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }
    @Override
    public Domanda createDomanda(Domanda domanda) {
        PreparedStatement ps;
        try {
            String query = "INSERT INTO Domanda (Descrizione, IdUtente) VALUES (?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, domanda.getDescrizione());
            ps.setString(2, domanda.getUtente().getIdUtente());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return domanda;
    }

    @Override
    public Domanda getDomandaById(String idDomanda) {
        PreparedStatement ps;
        ResultSet rs;
        Domanda domanda = new Domanda();

        try {
            String query = "SELECT * FROM Domanda WHERE IdDomanda = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, idDomanda);
            rs = ps.executeQuery();
            if (rs.next()) {
                domanda = read(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return domanda;
    }

    @Override
    public List<Domanda> getAllDomande() {

        PreparedStatement ps;
        ResultSet rs;
        List<Domanda> domande = new ArrayList<>();
        try {
            String query = "SELECT * FROM Domanda";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Domanda domanda = read(rs);
                domande.add(domanda);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return domande;
    }

    @Override
    public void updateDomanda(Domanda domanda) {

    }

    @Override
    public void deleteDomanda(String idDomanda) {

    }

    @Override
    public List<Domanda> getDomandeUtente(Utente utente) {
        return null;
    }

    @Override
    public List<Risposta> getRisposteDomanda(Domanda domanda) {
        PreparedStatement ps;
        ResultSet rs;
        List<Risposta> risposte = new ArrayList<>();

        try {
            String query = "SELECT * FROM Risposta WHERE IdDomanda = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, domanda.getIdDomanda());
            rs = ps.executeQuery();
            while (rs.next()) {
                Risposta risposta = new Risposta();
                Utente utente = new Utente();
                risposta.setUtente(utente);
                risposta.setDomanda(domanda);
                risposta.setIdRisposta(rs.getInt("IdRisposta"));
                risposta.setDescrizione(rs.getString("Descrizione"));
                risposta.getUtente().setIdUtente(rs.getString("IdUtente"));
                risposta.getDomanda().setIdDomanda(rs.getInt("IdDomanda"));
                risposte.add(risposta);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return risposte;

    }

    @Override
    public String getMailUtente(Domanda domanda) {
        PreparedStatement ps;
        ResultSet rs;
        String mail = null;

        try {
            String query = "SELECT Email FROM Domanda as d join Utente as u on d.IdUtente = u.IdUtente" +
                    " WHERE IdDomanda = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, domanda.getIdDomanda());
            rs = ps.executeQuery();
            if (rs.next()) {
                mail = rs.getString("Email");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mail;
    }

    Domanda read(ResultSet rs) {
        Domanda domanda = new Domanda();
        Utente utente = new Utente();
        domanda.setUtente(utente);
        try {
            domanda.setIdDomanda(rs.getInt("IdDomanda"));
            domanda.setDescrizione(rs.getString("Descrizione"));
            domanda.getUtente().setIdUtente(rs.getString("IdUtente"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return domanda;
    }
}

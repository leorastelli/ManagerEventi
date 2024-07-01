package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.AbbonamentoDAO;
import com.managereventi.managereventi.model.mo.Abbonamento;
import com.managereventi.managereventi.model.mo.Evento;
import com.managereventi.managereventi.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AbbonamentoDAOmysqlJDBCImpl implements AbbonamentoDAO {

    Connection conn;

    public AbbonamentoDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Abbonamento createAbbonamento(Abbonamento abbonamento) {

        PreparedStatement ps;

        try {

            String sql
                    = " INSERT INTO abbonamento "
                    + "   (IdAbbonamento, Tipo, Prezzo, Durata, IdUtente,IdEvento ) "
                    + " VALUES (?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, abbonamento.getIdAbbonamento());
            ps.setString(2, abbonamento.getTipo());
            ps.setDouble(3, abbonamento.getPrezzo());
            ps.setInt(4, abbonamento.getEntrate());
            ps.setString(5, abbonamento.getIdUtente().getIdUtente());
            ps.setString(6, abbonamento.getIdEvento().getIdEvento());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return abbonamento;
    }

    @Override
    public Abbonamento getAbbonamentoById(String idAbbonamento) {
        PreparedStatement ps;
        Abbonamento abbonamento = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM abbonamento "
                    + " WHERE "
                    + "   IdAbbonamento = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idAbbonamento);

            ps.executeQuery();

            if (ps.getResultSet().next()) {
                abbonamento = read(ps.getResultSet());
            }

            ps.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return abbonamento;
    }

    @Override
    public List<Abbonamento> getAllAbbonamenti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateAbbonamento(Abbonamento abbonamento) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteAbbonamento(String idAbbonamento) {
        PreparedStatement ps;

        try {
            String sql
                    = " DELETE FROM Abbonamento "
                    + " WHERE IdAbbonamento = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idAbbonamento);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Abbonamento read(ResultSet rs) {
        Abbonamento abbonamento = new Abbonamento();
        Utente utente = new Utente();
        Evento evento = new Evento();
        abbonamento.setIdUtente(utente);
        abbonamento.setIdEvento(evento);

        try {
            abbonamento.setIdAbbonamento(rs.getString("IdAbbonamento"));
            abbonamento.setTipo(rs.getString("Tipo"));
            abbonamento.setPrezzo(rs.getLong("Prezzo"));
            abbonamento.setEntrate(rs.getInt("Entrate"));
            abbonamento.getIdUtente().setIdUtente(rs.getString("IdUtente"));
            abbonamento.getIdEvento().setIdEvento(rs.getString("IdEvento"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return abbonamento;
    }

    public List<Abbonamento> getAbbonamentiUtente(Utente utente) {
        PreparedStatement ps;
        List<Abbonamento> abbonamenti = new ArrayList<>();

        try {
            String sql
                    = " SELECT * "
                    + " FROM Abbonamento "
                    + " WHERE IdUtente = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, utente.getIdUtente());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Abbonamento abbonamento = new Abbonamento();
                abbonamento = read(rs);
                abbonamenti.add(abbonamento);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return abbonamenti;
    }
}

package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.DomandaDAO;
import com.managereventi.managereventi.model.dao.RispostaDAO;
import com.managereventi.managereventi.model.mo.Domanda;
import com.managereventi.managereventi.model.mo.Risposta;
import com.managereventi.managereventi.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class RispostaDAOmysqlJDBCImpl implements RispostaDAO {

    Connection conn;

    public RispostaDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Risposta read(ResultSet rs) {

        Risposta risposta = new Risposta();
        Utente utente = new Utente();
        Domanda domanda = new Domanda();
        risposta.setUtente(utente);
        risposta.setDomanda(domanda);

        try {

            risposta.setIdRisposta(rs.getInt("IdRisposta"));
            risposta.setDescrizione(rs.getString("Descrizione"));
            risposta.getUtente().setIdUtente(rs.getString("IdUtente"));
            risposta.getDomanda().setIdDomanda(rs.getInt("IdDomanda"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return risposta;
    }

    @Override
    public Risposta createRisposta(Risposta risposta) {
        PreparedStatement ps;
        try {
            String query = "INSERT INTO Risposta (Descrizione, IdUtente, IdDomanda) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, risposta.getDescrizione());
            ps.setString(2, risposta.getUtente().getIdUtente());
            ps.setInt(3, risposta.getDomanda().getIdDomanda());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return risposta;
    }

    @Override
    public Risposta getRispostaById(String idRisposta) {
        return null;
    }

    @Override
    public List<Risposta> getAllRisposte() {
        return null;
    }

    @Override
    public void updateRisposta(Risposta risposta) {

    }

    @Override
    public void deleteRisposta(String idRisposta) {

    }

    @Override
    public List<Risposta> getRisposteUtente(Utente utente) {
        return null;
    }

    @Override
    public List<Risposta> getRisposteDomanda(Domanda domanda) {
        return null;
    }
}

package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.CandidatureDAO;
import com.managereventi.managereventi.model.mo.Biglietto;
import com.managereventi.managereventi.model.mo.Candidature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CandidatureDAOmysqlJDBCImpl implements CandidatureDAO {

    Connection conn;

    public CandidatureDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Candidature read(ResultSet rs){
        Candidature candidature = new Candidature();

        try{
            candidature.setId(rs.getInt("Id"));
            candidature.setNome(rs.getString("Nome"));
            candidature.setCognome(rs.getString("Cognome"));
            candidature.setEmail(rs.getString("Email"));
            candidature.setPosizione(rs.getString("Posizione"));
            candidature.setDescrizione(rs.getString("Descrizione"));
            candidature.setDataNascita(rs.getDate("DataNascita"));
            candidature.setTelefono(rs.getString("Telefono"));
            candidature.setCitta(rs.getString("Citta"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return candidature;
    }

    @Override
    public Candidature createCandidature(Candidature candidature) {
        PreparedStatement ps;

        try{
            String sql = "INSERT INTO Candidature " +
                    "(Nome, Cognome, Email, Posizione, Descrizione, DataNascita, Telefono, Citta) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, candidature.getNome());
            ps.setString(2, candidature.getCognome());
            ps.setString(3, candidature.getEmail());
            ps.setString(4, candidature.getPosizione());
            ps.setString(5, candidature.getDescrizione());
            ps.setDate(6, candidature.getDataNascita());
            ps.setString(7, candidature.getTelefono());
            ps.setString(8, candidature.getCitta());

            ps.executeUpdate();


        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return candidature;
    }

    @Override
    public Candidature getCandidatureById(String idCandidature) {
        return null;
    }

    @Override
    public List<Candidature> getCandidatureByPosizione(String Posizione) {
        PreparedStatement ps;
        List<Candidature> candidature = null;

        try{
            String sql = "SELECT * FROM Candidature WHERE Posizione = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, Posizione);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Candidature cand = new Candidature();
                cand = read(rs);
                candidature.add(cand);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return candidature;
    }

    @Override
    public void updateCandidature(Candidature candidature) {

    }

    @Override
    public void deleteCandidature(String idCandidature) {

    }

    @Override
    public List<Candidature> findAll() {
        PreparedStatement ps;
        List<Candidature> candidature = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Candidature";
            ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Candidature cand = new Candidature();
                cand = read(rs);
                candidature.add(cand);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return candidature;
    }
}

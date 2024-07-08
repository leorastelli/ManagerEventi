package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.RecensioneDAO;
import com.managereventi.managereventi.model.dao.SponsorizzazioneDAO;
import com.managereventi.managereventi.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SponsorizzazioneDAOmysqlJDBCImpl implements SponsorizzazioneDAO {

    Connection conn;

    public SponsorizzazioneDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Sponsorizzazione read(ResultSet rs) {
        Sponsorizzazione sponsorizzazione = new Sponsorizzazione();
        Azienda azienda = new Azienda();
        Evento evento = new Evento();

        sponsorizzazione.setPartitaIVA(azienda);
        sponsorizzazione.setIdEvento(evento);


        try {
            sponsorizzazione.getPartitaIVA().setPartitaIVA(rs.getString("PartitaIVA"));
            sponsorizzazione.getIdEvento().setIdEvento(rs.getString("IdEvento"));
            sponsorizzazione.setLogo(rs.getBlob("Logo"));
            sponsorizzazione.setCosto(rs.getLong("Costo"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sponsorizzazione;
    }

    @Override
    public Sponsorizzazione createSponsorizzazione(Sponsorizzazione sponsorizzazione) {
        PreparedStatement ps;

        try{
            String sql = "INSERT INTO Sponsorizzazione (PartitaIVA, IdEvento, Logo, Costo) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, sponsorizzazione.getPartitaIVA().getPartitaIVA());
            ps.setString(2, sponsorizzazione.getIdEvento().getIdEvento());
            ps.setBlob(3, sponsorizzazione.getLogo());
            ps.setLong(4, sponsorizzazione.getCosto());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sponsorizzazione;
    }

    @Override
    public Sponsorizzazione getSponsorizzazione(String partitaIVA, String idEvento) {
        return null;
    }

    @Override
    public List<Sponsorizzazione> getSponsorizzazioniByPartitaIVA(String partitaIVA) {

        PreparedStatement ps;

        List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Sponsorizzazione WHERE PartitaIVA = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, partitaIVA);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                sponsorizzazioni.add(read(rs));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sponsorizzazioni;
    }

    @Override
    public List<Sponsorizzazione> getSponsorizzazioniByEvento(String idEvento) {
        PreparedStatement ps;


        List<Sponsorizzazione> sponsorizzazioni = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Sponsorizzazione WHERE IdEvento = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idEvento);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                sponsorizzazioni.add(read(rs));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sponsorizzazioni;
    }

    @Override
    public void updateSponsorizzazione(Sponsorizzazione sponsorizzazione) {

        PreparedStatement ps;

        try{
            String sql = "UPDATE Sponsorizzazione SET Logo = ?, Costo = ? WHERE PartitaIVA = ? AND IdEvento = ?";
            ps = conn.prepareStatement(sql);
            ps.setBlob(1, sponsorizzazione.getLogo());
            ps.setLong(2, sponsorizzazione.getCosto());
            ps.setString(3, sponsorizzazione.getPartitaIVA().getPartitaIVA());
            ps.setString(4, sponsorizzazione.getIdEvento().getIdEvento());

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);


        }

    }

    @Override
    public void deleteSponsorizzazione(String partitaIVA, String idEvento) {

        PreparedStatement ps;

        try{
            String sql = "DELETE FROM Sponsorizzazione WHERE PartitaIVA = ? AND IdEvento = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, partitaIVA);
            ps.setString(2, idEvento);

            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

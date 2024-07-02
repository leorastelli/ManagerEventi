package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.AbbonamentoDAO;
import com.managereventi.managereventi.model.dao.BigliettoDAO;
import com.managereventi.managereventi.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BigliettoDAOmysqlJDBCImpl implements BigliettoDAO {

    Connection conn;

    public BigliettoDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Biglietto read(ResultSet rs) {
        Biglietto biglietto = new Biglietto();
        Utente utente = new Utente();
        Evento evento = new Evento();
        Esibizione esibizione = new Esibizione();
        biglietto.setIdUtente(utente);
        biglietto.setIdEsibizione(esibizione);
        biglietto.setIdEvento(evento);

        try {

            biglietto.setIdBiglietto(rs.getString("IdBiglietto"));
            biglietto.setPrezzo(rs.getLong("Prezzo"));
            biglietto.setTipo(rs.getString("Tipo"));
            biglietto.setStato(rs.getInt("Stato"));
            biglietto.getIdUtente().setIdUtente(rs.getString("IdUtente"));
            biglietto.getIdEvento().setIdEvento(rs.getString("IdEvento"));
            biglietto.getIdEsibizione().setIdEsibizione(rs.getString("IdEsibizione"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return biglietto;
    }

    Biglietto read1(ResultSet rs) {
        Biglietto biglietto = new Biglietto();
        Utente utente = new Utente();
        Evento evento = new Evento();
        Esibizione esibizione = new Esibizione();
        biglietto.setIdUtente(utente);
        biglietto.setIdEvento(evento);
        biglietto.setIdEsibizione(esibizione);

        try {
            biglietto.setIdBiglietto(rs.getString("IdBiglietto"));
            biglietto.setPrezzo(rs.getLong("Prezzo"));
            biglietto.setTipo(rs.getString("Tipo"));
            biglietto.setStato(rs.getInt("Stato"));
            biglietto.getIdUtente().setIdUtente(rs.getString("IdUtente"));

            // Assuming these fields exist in the ResultSet
            biglietto.getIdEvento().setIdEvento(rs.getString("IdEvento"));
            biglietto.getIdEvento().setNome(rs.getString("NomeEvento"));
            biglietto.getIdEvento().setDataInizio(rs.getDate("DataInizioEvento"));

            biglietto.getIdEsibizione().setIdEsibizione(rs.getString("IdEsibizione"));
            biglietto.getIdEsibizione().setNome(rs.getString("NomeEsibizione"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return biglietto;
    }

    @Override
    public Biglietto createBiglietto(Biglietto biglietto) {
       PreparedStatement ps;

       try{
              String sql
                     = " INSERT INTO biglietto "
                     + "   (IdBiglietto, Prezzo, Tipo, Stato, IdUtente, IdEvento, IdEsibizione) "
                     + " VALUES (?,?,?,?,?,?,?)";

              ps = conn.prepareStatement(sql);
              ps.setString(1, biglietto.getIdBiglietto());
              ps.setLong(2, biglietto.getPrezzo());
              ps.setString(3, biglietto.getTipo());
              ps.setInt(4, biglietto.getStato());
              ps.setString(5, biglietto.getIdUtente().getIdUtente());
              ps.setString(6, biglietto.getIdEvento().getIdEvento());
              ps.setString(7, biglietto.getIdEsibizione().getIdEsibizione());

              ps.executeUpdate();

         } catch (Exception e) {
              throw new RuntimeException(e);
       }

         return biglietto;
    }

    @Override
    public Biglietto getBigliettoById(String idBiglietto) {
        return null;
    }

    @Override
    public List<Biglietto> getAllBiglietti() {
        return null;
    }

    @Override
    public void updateBiglietto(Biglietto biglietto) {

        PreparedStatement ps;

        try{
            String sql
                    = " UPDATE biglietto "
                    + " SET "
                    + "   Prezzo = ?,"
                    + "   Tipo = ?,"
                    + "   Stato = ?,"
                    + "   IdUtente = ?,"
                    + "   IdEvento = ?,"
                    + "   IdEsibizione = ?"
                    + " WHERE "
                    + "   IdBiglietto = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, biglietto.getPrezzo());
            ps.setString(2, biglietto.getTipo());
            ps.setInt(3, biglietto.getStato());
            ps.setString(4, biglietto.getIdUtente().getIdUtente());
            ps.setString(5, biglietto.getIdEvento().getIdEvento());
            ps.setString(6, biglietto.getIdEsibizione().getIdEsibizione());
            ps.setString(7, biglietto.getIdBiglietto());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    public List<Biglietto> getBigliettiUtente(Utente utente) {

        PreparedStatement ps;
        List<Biglietto> biglietti = new ArrayList<>();

        try {
            String sql
                    = " SELECT b.*, e.Nome as NomeEvento, e.DataInizio as DataInizioEvento, es.Nome as NomeEsibizione "
                    + " FROM Biglietto b "
                    + " JOIN Evento e ON b.IdEvento = e.IdEvento "
                    + " JOIN Esibizione es ON b.IdEsibizione = es.IdEsibizione "
                    + " WHERE b.IdUtente = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, utente.getIdUtente());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Biglietto biglietto = new Biglietto();
                biglietto = read1(rs);
                biglietti.add(biglietto);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return biglietti;
    }

    @Override
    public List<String> getIdEventiUtente(Utente utente) {
        PreparedStatement ps;
        List<String> idEventi = new ArrayList<>();

        try {
            String sql
                    = " SELECT IdEvento "
                    + " FROM Biglietto "
                    + " WHERE IdUtente = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, utente.getIdUtente());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                idEventi.add(rs.getString("IdEvento"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return idEventi;
    }

    @Override
    public void deleteBiglietto(String idBiglietto) {
        PreparedStatement ps;

        try {
            String sql
                    = " DELETE FROM biglietto "
                    + " WHERE IdBiglietto = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idBiglietto);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

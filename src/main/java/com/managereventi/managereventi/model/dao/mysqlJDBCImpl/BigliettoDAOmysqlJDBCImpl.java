package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.AbbonamentoDAO;
import com.managereventi.managereventi.model.dao.BigliettoDAO;
import com.managereventi.managereventi.model.mo.Abbonamento;
import com.managereventi.managereventi.model.mo.Biglietto;
import com.managereventi.managereventi.model.mo.Evento;
import com.managereventi.managereventi.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    @Override
    public void deleteBiglietto(String idBiglietto) {

    }
}

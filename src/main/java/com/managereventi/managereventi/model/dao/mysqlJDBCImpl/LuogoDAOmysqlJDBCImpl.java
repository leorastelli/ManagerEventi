package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.LuogoDAO;
import com.managereventi.managereventi.model.mo.Luogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class LuogoDAOmysqlJDBCImpl implements LuogoDAO {

    Connection conn;

    public LuogoDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Luogo read(ResultSet rs) {
        Luogo luogo = new Luogo();


        try {
            luogo.setIdLuogo(rs.getString("IdLuogo"));
            luogo.setVia(rs.getString("Via"));
            luogo.setNumCivico(rs.getInt("NumCivico"));
            luogo.setCap(rs.getString("Cap"));
            luogo.setProvincia(rs.getString("Provincia"));
            luogo.setStato(rs.getString("Stato"));
            luogo.setCittà(rs.getString("Città"));
            luogo.setCapienza(rs.getInt("Capienza"));
            luogo.setTipologia(rs.getString("Tipologia"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return luogo;
    }


    @Override
    public Luogo createLuogo(Luogo luogo) {

            PreparedStatement ps;

            try {
                String sql
                        = " INSERT INTO luogo "
                        + "   (IdLuogo, Via, NumCivico, Cap, Provincia, Stato, Città, Capienza, Tipologia) "
                        + " VALUES (?,?,?,?,?,?,?,?,?)";

                ps = conn.prepareStatement(sql);
                ps.setString(1, luogo.getIdLuogo());
                ps.setString(2, luogo.getVia());
                ps.setInt(3, luogo.getNumCivico());
                ps.setString(4, luogo.getCap());
                ps.setString(5, luogo.getProvincia());
                ps.setString(6, luogo.getStato());
                ps.setString(7, luogo.getCittà());
                ps.setInt(8, luogo.getCapienza());
                ps.setString(9, luogo.getTipologia());

                ps.executeUpdate();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return luogo;
    }

    @Override
    public Luogo getLuogoById(String idLuogo) {
        return null;
    }


    @Override
    public List<Luogo> getAllLuoghi() {
        return null;
    }

    @Override
    public void updateLuogo(Luogo luogo) {

    }

    @Override
    public void deleteLuogo(String idLuogo) {

    }
}

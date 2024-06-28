package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.EsibizioneDAO;
import com.managereventi.managereventi.model.dao.EventoDAO;
import com.managereventi.managereventi.model.mo.Esibizione;
import com.managereventi.managereventi.model.mo.Evento;
import com.managereventi.managereventi.model.mo.Luogo;
import com.managereventi.managereventi.model.mo.Organizzatore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class EventoDAOmysqlJDBCImpl implements EventoDAO {

    Connection conn;

    public EventoDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Evento read(ResultSet rs) {
        Evento evento = new Evento();
        Organizzatore organizzatore = new Organizzatore();

        try {
            evento.setIdEvento(rs.getString("IdEvento"));
            evento.setNome(rs.getString("Nome"));
            evento.setDescrizione(rs.getString("Descrizione"));
            evento.setDataInizio(rs.getDate("DataInizio"));
            evento.setDataFine(rs.getDate("DataFine"));
            evento.setNumEsibizioni(rs.getInt("NumEsibizioni"));
            evento.getOrganizzatore().setIdOrganizzatore(rs.getString("IdOrganizzatore"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return evento;
    }

    @Override
    public Evento createEvento(Evento evento) {
        PreparedStatement ps;

        try{
            String sql
                    = " INSERT INTO evento "
                    + "   (IdEvento, Nome, Descrizione, DataInizio, DataFine, NumEsibizioni, IdOrganizzatore) "
                    + " VALUES (?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, evento.getIdEvento());
            ps.setString(2, evento.getNome());
            ps.setString(3, evento.getDescrizione());
            ps.setDate(4, evento.getDataInizio());
            ps.setDate(5, evento.getDataFine());
            ps.setInt(6, evento.getNumEsibizioni());
            ps.setString(7, evento.getOrganizzatore().getIdOrganizzatore());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return evento;
    }

    @Override
    public Evento getEventoById(String idEvento) {
        return null;
    }

    @Override
    public List<Evento> getAllEventi() {
        return null;
    }

    @Override
    public void updateEvento(Evento evento) {

        PreparedStatement ps;

        try{
            String sql
                    = " UPDATE evento "
                    + " SET "
                    + "   Nome = ?,"
                    + "   Descrizione = ?,"
                    + "   DataInizio = ?,"
                    + "   DataFine = ?,"
                    + "   NumEsibizioni = ?,"
                    + "   IdOrganizzatore = ?"
                    + " WHERE "
                    + "   IdEvento = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, evento.getNome());
            ps.setString(2, evento.getDescrizione());
            ps.setDate(3, evento.getDataInizio());
            ps.setDate(4, evento.getDataFine());
            ps.setInt(5, evento.getNumEsibizioni());
            ps.setString(6, evento.getOrganizzatore().getIdOrganizzatore());
            ps.setString(7, evento.getIdEvento());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteEvento(String idEvento) {

        PreparedStatement ps;

        try{
            String sql
                    = " DELETE FROM evento "
                    + " WHERE "
                    + "   IdEvento = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idEvento);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

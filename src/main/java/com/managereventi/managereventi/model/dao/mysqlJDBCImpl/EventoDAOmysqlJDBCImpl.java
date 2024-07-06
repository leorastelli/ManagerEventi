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
import java.util.ArrayList;
import java.util.List;

public class EventoDAOmysqlJDBCImpl implements EventoDAO {

    Connection conn;

    public EventoDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Evento read(ResultSet rs) {
        Evento evento = new Evento();
        Organizzatore organizzatore = new Organizzatore();
        evento.setOrganizzatore(organizzatore);

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
        PreparedStatement ps;
        Evento evento = null;

        try{
            String sql = " SELECT * "
                    + " FROM evento "
                    + " WHERE "
                    + "   IdEvento = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idEvento);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                evento = read(rs);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return evento;
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
                    = " UPDATE evento "
                    + " SET "
                    + "   deleted = 'Y'"
                    + " WHERE "
                    + "   IdEvento = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idEvento);
            ps.executeUpdate();

            String sql2
                    = " UPDATE esibizione "
                    + " SET "
                    + "   deleted = 'Y'"
                    + " WHERE "
                    + "   IdEvento = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql2);
            ps.setString(1, idEvento);
            ps.executeUpdate();

            String sql3 = "Update abbonamento set deleted = 'Y' where IdEvento = ? and deleted = 'N'";
            ps = conn.prepareStatement(sql3);
            ps.setString(1, idEvento);
            ps.executeUpdate();

            String sql4 = "Update biglietto set stato = 0 where IdEvento = ? and stato = 1";
            ps = conn.prepareStatement(sql4);
            ps.setString(1, idEvento);
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<String> getNomiEventibyId(List<String> ideventi) {
        PreparedStatement ps;
        List<String> nomiEventi = new ArrayList<>();
        try{
            for (String idEvento : ideventi) {
                String sql = " SELECT Nome "
                        + " FROM evento "
                        + " WHERE "
                        + "   IdEvento = ? AND deleted = 'N'";

                ps = conn.prepareStatement(sql);
                ps.setString(1, idEvento);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    nomiEventi.add(rs.getString("Nome"));
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return nomiEventi;
    }

    @Override
    public List<String> getNomiEventi() {
        PreparedStatement ps;
        List<String> nomiEventi = new ArrayList<>();
        try{
            String sql = " SELECT Nome "
                    + " FROM evento  where deleted = 'N'";

            ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nomiEventi.add(rs.getString("Nome"));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return nomiEventi;
    }

    @Override
    public List<Evento> getEventiByNome(String nome) {
        return null;
    }

    @Override
    public String getEventoByNome(String nome) {
        PreparedStatement ps;
        String idEvento = null;

        try{
            String sql = " SELECT IdEvento "
                    + " FROM evento "
                    + " WHERE "
                    + "   Nome = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, nome);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idEvento = rs.getString("IdEvento");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return idEvento;
    }

    @Override
    public List<Evento> getEventiByOrganizzatore(String idOrganizzatore) {
        PreparedStatement ps;
        List<Evento> eventi = new ArrayList<>();

        try{
            String sql = " SELECT * "
                    + " FROM evento "
                    + " WHERE "
                    + "IdOrganizzatore = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idOrganizzatore);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventi.add(read(rs));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return eventi;
    }


}

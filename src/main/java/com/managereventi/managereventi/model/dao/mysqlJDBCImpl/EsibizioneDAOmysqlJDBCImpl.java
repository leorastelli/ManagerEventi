package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.BigliettoDAO;
import com.managereventi.managereventi.model.dao.EsibizioneDAO;
import com.managereventi.managereventi.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EsibizioneDAOmysqlJDBCImpl implements EsibizioneDAO {

    Connection conn;

    public EsibizioneDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    Esibizione read(ResultSet rs) {
        Esibizione esibizione = new Esibizione();
        Organizzatore organizzatore = new Organizzatore();
        Evento evento = new Evento();
        Luogo luogo = new Luogo();

        esibizione.setIdOrganizzatore(organizzatore);
        esibizione.setIdEvento(evento);
        esibizione.setIdLuogo(luogo);

        try {

            esibizione.setIdEsibizione(rs.getString("IdEsibizione"));
            esibizione.setNome(rs.getString("Nome"));
            esibizione.setDescrizione(rs.getString("Descrizione"));
            esibizione.setDurata(rs.getTime("Durata"));
            esibizione.setOraInizio(rs.getTime("OraInizio"));
            esibizione.setGenere(rs.getString("Genere"));
            esibizione.getOrganizzatore().setIdOrganizzatore(rs.getString("IdOrganizzatore"));
            esibizione.getIdEvento().setIdEvento(rs.getString("IdEvento"));
            esibizione.getIdLuogo().setIdLuogo(rs.getString("IdLuogo"));
            esibizione.setPostiDisponibili(rs.getInt("PostiDisponibili"));
            esibizione.setImmagine(rs.getBlob("Immagine"));
            esibizione.setDataEsibizione(rs.getDate("DataEsibizione"));
            esibizione.getIdLuogo().setNome(rs.getString("NomeLuogo"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return esibizione;
    }

    @Override
    public Esibizione createEsibizione(Esibizione esibizione) {
        PreparedStatement ps;

        try{
            String sql
                    = " INSERT INTO esibizione "
                    + "   (IdEsibizione, Nome, Descrizione, Durata, OraInizio, Genere, IdOrganizzatore, IdEvento, IdLuogo, Immagine, DataEsibizione) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, esibizione.getIdEsibizione());
            ps.setString(2, esibizione.getNome());
            ps.setString(3, esibizione.getDescrizione());
            ps.setTime(4, esibizione.getDurata());
            ps.setTime(5, esibizione.getOraInizio());
            ps.setString(6, esibizione.getGenere());
            ps.setString(7, esibizione.getOrganizzatore().getIdOrganizzatore());
            ps.setString(8, esibizione.getIdEvento().getIdEvento());
            ps.setString(9, esibizione.getIdLuogo().getIdLuogo());
            ps.setBlob(10, esibizione.getImmagine());
            ps.setDate(11, esibizione.getDataEsibizione());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return esibizione;
    }

    @Override
    public Esibizione getEsibizioneById(String idEsibizione) {
        PreparedStatement ps;
        Esibizione esibizione = null;

        try{
            String sql
                    = " SELECT esibizione.*, Luogo.Nome as NomeLuogo "
                    + " FROM esibizione JOIN Luogo on esibizione.IdLuogo = Luogo.IdLuogo"
                    + " WHERE "
                    + "   IdEsibizione = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idEsibizione);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                esibizione = read(rs);
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return esibizione;
    }

    @Override
    public List<Esibizione> getAllEsibizioni() {
        return null;
    }

    @Override
    public void updateEsibizione(Esibizione esibizione) {

        PreparedStatement ps;

        try{
            String sql
                    = " UPDATE esibizione "
                    + " SET "
                    + "   Nome = ?,"
                    + "   Descrizione = ?,"
                    + "   Durata = ?,"
                    + "   OraInizio = ?,"
                    + "   Genere = ?,"
                    + "   NumeroArtisti = ?,"
                    + "   IdOrganizzatore = ?,"
                    + "   IdEvento = ?,"
                    + "   IdLuogo = ?,"
                    + "   PostiDisponibili = ?, Immagine = ?, DataEsibizione = ?"
                    + " WHERE "
                    + "   IdEsibizione = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, esibizione.getNome());
            ps.setString(2, esibizione.getDescrizione());
            ps.setTime(3, esibizione.getDurata());
            ps.setTime(4, esibizione.getOraInizio());
            ps.setString(5, esibizione.getGenere());
            ps.setInt(6, '1');
            ps.setString(7, esibizione.getOrganizzatore().getIdOrganizzatore());
            ps.setString(8, esibizione.getIdEvento().getIdEvento());
            ps.setString(9, esibizione.getIdLuogo().getIdLuogo());
            ps.setInt(10, esibizione.getPostiDisponibili());
            ps.setBlob(11, esibizione.getImmagine());
            ps.setDate(12, esibizione.getDataEsibizione());
            ps.setString(13, esibizione.getIdEsibizione());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void deleteEsibizione(String idEsibizione) {

        PreparedStatement ps;

        try{
            String sql=
                    " UPDATE esibizione "
                    + " SET "
                    + "   deleted = 'Y'"
                    + " WHERE "
                    + "   IdEsibizione = ? AND deleted = 'N'";


            ps = conn.prepareStatement(sql);
            ps.setString(1, idEsibizione);

            ps.executeUpdate();

            String sql2
                    = " UPDATE biglietto "
                    + " SET "
                    + "   stato = 0"
                    + " WHERE "
                    + "   IdEsibizione = ? AND stato = 1";

            ps = conn.prepareStatement(sql2);
            ps.setString(1, idEsibizione);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Esibizione> getEsibizioniByOrganizzatore(String idOrganizzatore) {
        PreparedStatement ps;
        List<Esibizione> esibizioni = new ArrayList<>();

        try{
            String sql
                    = " SELECT esibizione.*, Luogo.Nome as NomeLuogo"
                    + " FROM esibizione JOIN Luogo on esibizione.IdLuogo = Luogo.IdLuogo "
                    + " WHERE "
                    + "   IdOrganizzatore = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idOrganizzatore);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                esibizioni.add(read(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return esibizioni;
    }

    @Override
    public List<Esibizione> getEsibizioniByEvento(String idEvento) {
        PreparedStatement ps;
        List<Esibizione> esibizioni = new ArrayList<>();

        try{
            String sql
                    = " SELECT esibizione.*, Luogo.Nome as NomeLuogo "
                    + " FROM esibizione join Luogo on esibizione.IdLuogo = Luogo.IdLuogo "
                    + " WHERE "
                    + "   IdEvento = ? AND deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idEvento);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                esibizioni.add(read(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return esibizioni;
    }
}

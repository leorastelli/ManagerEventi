package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.AbbonamentoDAO;
import com.managereventi.managereventi.model.dao.ArtistaDAO;
import com.managereventi.managereventi.model.dao.EsibizioneDAO;
import com.managereventi.managereventi.model.mo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ArtistaDAOmysqlJDBCImpl implements ArtistaDAO {

    Connection conn;

    public ArtistaDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    Artista read(ResultSet rs) {
        Artista artista = new Artista();
        Artista sostituto = new Artista();
        Evento evento = new Evento();
        Esibizione esibizione = new Esibizione();

        try {

            artista.setCodiceSIAE(rs.getString("CodiceSIAE"));
            artista.setNome(rs.getString("Nome"));
            artista.setCognome(rs.getString("Cognome"));
            artista.setGenere(rs.getString("Genere"));
            artista.getIdEvento().setIdEvento(rs.getString("IdEvento"));
            artista.getIdEsibizione().setIdEsibizione(rs.getString("IdEsibizione"));
            artista.getIdSostituto().setCodiceSIAE(rs.getString("CodiceSIAE"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return artista;
    }

    @Override
    public Artista createArtista(Artista artista) {

        PreparedStatement ps;

        try {

            String sql
                    = " INSERT INTO artista "
                    + "   (CodiceSIAE, Nome, Cognome, Genere, IdEvento, IdEsibizione, CodiceSIAESostituto) "
                    + " VALUES (?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, artista.getCodiceSIAE());
            ps.setString(2, artista.getNome());
            ps.setString(3, artista.getCognome());
            ps.setString(4, artista.getGenere());
            ps.setString(5, artista.getIdEvento().getIdEvento());
            ps.setString(6, artista.getIdEsibizione().getIdEsibizione());
            ps.setString(7, artista.getIdSostituto().getCodiceSIAE());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return artista;
    }

    @Override
    public Artista getArtistaByCodiceSIAE(String codiceSIAE) {
        return null;
    }

    @Override
    public List<Artista> getAllArtisti() {
        return null;
    }

    @Override
    public void updateArtista(Artista artista) {

    }

    @Override
    public void deleteArtista(String codiceSIAE) {

    }
}

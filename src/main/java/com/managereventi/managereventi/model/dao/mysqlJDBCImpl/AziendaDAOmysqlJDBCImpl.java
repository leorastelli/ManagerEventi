package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.ArtistaDAO;
import com.managereventi.managereventi.model.dao.AziendaDAO;
import com.managereventi.managereventi.model.mo.Artista;
import com.managereventi.managereventi.model.mo.Azienda;
import com.managereventi.managereventi.model.mo.Esibizione;
import com.managereventi.managereventi.model.mo.Evento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class AziendaDAOmysqlJDBCImpl implements AziendaDAO {

    Connection conn;

    public AziendaDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    Azienda read(ResultSet rs) {
        Azienda azienda = new Azienda();


        try {
            azienda.setPartitaIVA(rs.getString("PartitaIVA"));
            azienda.setNome(rs.getString("Nome"));
            azienda.setIndirizzo(rs.getString("Indirizzo"));
            azienda.setCitta(rs.getString("Citta"));
            azienda.setProvincia(rs.getString("Provincia"));
            azienda.setCap(rs.getString("Cap"));
            azienda.setStato(rs.getString("Stato"));
            azienda.setTelefono(rs.getString("Telefono"));
            azienda.setEmail(rs.getString("Email"));
            azienda.setPassword(rs.getString("password"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return azienda;
    }

    @Override
    public Azienda createAzienda(Azienda azienda) {
        PreparedStatement ps;

        try {

                String sql
                        = " INSERT INTO azienda "
                        + "   (PartitaIVA, Nome, Indirizzo, Citta, Provincia, Cap, Stato, Telefono, Email, password) "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?)";

                ps = conn.prepareStatement(sql);
                ps.setString(1, azienda.getPartitaIVA());
                ps.setString(2, azienda.getNome());
                ps.setString(3, azienda.getIndirizzo());
                ps.setString(4, azienda.getCitta());
                ps.setString(5, azienda.getProvincia());
                ps.setString(6, azienda.getCap());
                ps.setString(7, azienda.getStato());
                ps.setString(8, azienda.getTelefono());
                ps.setString(9, azienda.getEmail());
                ps.setString(10, azienda.getPassword());

                ps.executeUpdate();
        }
        catch (Exception e) {
                throw new RuntimeException(e);
        }

        return azienda;
    }

    @Override
    public Azienda getAziendaByPartitaIVA(String partitaIVA) {
        PreparedStatement ps;
        ResultSet rs;
        Azienda azienda = null;

        try {
            String sql
                    = " SELECT * "
                    + " FROM azienda "
                    + " WHERE PartitaIVA = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, partitaIVA);

            rs = ps.executeQuery();

            if (rs.next()) {
                azienda = read(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return azienda;
    }

    @Override
    public List<Azienda> getAllAziende() {
        return null;
    }

    @Override
    public void updateAzienda(Azienda azienda) {

        PreparedStatement ps;

        try{
            String sql
                    = " UPDATE azienda "
                    + " SET "
                    + "   Nome = ?, "
                    + "   Indirizzo = ?, "
                    + "   Citta = ?, "
                    + "   Provincia = ?, "
                    + "   Cap = ?, "
                    + "   Stato = ?, "
                    + "   Telefono = ?, "
                    + "   Email = ? "
                    + " WHERE PartitaIVA = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, azienda.getNome());
            ps.setString(2, azienda.getIndirizzo());
            ps.setString(3, azienda.getCitta());
            ps.setString(4, azienda.getProvincia());
            ps.setString(5, azienda.getCap());
            ps.setString(6, azienda.getStato());
            ps.setString(7, azienda.getTelefono());
            ps.setString(8, azienda.getEmail());
            ps.setString(9, azienda.getPartitaIVA());

            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void deleteAzienda(String partitaIVA) {

    }

    @Override
    public Azienda findLoggedUser() {
        return null;
    }
}

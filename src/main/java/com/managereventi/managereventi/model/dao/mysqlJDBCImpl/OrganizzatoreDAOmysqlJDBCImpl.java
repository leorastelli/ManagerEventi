package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.AziendaDAO;
import com.managereventi.managereventi.model.dao.OrganizzatoreDAO;
import com.managereventi.managereventi.model.mo.Azienda;
import com.managereventi.managereventi.model.mo.Organizzatore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class OrganizzatoreDAOmysqlJDBCImpl implements OrganizzatoreDAO {

    Connection conn;

    public OrganizzatoreDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    Organizzatore read(ResultSet rs) {
        Organizzatore organizzatore = new Organizzatore();


        try {
            organizzatore.setIdOrganizzatore(rs.getString("IdOrganizzatore"));
            organizzatore.setNome(rs.getString("Nome"));
            organizzatore.setCognome(rs.getString("Cognome"));
            organizzatore.setEmail(rs.getString("Email"));
            organizzatore.setPassword(rs.getString("Password"));
            organizzatore.setCodiceAutorizzazione(rs.getString("CodiceAutorizzazione"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return organizzatore;
    }


    @Override
    public Organizzatore createOrganizzatore(Organizzatore organizzatore) {
        PreparedStatement ps;

        try{
            String sql
                    = " INSERT INTO organizzatore "
                    + "   (IdOrganizzatore, Nome, Cognome, Email, Password, CodiceAutorizzazione) "
                    + " VALUES (?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, organizzatore.getIdOrganizzatore());
            ps.setString(2, organizzatore.getNome());
            ps.setString(3, organizzatore.getCognome());
            ps.setString(4, organizzatore.getEmail());
            ps.setString(5, organizzatore.getPassword());
            ps.setString(6, organizzatore.getCodiceAutorizzazione());

            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return organizzatore;
    }

    @Override
    public Organizzatore getOrganizzatoreById(String idOrganizzatore) {
        return null;
    }

    @Override
    public List<Organizzatore> getAllOrganizzatori() {
        return null;
    }

    @Override
    public void updateOrganizzatore(Organizzatore organizzatore) {

        PreparedStatement ps;

        try{
            String sql
                    = " UPDATE organizzatore "
                    + " SET "
                    + "   Nome = ?, "
                    + "   Cognome = ?, "
                    + "   Email = ?, "
                    + "   Password = ?, "
                    + "   CodiceAutorizzazione = ? "
                    + " WHERE IdOrganizzatore = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, organizzatore.getNome());
            ps.setString(2, organizzatore.getCognome());
            ps.setString(3, organizzatore.getEmail());
            ps.setString(4, organizzatore.getPassword());
            ps.setString(5, organizzatore.getCodiceAutorizzazione());
            ps.setString(6, organizzatore.getIdOrganizzatore());

            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteOrganizzatore(String idOrganizzatore) {

        PreparedStatement ps;

        try{
            String sql
                    = " DELETE FROM organizzatore "
                    + " WHERE IdOrganizzatore = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idOrganizzatore);

            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Organizzatore finLoggedOrganizzatore() {
        return null;
    }
}

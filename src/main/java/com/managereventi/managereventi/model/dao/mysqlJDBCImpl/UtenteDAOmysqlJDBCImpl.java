package com.managereventi.managereventi.model.dao.mysqlJDBCImpl;

import com.managereventi.managereventi.model.dao.OrganizzatoreDAO;
import com.managereventi.managereventi.model.dao.UtenteDAO;
import com.managereventi.managereventi.model.mo.Organizzatore;
import com.managereventi.managereventi.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UtenteDAOmysqlJDBCImpl implements UtenteDAO {

    Connection conn;

    public UtenteDAOmysqlJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    Utente read(ResultSet rs) {
        Utente utente = new Utente();


        try {
            utente.setIdUtente(rs.getString("IdUtente"));
            utente.setNome(rs.getString("Nome"));
            utente.setCognome(rs.getString("Cognome"));
            utente.setEmail(rs.getString("Email"));
            utente.setPassword(rs.getString("Password"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return utente;
    }
    @Override
    public Utente createUtente(Utente utente) {
        PreparedStatement ps;

        try{
            String sql
                    = " INSERT INTO utente "
                    + "   (IdUtente, Nome, Cognome, Email, Password) "
                    + " VALUES (?,?,?,?,?)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, utente.getIdUtente());
            ps.setString(2, utente.getNome());
            ps.setString(3, utente.getCognome());
            ps.setString(4, utente.getEmail());
            ps.setString(5, utente.getPassword());

            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    @Override
    public Utente getUtenteById(String idUtente) {
        PreparedStatement ps;
        Utente utente = null;

        try{
            String sql
                    = " SELECT * "
                    + " FROM utente "
                    + " WHERE IdUtente = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, idUtente);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
               utente = read(rs);
            }
            rs.close();
            ps.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return utente;
    }

    @Override
    public List<Utente> getAllUtenti() {
        return null;
    }

    @Override
    public void updateUtente(Utente utente) {

        PreparedStatement ps;

        try{
            String sql
                    = " UPDATE utente "
                    + " SET "
                    + "   Nome = ?, "
                    + "   Cognome = ?, "
                    + "   Email = ?, "
                    + "   Password = ? "
                    + " WHERE IdUtente = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getPassword());
            ps.setString(5, utente.getIdUtente());

            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteUtente(Utente utente) {

        PreparedStatement ps;

        try{
            String sql
                    = " DELETE FROM utente "
                    + " WHERE IdUtente = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, utente.getIdUtente());

            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Utente findLoggedUser() {
        return null;
    }
}

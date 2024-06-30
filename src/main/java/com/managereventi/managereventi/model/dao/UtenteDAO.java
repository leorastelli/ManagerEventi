package com.managereventi.managereventi.model.dao;

import java.util.List;

import com.managereventi.managereventi.model.mo.Biglietto;
import com.managereventi.managereventi.model.mo.Utente;

public interface UtenteDAO {
    public Utente createUtente(Utente utente);
    public Utente getUtenteById(String idUtente);
    public List<Utente> getAllUtenti();
    public void updateUtente(Utente utente);
    public void deleteUtente(Utente utente);
    public Utente findLoggedUser();

    public List<Biglietto> getBigliettiUtente(Utente utente);
}




package com.managereventi.managereventi.model.dao;

import com.managereventi.managereventi.model.mo.Domanda;
import com.managereventi.managereventi.model.mo.Risposta;
import com.managereventi.managereventi.model.mo.Utente;

import java.util.List;

public interface DomandaDAO {

    public Domanda createDomanda(Domanda domanda);
    public Domanda getDomandaById(String idDomanda);
    public List<Domanda> getAllDomande();
    public void updateDomanda(Domanda domanda);
    public void deleteDomanda(String idDomanda);

    public List<Domanda> getDomandeUtente(Utente utente);

    public List<Risposta> getRisposteDomanda(Domanda domanda);

    public String getMailUtente(Domanda domanda);

}

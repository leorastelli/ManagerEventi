package com.managereventi.managereventi.model.dao;

import com.managereventi.managereventi.model.mo.Risposta;
import com.managereventi.managereventi.model.mo.Domanda;
import com.managereventi.managereventi.model.mo.Utente;

import java.util.List;

public interface RispostaDAO {

    public Risposta createRisposta(Risposta risposta);
    public Risposta getRispostaById(String idRisposta);
    public List<Risposta> getAllRisposte();
    public void updateRisposta(Risposta risposta);
    public void deleteRisposta(String idRisposta);

    public List<Risposta> getRisposteUtente(Utente utente);

    public List<Risposta> getRisposteDomanda(Domanda domanda);

}

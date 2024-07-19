package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Abbonamento;
import com.managereventi.managereventi.model.mo.Utente;

public interface AbbonamentoDAO {
    public Abbonamento createAbbonamento(Abbonamento abbonamento);
    public Abbonamento getAbbonamentoById(String idAbbonamento);
    public List<Abbonamento> getAllAbbonamenti();
    public void updateAbbonamento(Abbonamento abbonamento);
    public void deleteAbbonamento(String idAbbonamento);

    public List<String> getAbbonatiEvento(String idEvento);

    public List<Abbonamento> getAbbonamentiUtente(Utente utente);

    public String getAbbonamentiVendutiEvento(String idEvento);
}


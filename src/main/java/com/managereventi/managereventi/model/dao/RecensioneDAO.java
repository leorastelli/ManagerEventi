package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Recensione;

public interface RecensioneDAO {
    public Recensione createRecensione(Recensione recensione);
    public Recensione getRecensioneById(String idRecensione);
    public List<Recensione> getRecensioniByUtente(String idUtente);
    public List<Recensione> getRecensioniByEsibizione(String idEsibizione);
    public List<Recensione> getRecensioniByEvento(String idEvento);
    public void updateRecensione(Recensione recensione);
    public void deleteRecensione(String idRecensione);
}



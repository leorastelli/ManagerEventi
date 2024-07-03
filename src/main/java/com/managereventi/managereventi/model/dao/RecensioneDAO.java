package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Recensione;

public interface RecensioneDAO {
    public Recensione createRecensione(Recensione recensione);
    public Recensione getRecensioneById(String idRecensione);
    public List<Recensione> getRecensioniByUtente(String idUtente);
    public List<Recensione> getRecensioniByEsibizione(String idEsibizione);
    public List<Recensione> getRecensioniByEvento(String IdEvento);
    public void updateRecensione(Recensione recensione);
    public void deleteRecensione(String idRecensione);
    public List<Recensione> getRecensioniByEventoStelle(String idEvento, int stelle);

    public List<Recensione> findAll();

    public List<Recensione> getRecensioniByStelle(int stelle);

}




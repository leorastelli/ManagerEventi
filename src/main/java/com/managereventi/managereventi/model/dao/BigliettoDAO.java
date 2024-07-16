package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Biglietto;
import com.managereventi.managereventi.model.mo.Utente;

public interface BigliettoDAO {
    public Biglietto createBiglietto(Biglietto biglietto);
    public Biglietto getBigliettoById(String idBiglietto);
    public List<Biglietto> getAllBiglietti();
    public void updateBiglietto(Biglietto biglietto);
    public void deleteBiglietto(String idBiglietto);

    public List<Biglietto> getBigliettiUtente(Utente utente);

    public List<String> getIdEventiUtente(Utente utente);

    public List<String> getPostiOccupatiEsibizione(String IdEsibizione);
}

package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Biglietto;

public interface BigliettoDAO {
    public Biglietto createBiglietto(Biglietto biglietto);
    public Biglietto getBigliettoById(String idBiglietto);
    public List<Biglietto> getAllBiglietti();
    public void updateBiglietto(Biglietto biglietto);
    public void deleteBiglietto(String idBiglietto);
}

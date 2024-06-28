package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Azienda;

public interface AziendaDAO {

    public Azienda createAzienda(Azienda azienda)  ;
    public Azienda getAziendaByPartitaIVA(String partitaIVA)  ;
    public List<Azienda> getAllAziende() ;
    public void updateAzienda(Azienda azienda) ;
    public  void deleteAzienda(String partitaIVA);
    public Azienda findLoggedUser() ;
}

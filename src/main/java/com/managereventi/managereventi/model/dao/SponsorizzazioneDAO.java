package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Sponsorizzazione;

public interface SponsorizzazioneDAO {
    public Sponsorizzazione createSponsorizzazione(Sponsorizzazione sponsorizzazione);
    public Sponsorizzazione getSponsorizzazione(String partitaIVA, String idEvento);
    public List<Sponsorizzazione> getSponsorizzazioniByPartitaIVA(String partitaIVA);
    public List<Sponsorizzazione> getSponsorizzazioniByEvento(String idEvento);
    public void updateSponsorizzazione(Sponsorizzazione sponsorizzazione);
    public void deleteSponsorizzazione(String partitaIVA, String idEvento);
}




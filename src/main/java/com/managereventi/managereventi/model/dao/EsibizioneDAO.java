package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Esibizione;

public interface EsibizioneDAO {
    public Esibizione createEsibizione(Esibizione esibizione);
    public Esibizione getEsibizioneById(String idEsibizione);
    public List<Esibizione> getAllEsibizioni();
    public void updateEsibizione(Esibizione esibizione);
    public void deleteEsibizione(String idEsibizione);
}


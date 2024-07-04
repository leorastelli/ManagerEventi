package com.managereventi.managereventi.model.dao;

import com.managereventi.managereventi.model.mo.Candidature;
import java.util.List;

public interface CandidatureDAO {

    public Candidature createCandidature(Candidature candidature);
    public Candidature getCandidatureById(String idCandidature);
    public List<Candidature> getCandidatureByPosizione(String Posizione);
    public void updateCandidature(Candidature candidature);
    public void deleteCandidature(String idCandidature);
    public List<Candidature> findAll();

}

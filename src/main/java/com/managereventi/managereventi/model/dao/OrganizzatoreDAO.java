package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Organizzatore;

public interface OrganizzatoreDAO {
    public Organizzatore createOrganizzatore(Organizzatore organizzatore);
    public Organizzatore getOrganizzatoreById(String idOrganizzatore);
    public List<Organizzatore> getAllOrganizzatori();
    public void updateOrganizzatore(Organizzatore organizzatore);
    public void deleteOrganizzatore(String idOrganizzatore);

    public Organizzatore finLoggedOrganizzatore();
}



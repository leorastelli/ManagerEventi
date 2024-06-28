package com.managereventi.managereventi.model.dao;
import java.util.List;
import com.managereventi.managereventi.model.mo.Luogo;

public interface LuogoDAO {
    public Luogo createLuogo(Luogo luogo);
    public Luogo getLuogoById(String idLuogo);
    public List<Luogo> getAllLuoghi();
    public void updateLuogo(Luogo luogo);
    public void deleteLuogo(String idLuogo);
}


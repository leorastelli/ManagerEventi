package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Evento;

public interface EventoDAO {
    public Evento createEvento(Evento evento);
    public Evento getEventoById(String idEvento);
    public List<Evento> getAllEventi();
    public void updateEvento(Evento evento);
    public void deleteEvento(String idEvento);
}

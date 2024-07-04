package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Evento;

public interface EventoDAO {
    public Evento createEvento(Evento evento);
    public Evento getEventoById(String idEvento);
    public List<Evento> getAllEventi();
    public void updateEvento(Evento evento);
    public void deleteEvento(String idEvento);

    public List<String> getNomiEventibyId(List<String> ideventi);

    public List<String> getNomiEventi();

    public List<Evento> getEventiByNome(String nome);

    public String getEventoByNome(String nome);

    public List<Evento> getEventiByOrganizzatore(String idOrganizzatore);
}

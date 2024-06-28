package com.managereventi.managereventi.model.dao;

import com.managereventi.managereventi.model.mo.Artista;

import java.util.List;

public interface ArtistaDAO {

    public Artista createArtista(Artista artista) ;
    public Artista getArtistaByCodiceSIAE(String codiceSIAE) ;
    public List<Artista> getAllArtisti();
    public void updateArtista(Artista artista)  ;
    public void deleteArtista(String codiceSIAE)  ;
}

package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Ritual;

import java.util.List;
import java.util.Map;

public interface RitualService {
    Ritual create(String idFicha, Ritual ritual);
    List<Ritual> get(String idFicha);
    void partialUpdate(String idFicha, String idRitual, Map<String, Object> updates);
    void delete(String idFicha, String idRitual);
}

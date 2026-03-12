package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Ritual;

import java.util.List;

public interface RitualService {
    Ritual create(String idFicha, Ritual ritual);
    List<Ritual> get(String idFicha);
    Ritual update(String idFicha, Ritual ritual);
    Ritual delete(String idFicha, Ritual ritual);
}

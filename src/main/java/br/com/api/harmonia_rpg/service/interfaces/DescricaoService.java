package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Descricao;

import java.util.List;
import java.util.Map;

public interface DescricaoService {
    Descricao create(String idFicha, Descricao descricao);
    List<Descricao> get(String idFicha);
    void partialUpdate(String idFicha, String idDescricao, Map<String, Object> updates);
    void delete(String idFicha, String idDescricao);
}

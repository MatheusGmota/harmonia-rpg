package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.dtos.RitualDTO;

import java.util.List;
import java.util.Map;

public interface RitualService {
    List<RitualDTO.ResponseDTO> obter(String idUsuario, String idFicha);
    RitualDTO.ResponseDTO criar(String idUsuario, String idFicha, RitualDTO.RequestDTO dto);
    Map<String, Object> editar(String idUsuario, String idFicha, String idRitual, Map<String, Object> updates);
    Map<String, Object> deletar(String idUsuario, String idFicha, String idRitual);
}

package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;

import java.util.Map;

public interface DescricaoService {
    DescricaoDTO.DescricaoResponseDTO obter(String idUsuario, String idFicha);
    DescricaoDTO.DescricaoResponseDTO criar(String idUsuario, String idFicha, DescricaoDTO.DescricaoRequestDTO dto);
    Map<String, Object> editar(String idUsuario, String idFicha, String idDescricao, Map<String, Object> updates);
    Map<String, Object> deletar(String idUsuario, String idFicha, String idDescricao);
}

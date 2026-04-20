package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface DescricaoService {
    DescricaoDTO.ResponseDTO obter(String idUsuario, String idFicha);
    List<DescricaoDTO.ResponseDTO> obterAgentesDoUsuario(String idUsuario) throws ExecutionException, InterruptedException;
    DescricaoDTO.ResponseDTO criar(String idUsuario, DescricaoDTO.RequestDTO dto);
    Map<String, Object> editar(String idUsuario, String idFicha, Map<String, Object> updates);
    Map<String, Object> deletar(String idUsuario, String idFicha);
}

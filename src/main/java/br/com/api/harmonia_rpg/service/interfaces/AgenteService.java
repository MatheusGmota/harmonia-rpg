package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface AgenteService {
    AgenteDTO.AgenteResponseDTO obter(String idUsuario, String idFicha);
    List<AgenteDTO.AgenteResponseDTO> obterAgentesDoUsuario(String idUsuario) throws ExecutionException, InterruptedException;
    AgenteDTO.AgenteResponseDTO criar(String idUsuario, AgenteDTO.AgenteRequestDTO dto);
    Map<String, Object> editar(String idUsuario, String idFicha, Map<String, Object> updates);
    Map<String, Object> deletar(String idUsuario, String idFicha);

}

package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface AgenteService {
    AgenteDTO.ResponseDTO obter(String idUsuario, String idFicha);
    List<AgenteDTO.ResponseDTO> obterAgentesDoUsuario(String idUsuario) throws ExecutionException, InterruptedException;
    AgenteDTO.ResponseDTO criar(String idUsuario, AgenteDTO.RequestDTO dto);
    Map<String, Object> editar(String idUsuario, String idFicha, Map<String, Object> updates);
    Map<String, Object> deletar(String idUsuario, String idFicha);

}

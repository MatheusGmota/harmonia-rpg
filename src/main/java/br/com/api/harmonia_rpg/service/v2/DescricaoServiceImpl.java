package br.com.api.harmonia_rpg.service.v2;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;
import br.com.api.harmonia_rpg.domain.entities.Descricao;
import br.com.api.harmonia_rpg.repositories.interfaces.AgenteRepository;
import br.com.api.harmonia_rpg.repositories.v2.DescricaoRepositoryImpl;
import br.com.api.harmonia_rpg.service.interfaces.DescricaoService;
import br.com.api.harmonia_rpg.service.v1.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class DescricaoServiceImpl implements DescricaoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private DescricaoRepositoryImpl repository;

    @Override
    public DescricaoDTO.DescricaoResponseDTO obter(String idUsuario, String idFicha) {
        try {
            Descricao descricao = repository.obterPorId(idFicha, idFicha);
            return new DescricaoDTO.DescricaoResponseDTO(descricao.getId(), descricao.getAparencia(), descricao.getPersonalidade(), descricao.getHistorico(), descricao.getObjetivo());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DescricaoDTO.DescricaoResponseDTO> obterAgentesDoUsuario(String idUsuario) throws ExecutionException, InterruptedException {
        return List.of();
    }

    @Override
    public DescricaoDTO.DescricaoResponseDTO criar(String idUsuario, DescricaoDTO.DescricaoRequestDTO dto) {
        return null;
    }

    @Override
    public Map<String, Object> editar(String idUsuario, String idFicha, Map<String, Object> updates) {
        return Map.of();
    }

    @Override
    public Map<String, Object> deletar(String idUsuario, String idFicha) {
        return Map.of();
    }
}

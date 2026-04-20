package br.com.api.harmonia_rpg.service.v2;


import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.domain.dtos.UsuarioResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Agente;
import br.com.api.harmonia_rpg.domain.enums.TipoUsuario;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.AgenteMapper;
import br.com.api.harmonia_rpg.domain.mapper.DescricaoMapper;
import br.com.api.harmonia_rpg.repositories.interfaces.AgenteRepository;
import br.com.api.harmonia_rpg.repositories.v2.DescricaoRepositoryImpl;
import br.com.api.harmonia_rpg.service.interfaces.AgenteService;
import br.com.api.harmonia_rpg.service.v1.UsuarioService;
import com.google.cloud.firestore.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static br.com.api.harmonia_rpg.tools.UpdateTool.filterValidFields;
import static br.com.api.harmonia_rpg.tools.UpdateTool.flattenMap;

@Slf4j
@Service
public class AgenteServiceImpl implements AgenteService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AgenteRepository repository;

    @Autowired
    private DescricaoRepositoryImpl descricaoRepository;

    public AgenteDTO.AgenteResponseDTO obter(String idUsuario, String idFicha) {
        try {
            UsuarioResponseDTO usuario = usuarioService.obterUsuarioPorId(idUsuario); // verifica se usuario existe

            Agente agente = repository.obter(idFicha);
            if (agente == null) {
                log.warn("Nenhum ficha encontrada");
                throw new NotFoundException("Nenhuma ficha encontrada");
            }

            if (!idUsuario.equals(agente.getIdUsuario())) {
                if (!usuario.tipoUsuario().equals(TipoUsuario.MESTRE)) throw new BusinessException("Acesso negado");
            }

            return AgenteMapper.toAgenteDto(agente);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<AgenteDTO.AgenteResponseDTO> obterAgentesDoUsuario(String idUsuario) throws ExecutionException, InterruptedException {
        return repository.obterPorIdUsuario(idUsuario)
                .stream().map(AgenteMapper::toAgenteDto)
                .toList();
    }

    public AgenteDTO.AgenteResponseDTO criar(String idUsuario, AgenteDTO.AgenteRequestDTO dto) {
        try {
            usuarioService.obterUsuarioPorId(idUsuario);

            Agente ficha = AgenteMapper.toAgente(dto, idUsuario);
            ficha.inicializarClasse();

            if (ficha.getTrilha().getClasse() != ficha.getClasse()) {
                throw new IllegalArgumentException("A trilha selecionada não pertence à classe do personagem.");
            }

            Agente agente = repository.adicionar(ficha);

            descricaoRepository.adicionar(agente.getId(), DescricaoMapper.toDescricao(dto.descricao()));

            return AgenteMapper.toAgenteDto(agente);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> editar(String idUsuario, String idFicha, Map<String, Object> updates) {
        try {
            Agente ficha = repository.obter(idFicha);
            UsuarioResponseDTO usuario = usuarioService.obterUsuarioPorId(idUsuario);

            if (ficha == null) {
                throw new BusinessException("Ficha não encontrada");
            }

            if (!ficha.getIdUsuario().equals(idUsuario)) {
                // se usuario for o responsável pela ficha pode editar
                log.warn("A ficha não pertence ao usuário");
                if (!usuario.tipoUsuario().equals(TipoUsuario.MESTRE)) {
                    // se não for só pode editar caso seja do TipoUsuario 'MESTRE'
                    throw new BusinessException("Este usuário não pode editar essa ficha");
                }
            }

            Map<String, Object> flattenedUpdates = flattenMap(updates, "");

            // Remove campos inválidos ou inexistentes
            Map<String, Object> camposFiltrados = filterValidFields(flattenedUpdates, Agente.class);
            camposFiltrados.remove("id");
            camposFiltrados.remove("idUsuario");

            WriteResult atualizar = repository.atualizar(idFicha, camposFiltrados);

            log.info("Ficha id={} atualizado em: {}", idFicha, atualizar.getUpdateTime());
            return Map.of("id", idFicha, "message", "Atualizado com sucesso", "atualizadoEm", atualizar.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao atualizar ficha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public Map<String, Object> deletar(String idUsuario, String idFicha) {
        try {
            usuarioService.obterUsuarioPorId(idUsuario); // validar se usuario é existente

            Agente ficha = repository.obter(idFicha);

            if (ficha == null) {
                throw new BusinessException("Ficha não encontrada");
            }

            if (!ficha.getIdUsuario().equals(idUsuario)) {
                // se usuario for o responsável pela ficha pode deletar
                log.warn("A ficha não pertence ao usuário");
                throw new BusinessException("Este usuário não pode editar essa ficha");
            }

            WriteResult deletar = repository.deletar(idFicha);

            log.info("Ficha id={} deletado em: {}", idFicha, deletar.getUpdateTime());

            return Map.of("message", "Deletado com sucesso", "atualizadoEm", deletar.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


}

package br.com.api.harmonia_rpg.service.v2;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;
import br.com.api.harmonia_rpg.domain.entities.Descricao;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.DescricaoMapper;
import br.com.api.harmonia_rpg.repositories.interfaces.AgenteRepository;
import br.com.api.harmonia_rpg.repositories.v2.DescricaoRepositoryImpl;
import br.com.api.harmonia_rpg.service.interfaces.DescricaoService;
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
public class DescricaoServiceImpl extends GenericService implements DescricaoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private DescricaoRepositoryImpl repository;

    @Override
    public DescricaoDTO.DescricaoResponseDTO obter(String idUsuario, String idFicha) {
        try {
            List<Descricao> documentos = repository.obterTodos(idFicha);

            if (documentos.isEmpty())
                throw new NotFoundException("Descrição não encontrada"); // verifica se descrição existe

            return DescricaoMapper.toDescricaoDto(documentos.get(0));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DescricaoDTO.DescricaoResponseDTO criar(String idUsuario, String id, DescricaoDTO.DescricaoRequestDTO dto) {
        return null;
    }

    @Override
    public Map<String, Object> editar(String idUsuario, String idFicha, String idDescricao, Map<String, Object> updates) {
        try {
            verificaAcessoFicha(idUsuario, idFicha);

            if (repository.obterTodos(idFicha).isEmpty())
                throw new NotFoundException("Descrição não encontrada"); // verifica se descrição existe

            Map<String, Object> flattenedUpdates = flattenMap(updates, "");

            // Remove campos inválidos ou inexistentes
            Map<String, Object> safeUpdates = filterValidFields(flattenedUpdates, Descricao.class);

            WriteResult result = repository.atualizar(idFicha, idDescricao, safeUpdates);

            log.info("Descrição id={} editado em: {}", idFicha, result.getUpdateTime());
            return Map.of(
                    "id", idDescricao,
                    "message", "Descrição atualizado com sucesso",
                    "atualizadoEm", result.getUpdateTime()
            );
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao editar descrição id={}: {}", idDescricao, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> deletar(String idUsuario, String idFicha, String idDescricao) {
        try {
            verificaAcessoFicha(idUsuario, idFicha);

            if (!repository.existeDocumento(idFicha, idDescricao))
                throw new NotFoundException("Descrição não encontrado"); // verifica se descrição existe

            WriteResult result = repository.deletar(idFicha, idDescricao);

            log.info("Descrição id={} deletado pelo id={} em: {}", idDescricao, idUsuario, result.getUpdateTime());
            return Map.of(
                    "message", "Descrição deletado com sucesso",
                    "atualizadoEm", result.getUpdateTime()
            );
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao deletar descrição id={}: {}", idDescricao, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

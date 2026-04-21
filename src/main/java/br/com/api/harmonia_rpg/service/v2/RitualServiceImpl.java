package br.com.api.harmonia_rpg.service.v2;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.domain.dtos.RitualDTO;
import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.RitualMapper;
import br.com.api.harmonia_rpg.repositories.v2.RitualRepositoryImpl;
import br.com.api.harmonia_rpg.service.interfaces.AgenteService;
import br.com.api.harmonia_rpg.service.interfaces.RitualService;
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
public class RitualServiceImpl extends GenericService implements RitualService {

    @Autowired
    private AgenteService agenteService;

    @Autowired
    private RitualRepositoryImpl repository;

    @Override
    public RitualDTO.RitualResponseDTO criar(String idUsuario, String idFicha, RitualDTO.RitualRequestDTO dto) {
        try {
            verificaAcessoFicha(idUsuario, idFicha);

            // caso ficha exista, verifica se já existe um ritual pelo nome
            if (repository.existeDocPorNome(idFicha, "nomeRitual", dto.nomeRitual())) {
                throw new BusinessException("Ritual com esse nome já existe");
            }

            Ritual adicionar = repository.adicionar(idFicha, RitualMapper.toRitual(dto));
            return RitualMapper.toRitualDto(adicionar);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RitualDTO.RitualResponseDTO> obter(String idUsuario, String idFicha) {
        try {
            verificaAcessoFicha(idUsuario, idFicha);

            List<Ritual> rituais = repository.obterTodos(idFicha);
            return rituais.stream().map(RitualMapper::toRitualDto).toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> editar(String idUsuario, String idFicha, String idRitual, Map<String, Object> updates) {
        try {
            verificaAcessoFicha(idUsuario, idFicha);

            if (!repository.existeDocumento(idFicha, idRitual))
                throw new NotFoundException("Ritual não encontrado"); // verifica se ritual existe

            Map<String, Object> flattenedUpdates = flattenMap(updates, "");

            // Remove campos inválidos ou inexistentes
            Map<String, Object> safeUpdates = filterValidFields(flattenedUpdates, Ritual.class);
            safeUpdates.remove("idRitual");

            WriteResult result = repository.atualizar(idFicha, idRitual, safeUpdates);

            log.info("Ritual id={} editado em: {}", idFicha, result.getUpdateTime());
            return Map.of(
                    "id", idRitual,
                    "message", "Ritual atualizado com sucesso",
                    "atualizadoEm", result.getUpdateTime()
            );
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao editar ritual id={}: {}", idRitual, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> deletar(String idUsuario, String idFicha, String idRitual) {
        try {
            verificaAcessoFicha(idUsuario, idFicha);

            if (!repository.existeDocumento(idFicha, idRitual))
                throw new NotFoundException("Ritual não encontrado"); // verifica se ritual existe

            WriteResult result = repository.deletar(idFicha, idRitual);

            log.info("Ritual id={} deletado pelo id={} em: {}", idRitual, idUsuario, result.getUpdateTime());
            return Map.of(
                    "message", "Ritual deletado com sucesso",
                    "atualizadoEm", result.getUpdateTime()
            );
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao deletar ritual id={}: {}", idRitual, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

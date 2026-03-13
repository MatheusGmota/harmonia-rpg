package br.com.api.harmonia_rpg.service.v2;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.repositories.interfaces.RitualRepository;
import br.com.api.harmonia_rpg.service.interfaces.RitualService;
import br.com.api.harmonia_rpg.service.v1.FichaService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static br.com.api.harmonia_rpg.tools.UpdateTool.filterValidFields;
import static br.com.api.harmonia_rpg.tools.UpdateTool.flattenMap;

@Service
public class RitualServiceImpl implements RitualService {

    @Autowired
    private FichaService fichaService;

    @Autowired
    private RitualRepository repository;

    public Ritual create(String idFicha, Ritual ritual) {
        try {

            fichaService.obterFicha(idFicha); // verifica se ficha existe

            // caso ficha exista, verifica se já existe um ritual pelo nome
            if (repository.existeRitualPorNome(idFicha, ritual.getNomeRitual())) {
                throw new BusinessException("Ritual com esse nome já existe");
            }

            ApiFuture<DocumentSnapshot> future = repository.adicionarRitual(idFicha, ritual).get();

            return future.get().toObject(Ritual.class);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ritual> get(String idFicha) {
        try {
            List<QueryDocumentSnapshot> queryDocumentSnapshots = repository.obterRituais(idFicha);
            return queryDocumentSnapshots.stream()
                    .map(d -> d.toObject(Ritual.class)
                    ).toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void partialUpdate(String idFicha, String idRitual, Map<String, Object> updates) {
        try {
            fichaService.obterFicha(idFicha); // verifica se ficha existe

            if (!repository.existeRitual(idFicha, idRitual))
                throw new NotFoundException("Ritual não encontrado"); // verifica se ritual existe

            Map<String, Object> flattenedUpdates = flattenMap(updates, "");

            // Remove campos inválidos ou inexistentes
            Map<String, Object> safeUpdates = filterValidFields(flattenedUpdates, Ritual.class);
            safeUpdates.remove("idRitual");

            if (safeUpdates.isEmpty()) {
                throw new BusinessException("Nenhum campo válido para atualização");
            }
            repository.atualizarParcialRitual(idFicha, idRitual, safeUpdates);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String idFicha, String idRitual) {
        try {
            fichaService.obterFicha(idFicha); // verifica se ficha existe

            if (!repository.existeRitual(idFicha, idRitual))
                throw new NotFoundException("Ritual não encontrado"); // verifica se ritual existe

            repository.deletarRitual(idFicha, idRitual);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

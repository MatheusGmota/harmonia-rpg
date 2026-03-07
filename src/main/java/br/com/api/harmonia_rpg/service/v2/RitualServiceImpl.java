package br.com.api.harmonia_rpg.service.v2;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.repositories.interfaces.RitualRepository;
import br.com.api.harmonia_rpg.service.interfaces.RitualService;
import br.com.api.harmonia_rpg.service.v1.FichaService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
            if (repository.existeRitual(idFicha, ritual.getNomeRitual())) {
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

    @Override
    public Ritual update(String idFicha, Ritual ritual) {
        return null;
    }

    @Override
    public Ritual delete(String idFicha, Ritual ritual) {
        return null;
    }


}

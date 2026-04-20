package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Convite;
import br.com.api.harmonia_rpg.repositories.interfaces.ConviteRepository;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class ConviteRepositoryImpl implements ConviteRepository {

    private static final String COLLECTION = "convites";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION);
    }

    @Override
    public Convite adicionar(Convite convite) throws ExecutionException, InterruptedException {
        DocumentReference ref = getCollection().document();
        convite.setId(ref.getId());
        ref.set(convite).get();
        log.info("Convite id={} persistido, token={}", convite.getId(), convite.getToken());
        return convite;
    }

    @Override
    public Convite obter(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = getCollection().document(id).get().get();
        if (!doc.exists()) {
            log.warn("Convite id={} não encontrado", id);
            return null;
        }
        return doc.toObject(Convite.class);
    }

    @Override
    public Convite obterPorToken(String token) throws ExecutionException, InterruptedException {
        List<Convite> resultado = getCollection()
                .whereEqualTo("token", token)
                .limit(1)
                .get().get()
                .toObjects(Convite.class);

        if (resultado.isEmpty()) {
            log.warn("Nenhum convite encontrado para token={}", token);
            return null;
        }
        return resultado.get(0);
    }

    @Override
    public List<Convite> obterPorCampanha(String idCampanha) throws ExecutionException, InterruptedException {
        return getCollection()
                .whereEqualTo("idCampanha", idCampanha)
                .get().get()
                .toObjects(Convite.class);
    }

    @Override
    public WriteResult atualizar(String id, Map<String, Object> campos) throws ExecutionException, InterruptedException {
        return getCollection().document(id).update(campos).get();
    }
}
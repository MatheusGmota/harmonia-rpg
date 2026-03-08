package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.repositories.interfaces.RitualRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class RitualRepositoryImpl implements RitualRepository {

    private static final String COLLECTION_NAME = "fichas";
    private static final String SUB_COLLECTION = "rituais";

    @Autowired
    private Firestore db;

    private CollectionReference getRituaisCollection(String idFicha) {
        return db.collection(COLLECTION_NAME)
                .document(idFicha)
                .collection(SUB_COLLECTION);
    }

    public boolean existeRitual(String idFicha, String nomeRitual)
            throws ExecutionException, InterruptedException {

        // Query para retornar todos os rituais com o nomeRitual
        ApiFuture<QuerySnapshot> future = getRituaisCollection(idFicha)
                .whereEqualTo("nomeRitual", nomeRitual)
                .limit(1).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments(); // executa e retornar query

        return !documents.isEmpty(); // Se estiver vazio(não tem documento com o nomeRitual) retorna false, caso contrário retorna true
    }

    public DocumentReference adicionarRitual(String idFicha, Ritual ritual) throws ExecutionException, InterruptedException {
        CollectionReference rituaisCollection = getRituaisCollection(idFicha);

        return rituaisCollection
                .add(ritual)
                .get();
    }

    public List<QueryDocumentSnapshot> obterRituais(String idFicha) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getRituaisCollection(idFicha).get();
        return future.get().getDocuments();
    }

    public WriteResult atualizarParcialRitual(
            String idFicha,
            String idRitual,
            Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference ritualDocument = getRituaisCollection(idFicha).document(idRitual);

        return ritualDocument
                .update(updates)
                .get();
    }
}

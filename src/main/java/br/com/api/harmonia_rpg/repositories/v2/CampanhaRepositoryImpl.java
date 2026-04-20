package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Campanha;
import br.com.api.harmonia_rpg.repositories.interfaces.CampanhaRepository;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class CampanhaRepositoryImpl implements CampanhaRepository {

    private static final String COLLECTION_NAME = "campanhas";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    @Override
    public Campanha adicionar(Campanha campanha) throws ExecutionException, InterruptedException {
        CollectionReference collection = getCollection();
        DocumentReference documentReference = collection.add(campanha).get();
        return documentReference.get().get().toObject(Campanha.class);
    }

    @Override
    public Campanha obter(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = getCollection().document(id).get().get();
        return doc.exists() ? doc.toObject(Campanha.class) : null;
    }

    @Override
    public List<Campanha> obterPorMestre(String idMestre) throws ExecutionException, InterruptedException {
        return getCollection()
                .whereEqualTo("idMestre", idMestre)
                .get().get().toObjects(Campanha.class);
    }

    @Override
    public List<Campanha> obterPorJogador(String idJogador) throws ExecutionException, InterruptedException {
        return getCollection()
                .whereArrayContains("idJogadores", idJogador)
                .get().get().toObjects(Campanha.class);
    }

    @Override
    public WriteResult atualizar(String id, Map<String, Object> campos) throws ExecutionException, InterruptedException {
        return getCollection().document(id).update(campos).get();
    }

    @Override
    public WriteResult deletar(String id) throws ExecutionException, InterruptedException {
        return getCollection().document(id).delete().get();
    }
}

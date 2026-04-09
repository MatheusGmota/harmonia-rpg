package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Agente;
import br.com.api.harmonia_rpg.repositories.interfaces.AgenteRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class AgenteRepositoryImpl implements AgenteRepository {

    private static final String COLLECTION_NAME = "fichas";

    @Autowired
    public Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public Agente obter(String idFicha) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshot = getCollection()
                .document(idFicha)
                .get().get();

        return documentSnapshot.toObject(Agente.class);
    }

    public List<Agente> obterPorIdUsuario(String idUsuario) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollection()
                .whereEqualTo("idUsuario", idUsuario)
                .get();

        return future.get()
                .getDocuments()
                .stream()
                .map(f -> f.toObject(Agente.class)).toList();
    }

    public Agente adicionar(Agente ficha) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection().add(ficha).get();
        DocumentSnapshot documentSnapshot = documentReference.get().get();

        return documentSnapshot.toObject(Agente.class);
    }

    public WriteResult deletar(String idFicha) throws ExecutionException, InterruptedException {
        DocumentReference document = getCollection().document(idFicha);
        WriteResult writeResult = document.delete().get();
        return writeResult;
    }

    public WriteResult atualizar(String idFicha, Map<String, Object> campos) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getCollection().document(idFicha);

        return docRef.update(campos).get();
    }
}

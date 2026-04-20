package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Agente;
import br.com.api.harmonia_rpg.repositories.interfaces.AgenteRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    @Override
    public List<Agente> obterPorIds(List<String> ids) throws ExecutionException, InterruptedException {
        if (ids.isEmpty()) return List.of();

        // Firestore limita whereIn a 30 itens por query — divide em lotes
        List<Agente> resultado = new ArrayList<>();

        List<List<String>> lotes = partition(ids, 30);

        for (List<String> lote : lotes) {
            List<Agente> agentesDoLote = getCollection()
                    .whereIn(FieldPath.documentId(), lote)
                    .get().get()
                    .toObjects(Agente.class);
            resultado.addAll(agentesDoLote);
        }

        return resultado;
    }

    private <T> List<List<T>> partition(List<T> lista, int tamanho) {
        List<List<T>> lotes = new ArrayList<>();
        for (int i = 0; i < lista.size(); i += tamanho) {
            lotes.add(lista.subList(i, Math.min(i + tamanho, lista.size())));
        }
        return lotes;
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

package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.wrapper.RituaisWrapper;
import br.com.api.harmonia_rpg.repositories.interfaces.GenericRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public abstract class FirestoreGenericRepository<T> implements GenericRepository<T> {

    private final String COLLECTION_NAME = "fichas";
    private final Firestore firestore;
    private final Class<T> clazz;

    protected abstract String getSubCollectionName();

    private CollectionReference getCollection(String idFicha) {
        return firestore.collection(COLLECTION_NAME).document(idFicha).collection(getSubCollectionName());
    }

    @Override
    public boolean existeDocumento(String idFicha, String id) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = getCollection(idFicha).document(id).get();
        return future.get().exists();
    }

    @Override
    public T adicionar(String idFicha, T entity) throws ExecutionException, InterruptedException {
        getCollection(idFicha).add(entity).get();
        return entity;
    }

    @Override
    public T obterPorId(String idFicha, String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = getCollection(idFicha).document(id).get().get();

        if (!doc.exists()) {
            throw new RuntimeException("Documento não encontrado");
        }

        return doc.toObject(clazz);
    }

    @Override
    public WriteResult deletar(String idFicha, String id) throws ExecutionException, InterruptedException {
        return getCollection(idFicha).document(id).delete().get();
    }

    @Override
    public WriteResult atualizar(String idFicha, String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
         return getCollection(idFicha).document(id).update(updates).get();
    }

    @Override
    public boolean existeDocPorNome(String idFicha, String campo, String filtro) throws ExecutionException, InterruptedException {

        // Query para retornar todos os documentos com o campo e o filtro desejado
        ApiFuture<QuerySnapshot> future = getCollection(idFicha)
                .whereEqualTo(campo, filtro)
                .limit(1).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments(); // executa e retornar query

        return !documents.isEmpty(); // Se estiver vazio retorna false, caso contrário retorna true
    }

    public List<T> obterTodos(String idFicha) throws ExecutionException, InterruptedException{
        QuerySnapshot querySnapshot = getCollection(idFicha).get().get();
        return querySnapshot.getDocuments()
                .stream().map(d -> d.toObject(clazz))
                .toList();
    };
}

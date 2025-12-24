package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.entities.Descricao;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class DescricaoRepository {
    private static final String COLLECTION_NAME = "descricao";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public DocumentSnapshot obterDescricao(String idFicha) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> response = getCollection().document(idFicha).get();

        return response.get();
    }

    public DocumentSnapshot salvar(String idFicha, Descricao descricao)
            throws ExecutionException, InterruptedException {

        ApiFuture<WriteResult> future =
                getCollection().document(idFicha).set(descricao);

        future.get(); // garante persistência
        return getCollection().document(idFicha).get().get();
    }

    public boolean existePorIdFicha(String idFicha)
            throws ExecutionException, InterruptedException {

        DocumentSnapshot snapshot =
                getCollection().document(idFicha).get().get();

        return snapshot.exists();
    }
}

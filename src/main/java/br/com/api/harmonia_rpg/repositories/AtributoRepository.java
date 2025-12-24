package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.entities.Atributos;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;


@Repository
public class AtributoRepository {
    private static final String COLLECTION_NAME = "atributos";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public DocumentSnapshot obterPericias(String idFicha) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> response = getCollection().document(idFicha).get();

        return response.get();
    }

    public DocumentSnapshot salvar(String idFicha, Atributos atributos)
            throws ExecutionException, InterruptedException {

        ApiFuture<WriteResult> future =
                getCollection().document(idFicha).set(atributos);

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

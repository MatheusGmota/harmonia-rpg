package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.entities.Inventario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class InventarioRepository {
    private static final String COLLECTION_NAME = "inventario";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public DocumentSnapshot obterInvetario(String idFicha) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> document =
                getCollection().document(idFicha).get();

        return document.get();
    }

    public DocumentSnapshot create(String idFicha, Inventario inventario) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future =
                getCollection().document(idFicha).set(inventario);

        future.get(); // garante persistência
        return getCollection().document(idFicha).get().get();

    }
}

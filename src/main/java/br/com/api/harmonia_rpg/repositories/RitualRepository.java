package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.wrapper.RituaisWrapper;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class RitualRepository {

    private static final String COLLECTION_NAME = "rituais";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public DocumentSnapshot obterDocumento(String idFicha)
            throws ExecutionException, InterruptedException {

        return getCollection().document(idFicha).get().get();
    }

    public boolean existePorIdFicha(String idFicha)
            throws ExecutionException, InterruptedException {

        return getCollection().document(idFicha).get().get().exists();
    }

    public List<Ritual> obterRituais(String idFicha)
            throws ExecutionException, InterruptedException {

        DocumentSnapshot document =
                getCollection().document(idFicha).get().get();

        if (!document.exists()) {
            return new ArrayList<>();
        }

        List<Ritual> rituais = document.toObject(RituaisWrapper.class) != null
                ? document.toObject(RituaisWrapper.class).getRituais()
                : null;

        return rituais != null ? new ArrayList<>(rituais) : new ArrayList<>();
    }

    public List<Ritual> salvar(String idFicha, List<Ritual> rituais)
            throws ExecutionException, InterruptedException {

        if (rituais == null) {
            rituais = new ArrayList<>();
        }

        Map<String, Object> data = new HashMap<>();
        data.put(COLLECTION_NAME, rituais);

        getCollection()
                .document(idFicha)
                .set(data)
                .get();

        return rituais;
    }
}


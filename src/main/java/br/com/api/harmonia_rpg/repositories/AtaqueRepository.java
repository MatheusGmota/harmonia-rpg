package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.entities.Ataque;
import br.com.api.harmonia_rpg.domain.wrapper.AtaquesWrapper;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class AtaqueRepository {

    private static final String COLLECTION_NAME = "ataques";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public List<Ataque> obterAtaques(String idFicha) throws ExecutionException, InterruptedException {

        DocumentSnapshot document =
                getCollection().document(idFicha).get().get();

        if (!document.exists()) {
            return new ArrayList<>();
        }

        List<Ataque> habilidades = document.toObject(AtaquesWrapper.class) != null
                ? document.toObject(AtaquesWrapper.class).getAtaques()
                : null;

        return habilidades != null ? new ArrayList<>(habilidades) : new ArrayList<>();
    }

    public List<Ataque> salvar(String idFicha, List<Ataque> ataque) throws ExecutionException, InterruptedException {

        if (ataque == null) {
            ataque = new ArrayList<>();
        }

        Map<String, Object> data = new HashMap<>();
        data.put(COLLECTION_NAME, ataque);

        getCollection()
                .document(idFicha)
                .set(data)
                .get();

        return ataque;
    }
}

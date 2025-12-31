package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.entities.Habilidade;
import br.com.api.harmonia_rpg.domain.wrapper.HabilidadeWrapper;
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
public class HabilidadeRepository {

    private static final String COLLECTION_NAME = "habilidades";

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

    public List<Habilidade> obterHabilidades(String idFicha)
            throws ExecutionException, InterruptedException {

        DocumentSnapshot document =
                getCollection().document(idFicha).get().get();

        if (!document.exists()) {
            return new ArrayList<>();
        }

        List<Habilidade> habilidades = document.toObject(HabilidadeWrapper.class) != null
                ? document.toObject(HabilidadeWrapper.class).getHabilidades()
                : null;

        return habilidades != null ? new ArrayList<>(habilidades) : new ArrayList<>();
    }

    public List<Habilidade> salvar(String idFicha, List<Habilidade> habilidades)
            throws ExecutionException, InterruptedException {

        if (habilidades == null) {
            habilidades = new ArrayList<>();
        }

        Map<String, Object> data = new HashMap<>();
        data.put(COLLECTION_NAME, habilidades);

        getCollection()
                .document(idFicha)
                .set(data)
                .get();

        return habilidades;
    }
}

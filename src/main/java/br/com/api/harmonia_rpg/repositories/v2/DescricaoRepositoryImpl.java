package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Descricao;
import br.com.api.harmonia_rpg.repositories.interfaces.DescricaoRepository;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class DescricaoRepositoryImpl implements DescricaoRepository {
    private static final String COLLECTION_NAME = "fichas";
    private static final String SUB_COLLECTION = "descricao";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection(String idFicha) {
        return db.collection(COLLECTION_NAME)
                .document(idFicha)
                .collection(SUB_COLLECTION);
    }

    public DocumentReference adicionar(String idFicha, Descricao descricao) throws ExecutionException, InterruptedException {
        CollectionReference collection = getCollection(idFicha);

        return collection
                .add(descricao)
                .get();
    }
}

package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.dtos.FichaUsuarioDTO;
import br.com.api.harmonia_rpg.domain.entities.Ficha;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class FichaRepository {

    private static final String COLLECTION_NAME = "fichas";

    @Autowired
    public Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public List<FichaUsuarioDTO> obterFichasDoUsuario(String idUsuario) throws ExecutionException, InterruptedException {

        ApiFuture<QuerySnapshot> fichas = getCollection()
                .whereEqualTo("idUsuario", idUsuario)
                .get();

        return fichas
                .get()
                .getDocuments()
                .stream().map(f -> {
                    Ficha o = f.toObject(Ficha.class);

                    return FichaUsuarioDTO.from(o);
                }).toList();
    }

    public DocumentSnapshot obterFichaPorId(String id) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> fichas = getCollection().document(id).get();

        return fichas.get();
    }

    public ApiFuture<DocumentSnapshot> adicionar(Ficha ficha) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection().add(ficha).get();
        return documentReference.get();
    }

    public WriteResult atualizar(String id, Ficha ficha) throws ExecutionException, InterruptedException {
        return getCollection()
                .document(id)
                .set(ficha, SetOptions.merge())
                .get();
    }

    public WriteResult atualizarParcial(String idFicha, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getCollection().document(idFicha);

        return docRef.update(updates).get();
    }

    public WriteResult deletar(String id) throws ExecutionException, InterruptedException {
        return getCollection()
                .document(id)
                .delete().get();
    }

    public List<FichaUsuarioDTO> obterFichasPorCampanha(String nomeCampanha) throws ExecutionException, InterruptedException  {
        ApiFuture<QuerySnapshot> fichas = getCollection()
                .whereEqualTo("nomeCampanha", nomeCampanha)
                .get();

        return fichas
                .get()
                .getDocuments()
                .stream().map(f -> {
                    Ficha o = f.toObject(Ficha.class);

                    return FichaUsuarioDTO.from(o);
                }).toList();
    }
}

package br.com.api.harmonia_rpg.repositories.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface RitualRepository {
    boolean existeRitual(String idFicha, String nomeRitual)
            throws ExecutionException, InterruptedException;

    DocumentReference adicionarRitual(String idFicha, Ritual ritual)
            throws ExecutionException, InterruptedException;

    List<QueryDocumentSnapshot> obterRituais(String idFicha)
            throws ExecutionException, InterruptedException;

    WriteResult atualizarParcialRitual(String idFicha, String idRitual, Map<String, Object> updates)
            throws ExecutionException, InterruptedException;
}

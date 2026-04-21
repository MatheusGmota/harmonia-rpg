package br.com.api.harmonia_rpg.repositories.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Descricao;
import com.google.cloud.firestore.DocumentReference;

import java.util.concurrent.ExecutionException;

public interface DescricaoRepository {
    DocumentReference adicionar(String idFicha, Descricao descricao)
            throws ExecutionException, InterruptedException;

}

package br.com.api.harmonia_rpg.repositories.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Campanha;
import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface CampanhaRepository {
    Campanha adicionar(Campanha campanha) throws ExecutionException, InterruptedException;
    Campanha obter(String id) throws ExecutionException, InterruptedException;
    List<Campanha> obterPorMestre(String idMestre) throws ExecutionException, InterruptedException;
    List<Campanha> obterPorJogador(String idJogador) throws ExecutionException, InterruptedException;
    WriteResult atualizar(String id, Map<String, Object> campos) throws ExecutionException, InterruptedException;
    WriteResult deletar(String id) throws ExecutionException, InterruptedException;
}

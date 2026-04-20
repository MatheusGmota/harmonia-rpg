package br.com.api.harmonia_rpg.repositories.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Convite;
import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ConviteRepository {

    Convite adicionar(Convite convite) throws ExecutionException, InterruptedException;

    Convite obter(String id) throws ExecutionException, InterruptedException;

    /**
     * Busca um convite pelo token UUID (usado na validação do link).
     * Retorna null se não encontrado.
     */
    Convite obterPorToken(String token) throws ExecutionException, InterruptedException;

    /** Todos os convites de uma campanha (útil para reutilizar ou revogar) */
    List<Convite> obterPorCampanha(String idCampanha) throws ExecutionException, InterruptedException;

    WriteResult atualizar(String id, Map<String, Object> campos) throws ExecutionException, InterruptedException;
}
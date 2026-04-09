package br.com.api.harmonia_rpg.repositories.interfaces;

import br.com.api.harmonia_rpg.domain.entities.Agente;
import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface AgenteRepository {
    Agente obter(String idFicha) throws ExecutionException, InterruptedException;
    List<Agente> obterPorIdUsuario(String idUsuario) throws ExecutionException, InterruptedException;
    Agente adicionar(Agente ficha) throws ExecutionException, InterruptedException;
    WriteResult deletar(String idFicha) throws ExecutionException, InterruptedException;
    WriteResult atualizar(String idFicha, Map<String, Object> campos) throws ExecutionException, InterruptedException;
}

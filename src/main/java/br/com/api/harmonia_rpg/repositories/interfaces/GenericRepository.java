package br.com.api.harmonia_rpg.repositories.interfaces;

import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface GenericRepository<T> {

    T adicionar(String idFicha, T entity) throws ExecutionException, InterruptedException;

    T obterPorId(String idFicha, String id) throws ExecutionException, InterruptedException;

    WriteResult deletar(String idFicha, String id) throws ExecutionException, InterruptedException;

    WriteResult atualizar(String idFicha, String id, Map<String, Object> updates) throws ExecutionException, InterruptedException;

    boolean existeDocumento(String idFicha, String id) throws ExecutionException, InterruptedException;

    boolean existeDocPorNome(String idFicha, String campo, String filtro) throws ExecutionException, InterruptedException;

    List<T> obterTodos(String idFicha) throws ExecutionException, InterruptedException;
}

package br.com.api.harmonia_rpg.repositories;

import br.com.api.harmonia_rpg.domain.entities.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
public class UsuarioRepository {
    private static final String COLLECTION_NAME = "usuarios";

    @Autowired
    private Firestore db;

    private CollectionReference getCollection() {
        return db.collection(COLLECTION_NAME);
    }

    public ApiFuture<DocumentSnapshot> salvar(Usuario usuario) throws ExecutionException, InterruptedException {
        DocumentReference usuarioReference = getCollection().add(usuario).get();
        return usuarioReference.get();
    }

    public DocumentSnapshot obterUsuarioPorId(String id) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = getCollection().document(id).get();
        return documentSnapshotApiFuture.get();
    }

    public Usuario buscarPorNomeUsuario(String nomeUsuario) throws UsernameNotFoundException {
        // 1. Cria a query: WHERE nomeUsuario == nomeUsuario
        Query query = db.collection(COLLECTION_NAME)
                .whereEqualTo("nomeUsuario", nomeUsuario)
                .limit(1); // Limita a busca a apenas 1 resultado

        // 2. Executa a query
        ApiFuture<QuerySnapshot> future = query.get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (documents.isEmpty()) {
                return null;
            }

            // 3. Mapeia o primeiro (e único) resultado para o objeto Usuario
            return documents.get(0).toObject(Usuario.class);

        } catch (InterruptedException | ExecutionException e) {
            // Em caso de erro de execução do Firestore
            throw new RuntimeException("Erro ao buscar usuário no Firestore: " + e.getMessage(), e);
        }

    }

    public WriteResult atualizar(String id, HashMap<String, Object> map) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).update(map);
        return writeResultApiFuture.get();
    }

    public WriteResult deletar(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
        return writeResultApiFuture.get();
    }
}

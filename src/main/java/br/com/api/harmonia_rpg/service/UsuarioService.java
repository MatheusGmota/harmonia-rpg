package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.entities.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private Firestore firestore;

    public String criarUsuario(Usuario usuario) {
        try {
            // validar se usuario já existe no banco de dados
            List<QueryDocumentSnapshot> documents = firestore
                    .collection("usuarios")
                    .whereEqualTo("nomeUsuario", usuario.getNomeUsuario())
                    .get()
                    .get().getDocuments();
            if (!documents.isEmpty()) {
                return "Usuário já cadastrado.";
            }

            ApiFuture<DocumentReference> usuarioReference = firestore.collection("usuarios").add(usuario);

            return "Documento salvo, idUsuario: " + usuarioReference.get().getId();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public Usuario obterUsuario(String id) {
        try {
            ApiFuture<DocumentSnapshot> usuario = firestore.collection("usuarios").document(id).get();
            if (!usuario.get().exists()) {
                log.error("Nenhum usuário encontrado");
                return null;
            }

            return usuario.get().toObject(Usuario.class);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public String editarUsuario(String id, Usuario usuario) {
        try {
            ApiFuture<DocumentSnapshot> usuarios = firestore.collection("usuarios").document(id).get();

            if (!usuarios.get().exists()) {
                log.error("Não existe usuário para id: {}", id);
                return null;
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("nomeUsuario", usuario.getNomeUsuario());
            map.put("senha", usuario.getSenha());

            firestore.collection("usuarios").document(id).update(map);

            return "Usuário atualizado em: " + usuarios.get().getUpdateTime();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public String deletarUsuario(String id) {
        try {
            ApiFuture<DocumentSnapshot> usuarios = firestore.collection("usuarios").document(id).get();

            if (!usuarios.get().exists()) {
                log.error("Não existe usuário para id: {}", id);
                return null;
            }

            ApiFuture<WriteResult> deletado = firestore.collection("usuarios").document(id).delete();

            return "Usuário deletado em: " + deletado.get().getUpdateTime();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }


}

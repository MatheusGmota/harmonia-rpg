package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.FichaUsuarioDTO;
import br.com.api.harmonia_rpg.domain.entities.Ficha;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FichaService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Firestore firestore;

    public Ficha obterFicha(String id) {
        try {
            ApiFuture<DocumentSnapshot> fichas = firestore.collection("fichas").document(id).get();
            if (!fichas.get().exists()) {
                log.error("Nenhuma ficha encontrada");
                return null;
            }

            return fichas.get().toObject(Ficha.class);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao obter ficha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<FichaUsuarioDTO> obterFichasDoUsuario(String idUsuario) {
        try {
            Usuario usuario = usuarioService.obterUsuario(idUsuario);
            if (usuario == null) return null;

            ApiFuture<QuerySnapshot> fichas = firestore.collection("fichas")
                    .whereEqualTo("idUsuario", idUsuario)
                    .get();

            return fichas.get().getDocuments()
                    .stream().map(f -> {
                        Ficha o = f.toObject(Ficha.class);

                        return FichaUsuarioDTO.from(o);
                    }).toList();

        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao obter fichas do usuário: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String criarFicha(String idUsuario, Ficha ficha) {
        try {
            Usuario usuario = usuarioService.obterUsuario(idUsuario);
            if (usuario == null) return null;

            if (ficha.getTrilha().getClasse() != ficha.getClasse()) {
                throw new IllegalArgumentException("A trilha selecionada não pertence à classe do personagem.");
            }

            ficha.setIdUsuario(idUsuario);
            ApiFuture<DocumentReference> fichas = firestore.collection("fichas").add(ficha);
            return "Ficha(id: " + fichas.get().getId() + ") cadastrada com sucesso para usuário: " + idUsuario;
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String editarFicha(String id, Ficha ficha) {
        try {
            DocumentReference fichas = firestore.collection("fichas").document(id);

            if (!fichas.get().get().exists()) {
                log.error("Não existe ficha para id: {}", id);
                return null;
            }

            WriteResult writeResult = fichas.set(ficha, SetOptions.merge()).get();

            return "Ficha atualizada em: " + writeResult.getUpdateTime();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao atualizar ficha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String deletarFicha(String id) {
        try {
            DocumentReference fichas = firestore.collection("fichas").document(id);

            if (!fichas.get().get().exists()) {
                log.error("Não existe ficha para id: {}", id);
                return null;
            }

            ApiFuture<WriteResult> deletado = fichas.delete();

            return "Ficha excluída em: " + deletado.get().getUpdateTime();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao excluir ficha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

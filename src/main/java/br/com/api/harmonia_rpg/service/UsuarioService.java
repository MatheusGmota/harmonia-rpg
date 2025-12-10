package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.CadastroRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.CadastroResponseDTO;
import br.com.api.harmonia_rpg.domain.dtos.UsuarioRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.UsuarioResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.exceptions.UserAlreadyExistsException;
import br.com.api.harmonia_rpg.domain.mapper.UsuarioMapper;
import br.com.api.harmonia_rpg.repositories.UsuarioRepository;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository db;

    @Autowired
    private Firestore firestore;

    public CadastroResponseDTO registrarUsuario(CadastroRequestDTO requisicao) {
        try {
            Usuario usuario = UsuarioMapper.from(requisicao);

            // Valida se nomeUsuario é existente
            validarNomeUsuario(usuario.getNomeUsuario(), "Usuário já cadastrado.");

            // Encriptando senha
            usuario.setSenha(encriptarSenha(usuario.getSenha()));

            Usuario salvar = db.salvar(usuario).get().toObject(Usuario.class);

            return new CadastroResponseDTO(salvar.getId(), salvar.getNomeUsuario(), salvar.getCriadoEm());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public UsuarioResponseDTO obterUsuarioPorId(String id) {
        try {
            DocumentSnapshot usuarioPorId = db.obterUsuarioPorId(id);

            if (!usuarioPorId.exists()) {
                log.error("Não existe usuário para id: {}", id);
                throw new NotFoundException("Usuário não encontrado");
            }

            Usuario usuario = usuarioPorId.toObject(Usuario.class);

            return new UsuarioResponseDTO(usuario.getId(), usuario.getNomeUsuario(), usuario.getTipoUsuario(), usuario.getCriadoEm());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> editarUsuario(String id, UsuarioRequestDTO requisicao) {
        try {
            Usuario usuario = UsuarioMapper.from(requisicao);

            obterUsuarioPorId(id); // Faz chamada apenas para validar existência do usuário pelo ID

            validarNomeUsuario(usuario.getNomeUsuario(), "Nome de usuário já utilizado");

            HashMap<String, Object> map = new HashMap<>();
            map.put("nomeUsuario", usuario.getNomeUsuario());

            if (usuario.getSenha() != null) {
                usuario.setSenha(encriptarSenha(usuario.getSenha())); // Encriptando senha
                map.put("senha", usuario.getSenha());
            }

            WriteResult atualizar = db.atualizar(id, map);

            log.info("Usuário id:{} atualizado em: {}", id, atualizar.getUpdateTime());

            return Map.of("id", id, "atualizadoEm", atualizar.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public Map<String, Object> deletarUsuario(String id) {
        try {
            obterUsuarioPorId(id); // Faz chamada apenas para validar existência do usuário pelo ID

            WriteResult atualizar = db.deletar(id);

            log.info("Usuário id:{} deletado em: {}", id, atualizar.getUpdateTime());

            return Map.of("message", "Deletado com sucesso", "atualizadoEm", atualizar.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void validarNomeUsuario(String nomeUsuario, String mensagemErro)  {
        Usuario existente = db.buscarPorNomeUsuario(nomeUsuario); // Caso nomeUsuário não exista irá retornar nulo

        if (existente != null) {
            throw new UserAlreadyExistsException(mensagemErro);
        }
    }

    private String encriptarSenha(String senha) {
        return new BCryptPasswordEncoder().encode(senha);
    }

}

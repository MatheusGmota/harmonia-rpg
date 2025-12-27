package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.*;
import br.com.api.harmonia_rpg.domain.entities.Usuario;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.exceptions.UserAlreadyExistsException;
import br.com.api.harmonia_rpg.domain.mapper.UsuarioMapper;
import br.com.api.harmonia_rpg.infra.security.TokenService;
import br.com.api.harmonia_rpg.repositories.UsuarioRepository;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository db;

    public UsuarioTokenResponseDTO login(LoginRequestDTO requisicao) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(requisicao.nomeUsuario(), requisicao.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            Usuario user = (Usuario) auth.getPrincipal();
            var token = tokenService.generateToken((Usuario) Objects.requireNonNull(auth.getPrincipal()));

            return new UsuarioTokenResponseDTO(user.getId(), user.getUsername(), token, user.getCriadoEm());
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public UsuarioTokenResponseDTO registrarUsuario(CadastroRequestDTO requisicao) {
        try {
            Usuario usuario = UsuarioMapper.from(requisicao);

            // Valida se nomeUsuario é existente
            validarNomeUsuario(usuario.getNomeUsuario(), "Usuário já cadastrado.");

            // Encriptando senha
            usuario.setSenha(encriptarSenha(usuario.getSenha()));

            Usuario usuarioSalvo = db.salvar(usuario).get().toObject(Usuario.class);

            if (usuarioSalvo == null) {
                throw new RuntimeException("Erro ao recuperar usuário após o salvamento.");
            }

            // Gerando token para o login automático
            String token = tokenService.generateToken(usuarioSalvo);

            return new UsuarioTokenResponseDTO(
                    usuarioSalvo.getId(),
                    usuarioSalvo.getNomeUsuario(),
                    token,
                    usuarioSalvo.getCriadoEm()
            );

        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro no processo de cadastro: {}", e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException("Falha ao registrar e autenticar usuário.", e);
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

            WriteResult deletar = db.deletar(id);

            log.info("Usuário id:{} deletado em: {}", id, deletar.getUpdateTime());

            return Map.of("message", "Deletado com sucesso", "atualizadoEm", deletar.getUpdateTime());
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

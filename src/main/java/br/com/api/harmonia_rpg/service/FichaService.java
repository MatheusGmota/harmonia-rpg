package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.FichaRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.FichaResponseDTO;
import br.com.api.harmonia_rpg.domain.dtos.FichaUsuarioDTO;
import br.com.api.harmonia_rpg.domain.dtos.UsuarioResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Ficha;
import br.com.api.harmonia_rpg.domain.enums.TipoUsuario;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.FichaMapper;
import br.com.api.harmonia_rpg.repositories.FichaRepository;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FichaService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FichaRepository db;

    public FichaResponseDTO obterFicha(String id) {
        try {
            DocumentSnapshot fichas = db.obterFichaPorId(id);

            if (!fichas.exists()) {
                log.warn("Nenhuma ficha encontrada");
                throw new NotFoundException("Nenhuma ficha encontrada");
            }

            return fichas.toObject(FichaResponseDTO.class);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao obter ficha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<FichaUsuarioDTO> obterFichasDoUsuario(String idUsuario) {
        try {
            usuarioService.obterUsuarioPorId(idUsuario); // Chamada para validar usuario existente

            List<FichaUsuarioDTO> fichasDoUsuario = db.obterFichasDoUsuario(idUsuario);

            if (fichasDoUsuario.isEmpty()) throw new NotFoundException("Nenhuma ficha encontrada para esse usuário");

            return fichasDoUsuario;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao obter fichas do usuário: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public FichaResponseDTO criarFicha(String idUsuario, FichaRequestDTO dto) {
        try {
            usuarioService.obterUsuarioPorId(idUsuario);

            Ficha ficha = FichaMapper.toFicha(dto, idUsuario);

            if (ficha.getTrilha().getClasse() != ficha.getClasse()) {
                throw new IllegalArgumentException("A trilha selecionada não pertence à classe do personagem.");
            }

            DocumentSnapshot adicionar = db.adicionar(ficha).get();

            return adicionar.toObject(FichaResponseDTO.class);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> editarFicha(String id, String idUsuario, FichaRequestDTO dto) {
        try {
            FichaResponseDTO fichaResponse = obterFicha(id);
            UsuarioResponseDTO usuario = usuarioService.obterUsuarioPorId(idUsuario);

            if (!fichaResponse.idUsuario().equals(idUsuario)) {
                // se usuario for o responsável pela ficha pode editar
                log.warn("A ficha não pertence ao usuário");
                if (!usuario.tipoUsuario().equals(TipoUsuario.MESTRE)) {
                    // se não for só pode editar caso seja do TipoUsuario 'MESTRE'
                    throw new BusinessException("Este usuário não pode editar essa ficha");
                }
            }

            WriteResult atualizar = db.atualizar(id, FichaMapper.toFicha(dto, fichaResponse.idUsuario()));

            return Map.of("message", "Atualizado com sucesso", "atualizadoEm", atualizar.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erro ao atualizar ficha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> deletarFicha(String id, String idUsuario) {
        try {
            usuarioService.obterUsuarioPorId(idUsuario); // validar se usuario é existente

            FichaResponseDTO ficha = obterFicha(id);

            if (!ficha.idUsuario().equals(idUsuario)) {
                // se usuario for o responsável pela ficha pode editar
                log.warn("A ficha não pertence ao usuário");
                throw new BusinessException("Este usuário não pode editar essa ficha");
            }
            WriteResult deletar = db.deletar(id);

            log.info("Usuário id:{} deletado em: {}", id, deletar.getUpdateTime());

            return Map.of("message", "Deletado com sucesso", "atualizadoEm", deletar.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void patchFicha(String idFicha, String idUsuario, Map<String, Object> updates) {
        try {
            DocumentSnapshot doc = db.obterFichaPorId(idFicha);
            if (!doc.getString("idUsuario").equals(idUsuario)) {
                throw new RuntimeException("Acesso negado");
            }

            Map<String, Object> flattenedUpdates = flattenMap(updates, "");

            db.atualizarParcial(idFicha, flattenedUpdates);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar ficha", e);
        }
    }

    private Map<String, Object> flattenMap(Map<String, Object> map, String prefix) {
        Map<String, Object> flattened = new HashMap<>();

        map.forEach((key, value) -> {
            String newKey = (prefix.isEmpty()) ? key : prefix + "." + key;

            if (value instanceof Map) {
                // Se o valor for outro mapa, chama a função recursivamente
                flattened.putAll(flattenMap((Map<String, Object>) value, newKey));
            } else {
                flattened.put(newKey, value);
            }
        });

        return flattened;
    }
}

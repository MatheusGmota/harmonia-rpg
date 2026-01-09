package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.InventarioRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.InventarioResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Ficha;
import br.com.api.harmonia_rpg.domain.entities.Inventario;
import br.com.api.harmonia_rpg.domain.entities.Item;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.InventarioMapper;
import br.com.api.harmonia_rpg.repositories.FichaRepository;
import br.com.api.harmonia_rpg.repositories.InventarioRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class InventarioService {
    @Autowired
    private InventarioRepository repository;

    public InventarioResponseDTO get(String idFicha) {
        try {
            DocumentSnapshot response = repository.obterInvetario(idFicha);

            if (!response.exists()) throw new NotFoundException("Inventário não encontrado");

            return response.toObject(InventarioResponseDTO.class);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public InventarioResponseDTO create(String idFicha, InventarioRequestDTO requestDTO) {
        try {
            Inventario inventario = InventarioMapper.from(requestDTO);
            DocumentSnapshot document = repository.obterInvetario(idFicha);

            if (document.exists()) {
                throw new BusinessException("Inventário já existe para a ficha: " + idFicha);
            }
            DocumentSnapshot response = repository.create(idFicha, inventario);

            return response.toObject(InventarioResponseDTO.class);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public InventarioResponseDTO adicionarItem(String idFicha, Item novoItem) {
        try {
            // 1. Busca do Banco (Ao fazer toObject, o setItens já recalcula a carga automaticamente)
            DocumentSnapshot document = repository.obterInvetario(idFicha);
            if (!document.exists()) throw new NotFoundException("Inventário não encontrado");

            Inventario inventario = document.toObject(Inventario.class);

            // 2. Ação de domínio (adicionarItem já atualiza a carga internamente)
            inventario.adicionarItem(novoItem);

            // 3. Persistência
            repository.update(idFicha, inventario);

            // 4. Retorno transformado
            return InventarioMapper.toDTO(inventario);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Erro ao processar adição de item", e);
        }
    }

    public InventarioResponseDTO removerItem(String idFicha, String nomeItem) {
        try {
            Inventario inventario = buscarEntidade(idFicha);
            inventario.removerItem(nomeItem);

            repository.update(idFicha, inventario);
            return InventarioMapper.toDTO(inventario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover item", e);
        }
    }

    public InventarioResponseDTO editarItem(String idFicha, String nomeItem, Item itemRequest) {
        try {
            Inventario inventario = buscarEntidade(idFicha);
            inventario.editarItem(nomeItem, itemRequest);

            repository.update(idFicha, inventario);
            return InventarioMapper.toDTO(inventario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar item", e);
        }
    }

    // Método auxiliar para evitar repetição de código
    private Inventario buscarEntidade(String idFicha) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = repository.obterInvetario(idFicha);
        if (!document.exists()) throw new NotFoundException("Inventário não encontrado");
        return document.toObject(Inventario.class);
    }
}

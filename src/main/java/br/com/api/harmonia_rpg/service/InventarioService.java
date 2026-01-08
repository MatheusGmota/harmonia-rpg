package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.InventarioRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.InventarioResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Inventario;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.InventarioMapper;
import br.com.api.harmonia_rpg.repositories.InventarioRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

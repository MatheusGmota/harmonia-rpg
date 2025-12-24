package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.AtributoResponseDTO;

import br.com.api.harmonia_rpg.domain.entities.Atributos;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.exceptions.ObjectAlreadyExistsException;
import br.com.api.harmonia_rpg.repositories.AtributoRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AtributoService {
    
    @Autowired
    private AtributoRepository repository;

    @Autowired
    private FichaService fichaService;
    
    public AtributoResponseDTO obterPericias(String idFicha) {
        try {
            DocumentSnapshot documentSnapshot = repository.obterPericias(idFicha);
            if (!documentSnapshot.exists()) throw new NotFoundException("Atributos não encontrados");

            return documentSnapshot.toObject(AtributoResponseDTO.class);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public AtributoResponseDTO criarAtributos(String idFicha, Atributos atributos) {
        try {
            fichaService.obterFicha(idFicha); // Valida se a ficha existe

            if (repository.existePorIdFicha(idFicha)) {
                throw new ObjectAlreadyExistsException("Descrição já existe no id:" + idFicha);
            }

            atributos.setIdFicha(idFicha); // garante vínculo entre a ficha e os atributos

            DocumentSnapshot snapshot = repository.salvar(idFicha, atributos);

            return snapshot.toObject(AtributoResponseDTO.class);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao criar atributos", e);
            throw new RuntimeException(e);
        }
    }

}

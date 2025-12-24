package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Descricao;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.exceptions.ObjectAlreadyExistsException;
import br.com.api.harmonia_rpg.repositories.DescricaoRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class DescricaoService {

    @Autowired
    private DescricaoRepository repository;

    @Autowired
    private FichaService fichaService;

    public DescricaoResponseDTO obterPorId(String idFicha) {
        try {
            DocumentSnapshot documentSnapshot = repository.obterDescricao(idFicha);
            if (!documentSnapshot.exists()) throw new NotFoundException("Descrição não encontrada");

            return documentSnapshot.toObject(DescricaoResponseDTO.class);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public DescricaoResponseDTO criar(String idFicha, Descricao descricao) {
        try {
            fichaService.obterFicha(idFicha);

            if (repository.existePorIdFicha(idFicha)) {
                throw new ObjectAlreadyExistsException("Descrição já existe no id:" + idFicha);
            }

            descricao.setIdFicha(idFicha); // garante vínculo entre a ficha e os atributos

            DocumentSnapshot snapshot = repository.salvar(idFicha, descricao);
            return snapshot.toObject(DescricaoResponseDTO.class);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

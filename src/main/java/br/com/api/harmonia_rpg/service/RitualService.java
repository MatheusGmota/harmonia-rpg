package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.repositories.RitualRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RitualService {

    @Autowired
    private RitualRepository repository;

    @Autowired
    private FichaService fichaService;

    public List<Ritual> get(String idFicha) {
        try {
            DocumentSnapshot document = repository.obterDocumento(idFicha);

            if (!document.exists()) {
                throw new NotFoundException("Rituais não encontrados para a ficha: " + idFicha);
            }

            return repository.obterRituais(idFicha);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ritual> create(String idFicha, Ritual ritual) {
        try {
            fichaService.obterFicha(idFicha); // verifica se a ficha existe

            List<Ritual> rituais;

            // verifica se já existe documento de rituais
            if (!repository.existePorIdFicha(idFicha)) {
                rituais = new ArrayList<>();
            } else {
                rituais = repository.obterRituais(idFicha);
            }

            rituais.add(ritual);
            return repository.salvar(idFicha, rituais);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


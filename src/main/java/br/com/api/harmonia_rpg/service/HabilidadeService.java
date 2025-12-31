package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.entities.Habilidade;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.repositories.HabilidadeRepository;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class HabilidadeService {
    @Autowired
    private HabilidadeRepository repository;

    @Autowired
    private FichaService fichaService;

    public List<Habilidade> get(String idFicha) {
        try {
            DocumentSnapshot document = repository.obterDocumento(idFicha);

            if (!document.exists()) {
                throw new NotFoundException("Habilidades não encontrados para a ficha: " + idFicha);
            }

            return repository.obterHabilidades(idFicha);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Habilidade> create(String idFicha, Habilidade ritual) {
        try {
            fichaService.obterFicha(idFicha); // verifica se a ficha existe

            List<Habilidade> habilidades;

            // verifica se já existe documento de habilidades
            if (!repository.existePorIdFicha(idFicha)) {
                habilidades = new ArrayList<>();
            } else {
                habilidades = repository.obterHabilidades(idFicha);
            }

            habilidades.add(ritual);
            return repository.salvar(idFicha, habilidades);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

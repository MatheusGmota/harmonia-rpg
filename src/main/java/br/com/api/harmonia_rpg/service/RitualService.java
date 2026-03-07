package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.RitualEditRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.RitualResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.RitualMapper;
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

    public List<RitualResponseDTO> update(String idFicha, RitualEditRequestDTO request) {
        try {
            if (request == null) throw new BusinessException("Objeto não pode ser nulo");
            Ritual ritual = RitualMapper.toRitual(request);

            List<Ritual> rituais = repository.obterRituais(idFicha);
            if (rituais.isEmpty()) {
                throw new NotFoundException("Não existe nenhuma lista de rituais");
            }

            Ritual ritualRecuperado = rituais.get(request.index());
            if (ritualRecuperado.equals(ritual)) return RitualMapper.toDtoList(rituais);

            rituais.set(request.index(), ritual);

            List<Ritual> res = repository.salvar(idFicha, rituais);
            return RitualMapper.toDtoList(res);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Índice indicado está fora do limite");
        }
    }

    public void delete(String idFicha, int index) {
        try {
            List<Ritual> rituais = repository.obterRituais(idFicha);
            if (rituais.isEmpty()) {
                throw new NotFoundException("Não existe nenhuma lista de rituais");
            }
            Ritual ritual = rituais.get(index);
            rituais.remove(ritual);

            repository.salvar(idFicha, rituais);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException e) {
        throw new RuntimeException("Índice indicado está fora do limite");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível deletar ritual");
        }
    }
}


package br.com.api.harmonia_rpg.service;

import br.com.api.harmonia_rpg.domain.dtos.AtaqueRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.AtaqueResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Ataque;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.AtaqueMapper;
import br.com.api.harmonia_rpg.repositories.AtaqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AtaqueService {
    @Autowired
    private AtaqueRepository repository;

    public List<AtaqueResponseDTO> obter(String idFicha) {
        try {
            List<Ataque> ataques = repository.obterAtaques(idFicha);
            if (ataques.isEmpty()) throw new NotFoundException("Nenhum ataque encontrado");
            return AtaqueMapper.toDtoList(ataques);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AtaqueResponseDTO> adicionar(String idFicha, AtaqueRequestDTO request) {
        try {
            Ataque ataque = AtaqueMapper.toAtaque(request);

            List<Ataque> ataques = repository.obterAtaques(idFicha);

            if (ataques == null) {
                ataques = new ArrayList<>();
            }

            ataques.add(ataque);

            List<Ataque> res = repository.salvar(idFicha, ataques);
            return AtaqueMapper.toDtoList(res);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AtaqueResponseDTO> editar(String idFicha, int index, AtaqueRequestDTO request) {
        try {
            if (request == null) throw new BusinessException("Objeto não pode ser nulo");
            Ataque ataque = AtaqueMapper.toAtaque(request);

            List<Ataque> ataques = repository.obterAtaques(idFicha);
            if (ataques.isEmpty()) {
                throw new NotFoundException("Não existe nenhuma lista de habilidades");
            }

            Ataque ataqueRecuperado = ataques.get(index);
            if (ataqueRecuperado.equals(ataque)) return AtaqueMapper.toDtoList(ataques);

            ataques.set(index, ataque);

            List<Ataque> res = repository.salvar(idFicha, ataques);
            return AtaqueMapper.toDtoList(res);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Índice indicado está fora do limite");
        }
    }

    public List<AtaqueResponseDTO> remover(String idFicha, int index) {
        try {
            List<Ataque> ataques = repository.obterAtaques(idFicha);
            if (ataques.isEmpty()) {
                throw new NotFoundException("Não existe nenhuma lista de habilidades");
            }

            Ataque ataqueRecuperado = ataques.get(index);

            ataques.remove(ataqueRecuperado);

            List<Ataque> res = repository.salvar(idFicha, ataques);
            return AtaqueMapper.toDtoList(res);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Índice indicado está fora do limite");
        }
    }
}

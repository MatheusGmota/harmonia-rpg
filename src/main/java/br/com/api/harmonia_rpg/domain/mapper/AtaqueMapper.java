package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.AtaqueRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.AtaqueResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Ataque;
import br.com.api.harmonia_rpg.domain.dtos.AtaqueEditRequestDTO;

import java.util.List;

public class AtaqueMapper {

    public static Ataque toAtaque(AtaqueEditRequestDTO dto) {
        Ataque ataque  = new Ataque();

        ataque.setNome(dto.nome());
        ataque.setTeste(dto.teste());
        ataque.setDano(dto.dano());

        ataque.setCritico(dto.critico());
        ataque.setAlcance(dto.alcance());
        ataque.setEspecial(dto.especial());

        return ataque;
    }

    public static Ataque toAtaque(AtaqueRequestDTO dto) {
        Ataque ataque  = new Ataque();

        ataque.setNome(dto.nome());
        ataque.setTeste(dto.teste());
        ataque.setDano(dto.dano());

        ataque.setCritico(dto.critico());
        ataque.setAlcance(dto.alcance());
        ataque.setEspecial(dto.especial());

        return ataque;
    }

    public static List<AtaqueResponseDTO> toDtoList(List<Ataque> ataques) {
        return ataques
                .stream()
                .map(x ->
                        new AtaqueResponseDTO(
                                x.getNome(),
                                x.getTeste(),
                                x.getDano(),
                                x.getCritico(),
                                x.getAlcance(),
                                x.getEspecial()
                        )
                ).toList();
    }
}

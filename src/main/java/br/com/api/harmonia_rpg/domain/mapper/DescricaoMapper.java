package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;
import br.com.api.harmonia_rpg.domain.entities.Descricao;
import org.springframework.stereotype.Component;

@Component
public class DescricaoMapper {

    public static Descricao toDescricao(DescricaoDTO dto) {
        Descricao descricao = new Descricao();

        descricao.setAparencia(dto.aparencia());
        descricao.setPersonalidade(dto.personalidade());
        descricao.setHistorico(dto.historico());
        descricao.setObjetivo(dto.objetivo());

        return descricao;
    }
}

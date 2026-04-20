package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;
import br.com.api.harmonia_rpg.domain.entities.Descricao;
public class DescricaoMapper {

    public static Descricao toDescricao(DescricaoDTO.RequestDTO dto) {
        Descricao descricao = new Descricao();

        descricao.setAparencia(dto.aparencia());
        descricao.setPersonalidade(dto.personalidade());
        descricao.setHistorico(dto.historico());
        descricao.setObjetivo(dto.objetivo());

        return descricao;
    }
    public static DescricaoDTO.ResponseDTO toDescricaoDto(Descricao d) {
        return new DescricaoDTO.ResponseDTO(
                d.getId(),
                d.getAparencia(),
                d.getPersonalidade(),
                d.getHistorico(),
                d.getObjetivo()
        );
    }
}

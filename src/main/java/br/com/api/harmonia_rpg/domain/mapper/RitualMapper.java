package br.com.api.harmonia_rpg.domain.mapper;


import br.com.api.harmonia_rpg.domain.dtos.DescricaoDTO;
import br.com.api.harmonia_rpg.domain.dtos.RitualDTO;
import br.com.api.harmonia_rpg.domain.dtos.RitualEditRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.RitualResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.CustoRitual;
import br.com.api.harmonia_rpg.domain.entities.Descricao;
import br.com.api.harmonia_rpg.domain.entities.Ritual;
import br.com.api.harmonia_rpg.domain.enums.TipoElemento;

import java.util.List;

public class RitualMapper {

    public static List<RitualResponseDTO> toDtoList(List<Ritual> rituals) {
        return rituals
                .stream()
                .map(x ->
                        new RitualResponseDTO(
                                x.getNomeRitual(),
                                x.getTipoElemento(),
                                x.getCustoRitual(),
                                x.getExecucao(),
                                x.getAlcance(),
                                x.getAlvo(),
                                x.getDuracao(),
                                x.getResistencia(),
                                x.getDescricao(),
                                x.getDtRitual(),
                                x.getDanoSanidade(),
                                x.getCirculo())
                ).toList();
    }

    @Deprecated
    public static Ritual toRitual(RitualEditRequestDTO dto) {
        Ritual ritual = new Ritual();

        ritual.setNomeRitual(dto.nomeRitual());
        ritual.setTipoElemento(dto.tipoElemento());
        ritual.setCustoRitual(dto.custoRitual());
        ritual.setExecucao(dto.execucao());
        ritual.setAlcance(dto.alcance());
        ritual.setAlvo(dto.alvo());
        ritual.setDuracao(dto.duracao());
        ritual.setResistencia(dto.resistencia());
        ritual.setDescricao(dto.descricao());
        ritual.setDtRitual(dto.dtRitual());
        ritual.setDanoSanidade(dto.danoSanidade());
        ritual.setCirculo(dto.circulo());

        return ritual;
    }

    public static Ritual toRitual(RitualDTO.RitualRequestDTO dto) {
        Ritual ritual = new Ritual();

        ritual.setNomeRitual(dto.nomeRitual());
        ritual.setTipoElemento(dto.tipoElemento());
        ritual.setCustoRitual(dto.custoRitual());
        ritual.setExecucao(dto.execucao());
        ritual.setAlcance(dto.alcance());
        ritual.setAlvo(dto.alvo());
        ritual.setDuracao(dto.duracao());
        ritual.setResistencia(dto.resistencia());
        ritual.setDescricao(dto.descricao());
        ritual.setDtRitual(dto.dtRitual());
        ritual.setDanoSanidade(dto.danoSanidade());
        ritual.setCirculo(dto.circulo());

        return ritual;
    }

    public static RitualDTO.RitualResponseDTO toRitualDto(Ritual r) {
        return new RitualDTO.RitualResponseDTO(
                r.getIdRitual(),
                r.getNomeRitual(),
                r.getTipoElemento(),
                r.getCustoRitual(),

                r.getExecucao(),
                r.getAlcance(),
                r.getAlvo(),
                r.getDuracao(),
                r.getResistencia(),
                r.getDescricao(),
                r.getDtRitual(),

                r.getDanoSanidade(),
                r.getCirculo()
        );
    }
}

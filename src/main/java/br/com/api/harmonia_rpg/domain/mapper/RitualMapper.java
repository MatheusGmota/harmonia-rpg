package br.com.api.harmonia_rpg.domain.mapper;


import br.com.api.harmonia_rpg.domain.dtos.RitualEditRequestDTO;
import br.com.api.harmonia_rpg.domain.dtos.RitualResponseDTO;
import br.com.api.harmonia_rpg.domain.entities.Ritual;

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
}

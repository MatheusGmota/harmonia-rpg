package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.domain.entities.Agente;
import org.springframework.stereotype.Component;

@Component
public class AgenteMapper {

    public static Agente toAgente(AgenteDTO.AgenteRequestDTO dto, String idUsuario) {
        Agente agente = new Agente();

        agente.setIdUsuario(idUsuario);

        agente.setNome(dto.nome());
        agente.setIdade(dto.idade());
        agente.setImagemUrl(dto.imagemUrl());
        agente.setOrigem(dto.origem());
        agente.setClasse(dto.classe());
        agente.setTrilha(dto.trilha());
        agente.setAgilidade(dto.agilidade());
        agente.setForca(dto.forca());
        agente.setIntelecto(dto.intelecto());
        agente.setPresenca(dto.presenca());
        agente.setVigor(dto.vigor());

        return agente;
    }

    public static AgenteDTO.AgenteResponseDTO toAgenteDto(Agente a) {
        return new AgenteDTO.AgenteResponseDTO(
                a.getId(),
                a.getIdUsuario(),
                a.getNome(),
                a.getIdade(),
                a.getImagemUrl(),
                a.getOrigem(),
                a.getClasse(),
                a.getTrilha(),
                a.getAfinidade(),
                a.getNivelExposicao(),
                a.getEsforcoPorRodada(),
                a.getAgilidade(),
                a.getForca(),
                a.getIntelecto(),
                a.getPresenca(),
                a.getVigor(),
                a.getPontosDeVida(),
                a.getPontosDeEsforco(),
                a.getPontosDeSanidade(),
                a.getDefesa(),
                a.getDefesaEsquiva(),
                a.getRedDanoBloqueando(),
                a.getProtecoes(),
                a.getResistencia(),
                a.getDeslocamento(),
                a.getCriadoEm()
        );
    }
}
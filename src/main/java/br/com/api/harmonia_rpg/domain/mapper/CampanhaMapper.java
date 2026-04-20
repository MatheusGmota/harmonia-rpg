package br.com.api.harmonia_rpg.domain.mapper;

import br.com.api.harmonia_rpg.domain.dtos.CampanhaDTO;
import br.com.api.harmonia_rpg.domain.dtos.ConviteDTO;
import br.com.api.harmonia_rpg.domain.entities.Agente;
import br.com.api.harmonia_rpg.domain.entities.Campanha;
import br.com.api.harmonia_rpg.domain.entities.Convite;
import br.com.api.harmonia_rpg.domain.enums.StatusCampanha;

import java.util.ArrayList;

public class CampanhaMapper {
    private CampanhaMapper() {}

    /**
     * Converte o RequestDTO em entidade Campanha.
     * O {@code idMestre} é sempre o usuário autenticado, nunca vindo do payload.
     */
    public static Campanha toCampanha(CampanhaDTO.RequestDTO dto, String idMestre) {
        Campanha campanha = new Campanha();
        campanha.setNome(dto.nome());
        campanha.setDescricao(dto.descricao());
        campanha.setImagemCapaUrl(dto.imagemCapaUrl());
        campanha.setIdMestre(idMestre);
        campanha.setIdJogadores(new ArrayList<>());
        campanha.setIdAgentes(new ArrayList<>());
        campanha.setStatus(StatusCampanha.ATIVA);
        return campanha;
    }

    /**
     * Converte Campanha em ResponseDTO.
     * O campo {@code papel} é calculado com base no usuário logado:
     * "MESTRE" se for o criador da campanha, "JOGADOR" caso contrário.
     */
    public static CampanhaDTO.ResponseDTO toResponseDTO(Campanha campanha, String idUsuarioLogado) {
        String papel = campanha.getIdMestre().equals(idUsuarioLogado) ? "MESTRE" : "JOGADOR";
        return new CampanhaDTO.ResponseDTO(
                campanha.getId(),
                campanha.getNome(),
                campanha.getDescricao(),
                campanha.getImagemCapaUrl(),
                campanha.getIdMestre(),
                campanha.getIdJogadores(),
                campanha.getIdAgentes(),
                campanha.getStatus(),
                papel,
                campanha.getCriadoEm(),
                campanha.getAtualizadoEm()
        );
    }

    /**
     * Converte Convite em ResponseDTO montando o link completo a partir da baseUrl.
     */
    public static ConviteDTO.ResponseDTO toConviteResponseDTO(Convite convite, String baseUrl) {
        String linkConvite = baseUrl + "/" + convite.getToken();
        return new ConviteDTO.ResponseDTO(
                convite.getId(),
                convite.getIdCampanha(),
                convite.getNomeCampanha(),
                convite.getToken(),
                linkConvite,
                convite.getStatus(),
                convite.getExpiraEm()
        );
    }

    public static CampanhaDTO.AgenteNaCampanhaDTO toAgenteNaCampanhaDTO(Agente agente, String nomeCampanha) {
        return new CampanhaDTO.AgenteNaCampanhaDTO(
                agente.getId(),
                agente.getImagemUrl(),
                agente.getNome(),
                nomeCampanha,
                agente.getCriadoEm()
        );
    }
}

package br.com.api.harmonia_rpg.service.interfaces;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.domain.dtos.CampanhaDTO;
import br.com.api.harmonia_rpg.domain.dtos.ConviteDTO;

import java.util.List;
import java.util.Map;

public interface CampanhaService {
    CampanhaDTO.CampanhaResponseDTO criar(String idUsuario, CampanhaDTO.CampanhaRequestDTO dto);

    CampanhaDTO.CampanhaResponseDTO obter(String idUsuario, String idCampanha);

    List<CampanhaDTO.CampanhaResponseDTO> obterMinhas(String idUsuario);

    Map<String, Object> editar(String idUsuario, String idCampanha, Map<String, Object> updates);

    Map<String, Object> deletar(String idUsuario, String idCampanha);

    // ---- Agentes ----
    List<CampanhaDTO.AgenteNaCampanhaDTO> obterAgentesDaCampanha(String idUsuario, String idCampanha);

    CampanhaDTO.CampanhaResponseDTO adicionarAgente(String idUsuario, String idCampanha, String idAgente);

    Map<String, Object> editarAgente(String idUsuario, String idCampanha, String idAgente, Map<String, Object> updates);

    Map<String, Object> removerAgente(String idUsuario, String idCampanha, String idAgente);

    // ---- Convites por link/token ----

    /** Gera (ou reutiliza) o link de convite da campanha. Exclusivo do Mestre. */
    ConviteDTO.ConviteResponseDTO gerarLinkConvite(String idUsuario, String idCampanha);

    /** Jogador logado usa o token do link para entrar na campanha. */
    ConviteDTO.AceitarResponseDTO aceitarPorToken(String idUsuario, String token);

    /** Mestre invalida todos os links ativos da campanha. */
    Map<String, Object> revogarLinkConvite(String idUsuario, String idCampanha);

    // ---- Gestão de jogadores ----


    Map<String, Object> removerJogador(String idMestre, String idCampanha, String idJogador);
}

package br.com.api.harmonia_rpg.domain.dtos;

import br.com.api.harmonia_rpg.domain.enums.StatusConvite;
import com.google.cloud.Timestamp;

public class ConviteDTO {

    public record ResponseDTO(
            String id,
            String idCampanha,
            String nomeCampanha,
            String token,
            String linkConvite, // link completo montado no service
            StatusConvite status,
            Timestamp expiraEm
    ) {}

    public record AceitarResponseDTO(
            String idCampanha,
            String nomeCampanha,
            String mensagem
    ) {}
}
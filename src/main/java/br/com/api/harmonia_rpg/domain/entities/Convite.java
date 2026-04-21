package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.StatusConvite;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Convite {
    @DocumentId
    private String id;
    private String idCampanha;
    private String nomeCampanha;
    private String idMestre;
    private String token;
    private String idUsuarioConvidado; // preenchido ao aceitar

    private StatusConvite status;

    @ServerTimestamp
    private Timestamp criadoEm;

    private Timestamp expiraEm;
}

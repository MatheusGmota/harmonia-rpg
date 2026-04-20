package br.com.api.harmonia_rpg.domain.entities;

import br.com.api.harmonia_rpg.domain.enums.StatusCampanha;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campanha {

    @DocumentId
    private String id;
    private String nome;
    private String descricao;
    private String imagemCapaUrl;
    private String idMestre; // ID do criador

    private List<String> idJogadores = new ArrayList<>();
    private List<String> idAgentes = new ArrayList<>();
    private List<String> idAmeacas = new ArrayList<>();

    private StatusCampanha status;

    @ServerTimestamp
    private Timestamp criadoEm;

    @ServerTimestamp
    private Timestamp atualizadoEm;
}

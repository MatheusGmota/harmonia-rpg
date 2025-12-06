package br.com.api.harmonia_rpg.domain.entities;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @DocumentId
    private String id;

    private String nomeUsuario;

    private String senha;

    @ServerTimestamp
    private Timestamp criadoEm;
}

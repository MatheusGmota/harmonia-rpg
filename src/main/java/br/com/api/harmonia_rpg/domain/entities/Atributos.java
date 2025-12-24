package br.com.api.harmonia_rpg.domain.entities;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Atributos {
    @DocumentId
    private String idFicha;
    private List<Pericia> agilidade;
    private List<Pericia> forca;
    private List<Pericia> intelecto;
    private List<Pericia> presenca;
    private List<Pericia> vigor;
}

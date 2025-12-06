package br.com.api.harmonia_rpg.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pontos {
    private Integer atual;
    private Integer total;
}

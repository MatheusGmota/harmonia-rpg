package br.com.api.harmonia_rpg.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimiteItens {
    private int categoriaI;
    private int categoriaII;
    private int categoriaIII;
    private int categoriaIV;
    private int categoriaV;
    private int categoriaVI;
}

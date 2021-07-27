package br.usp.haroldo.epwebsemantica.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LojaDTO {
    private String nome;
    private String descricao;
    private String loja;
}

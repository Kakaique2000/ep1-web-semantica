package br.usp.haroldo.epwebsemantica.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LojaDTO {
    private String nome;
    private String atividade;
    private String uri;
}

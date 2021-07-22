package br.usp.haroldo.epwebsemantica.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LojaDTO {
    private String nome;
    private String atividade;
    private String uri;
    private String fotoUrl;
    private String descricao;
}

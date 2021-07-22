package br.usp.haroldo.epwebsemantica.models;

import lombok.Data;

@Data
public class ProdutoDTO {
        private String nome;
        private String categoria;
        private String uri;
        private String fotoUrl;
        private String descricao;
        private double preco;
}

package br.usp.haroldo.epwebsemantica.models;

import lombok.Data;

@Data
public class ProdutoDTO {

        private String nome;
        private String loja;
        private String lojaLabel;
        private String lojaAtividade;
        private String uri;
        private String fotoUrl;
        private int codigo;
        private double preco;
        private int qtd;
}

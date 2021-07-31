package br.usp.haroldo.epwebsemantica.models;

public enum LojaPesquisarPorEnum {

    NOME("nome"),
    ATIVIDADE("atividade");

    public String label;

    LojaPesquisarPorEnum(String label) {
        this.label = label;
    }

}

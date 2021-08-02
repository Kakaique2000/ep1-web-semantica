package br.usp.haroldo.epwebsemantica.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String nome;
    private String token;
    private String uri;
}

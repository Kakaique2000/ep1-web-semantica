package br.usp.haroldo.epwebsemantica.controllers;

import br.usp.haroldo.epwebsemantica.configuration.OntologyConfig;
import br.usp.haroldo.epwebsemantica.models.LojaDTO;
import br.usp.haroldo.epwebsemantica.services.RDFLoader;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
public class MainController {

    @Autowired
    OntologyConfig ontology;

    @Autowired
    Model model;

    @GetMapping("/lojas")
    public List<LojaDTO> getLojas() throws IOException {
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT ?uri ?nome ?atividadeRes ?atividade ?fotoUrl " +
                        "WHERE{ " +
                        "{ ?uri a <http://www.web-semantica/ep/parte_1#LojaServicos> } UNION { ?uri a <http://www.web-semantica/ep/parte_1#LojaProdutos> } ." +
                        "?uri rdfs:label ?nome . " +
                        " OPTIONAL { ?uri parte_1:foto ?fotoUrl } . " +
                        "?uri parte_1:oferece ?atividadeRes ." +
                        "?atividadeRes rdfs:label ?atividade" +
                        "}";

        List<LojaDTO> lojas = ontology.executeQueryToType(queryString, LojaDTO.class);
        return lojas;
    }
}

/*
!!!!FILTER (?nome = "bakery") -> para buscar loja com certo nome   !!!!

Listar Loja por nome
@GetMapping("/lojas")
    public List<LojaDTO> getLojas() throws IOException {
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                        "WHERE" +
                          "{   { SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE
                                  "{ ?uri  rdf:type    parte_1:LojaServicos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?uri      parte_1:oferece  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                            "UNION" +
                              "{ SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE" +
                                  "{ ?uri  rdf:type    parte_1:LojaProdutos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?uri      parte_1:vende  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                          "}";

        List<LojaDTO> lojas = ontology.executeQueryToType(queryString, LojaDTO.class);
        return lojas;
    }
Listar Loja por atividade
@GetMapping("/lojas")
    public List<LojaDTO> getLojasByAtividade() throws IOException {
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?atividade ?uri ?nome ?atividadeRes ?fotoUrl" +
                        "WHERE" +
                          "{   { SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE
                                  "{ ?uri  rdf:type    parte_1:LojaServicos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?uri      parte_1:oferece  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                            "UNION" +
                              "{ SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE" +
                                  "{ ?uri  rdf:type    parte_1:LojaProdutos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?uri      parte_1:vende  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                          "}" +
                          "ORDER BY ?atividade";

        List<LojaDTO> lojas = ontology.executeQueryToType(queryString, LojaDTO.class);
        return lojas;
    }
Listar Loja por Orderm Alfabetica
@GetMapping("/lojas")
    public List<LojaDTO> getLojasOrdered() throws IOException {
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?nome ?uri ?atividadeRes ?atividade ?fotoUrl" +
                        "WHERE" +
                          "{   { SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE
                                  "{ ?uri  rdf:type    parte_1:LojaServicos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?uri      parte_1:oferece  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                            "UNION" +
                              "{ SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE" +
                                  "{ ?uri  rdf:type    parte_1:LojaProdutos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?uri      parte_1:vende  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                          "}" +
                          "ORDER BY ?nome";

        List<LojaDTO> lojas = ontology.executeQueryToType(queryString, LojaDTO.class);
        return lojas;
    }
Listar Rotas de Lojas 
@GetMapping("/lojas")
    public List<LojaDTO> getRotas() throws IOException {
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?nome ?rota ?uri ?atividadeRes ?atividade ?fotoUrl" +
                        "WHERE" +
                          "{   { SELECT  ?rota ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE
                                  "{ ?uri  rdf:type    parte_1:LojaServicos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?rota     rdf:type         parte_1:Rota ;" +
                                    "parte_1:levaA    ?uri ;" +
                                    "rdfs:label       ?nomeRota ." +
                                    "?uri      parte_1:oferece  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                            "UNION" +
                              "{ SELECT  ?uri ?rota ?nome ?atividadeRes ?atividade ?fotoUrl" +
                                "WHERE" +
                                  "{ ?uri  rdf:type    parte_1:LojaProdutos ;" +
                                          "rdfs:label  ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                    "?rota     rdf:type         parte_1:Rota ;" +
                                    "parte_1:levaA    ?uri ;" +
                                    "rdfs:label       ?nomeRota ." +
                                    "?uri      parte_1:vende  ?atividadeRes ." +
                                    "?atividadeRes" +
                                              "rdfs:label       ?atividade" +
                                  "}" +
                              "}" +
                          "}" +
                          "ORDER BY ?nome";

        List<LojaDTO> lojas = ontology.executeQueryToType(queryString, LojaDTO.class);
        return lojas;
    }
    
Listar Produtos
@GetMapping("/lojas")
    public List<ProdutoDTO> getProducts() throws IOException {
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?uri ?nome ?atividadeRes ?atividade ?fotoUrl" +
                        "WHERE" +
                          "{   { SELECT  ?nome ?uri ?fotoUrl" +
                                "WHERE
                                  "{ ?uriLoja  rdf:type    parte_1:LojaServicos ;" +
                                          "rdfs:label  ?nomeLoja" +
                                    "?uriLoja      parte_1:oferece  ?atividadeRes ." +
                                    "?uri" +
                                              "rdfs:label       ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                  "}" +
                              "}" +
                            "UNION" +
                              "{ SELECT  ?nome ?uri ?fotoUrl" +
                                "WHERE" +
                                  "{ ?uriLoja  rdf:type    parte_1:LojaProdutos ;" +
                                          "rdfs:label  ?nomeLoja" +
                                    "?uriLoja      parte_1:vende  ?atividadeRes ." +
                                    "?uri" +
                                              "rdfs:label       ?nome" +
                                    "OPTIONAL" +
                                      "{ ?uri  parte_1:foto  ?fotoUrl }" +
                                  "}" +
                              "}" +
                          "}";

        List<ProdutoDTO> produtos = ontology.executeQueryToType(queryString, ProdutoDTO.class);
        return produtos;
    }
*/

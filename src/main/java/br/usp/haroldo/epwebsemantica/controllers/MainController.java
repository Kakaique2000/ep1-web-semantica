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

    //Listar Loja ordenado por nome
    @GetMapping("/lojas")
    public List<LojaDTO> getLojasByNome() throws IOException {
        String queryString =
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?nome ?atividade ?uri ?rota" +
                        "WHERE" +
                        "{ ?uri  rdf:type           parte_1:Loja ;" +
                                "rdfs:label         ?nome ;" +
                                "parte_1:Atividade  ?atividade ;" +
                                "parte_1:estaEm     ?rota" +
                        "}" +
                        "ORDER BY ?nome";

        List<LojaDTO> lojas = ontology.executeQueryToType(queryString, LojaDTO.class);
        return lojas;
    }

    //Listar Loja por ordenado por atividade
    @GetMapping("/lojas")
    public List<LojaDTO> getLojasByAtividade() throws IOException {
        String queryString =
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?nome ?atividade ?uri ?rota" +
                        "WHERE" +
                        "{ ?uri  rdf:type           parte_1:Loja ;" +
                                "rdfs:label         ?nome ;" +
                                "parte_1:Atividade  ?atividade ;" +
                                "parte_1:estaEm     ?rota" +
                        "}" +
                        "ORDER BY ?atividade";

        List<LojaDTO> lojas = ontology.executeQueryToType(queryString, LojaDTO.class);
        return lojas;
    }

    //Listar Produtos
    @GetMapping("/produtos")
    public List<ProdutoDTO> getProdutos(LojaDTO loja) throws IOException {
        String queryString =
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?nome ?loja ?uri ?fotoUrl ?codigo ?preco ?qtd" +
                        "WHERE" +
                        "{ ?uri  rdf:type                   parte_1:Produto ;" +
                                "rdfs:label                 ?nome ;" +
                                "parte_1:vendidoPor         ?loja ;" +
                                "parte_1:foto               ?fotoUrl ;" +
                                "parte_1:codigo             ?codigo ;" +
                                "parte_1:preco              ?preco ;" +
                                "parte_1:quantidadeEstoque  ?qtd" +
                            "FILTER ( ?loja = "+ loja.uri +" )" +
                        "}" + 
                        "ORDER BY ?nome";

        List<ProdutoDTO> produtos = ontology.executeQueryToType(queryString, ProdutoDTO.class);
        return produtos;
    }

    //Listar Rotas
    @GetMapping("/rotas")
    public List<RotaDTO> getRotas(LojaDTO loja) throws IOException {
        String queryString =
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?nome ?descricao ?loja" +
                        "WHERE" +
                        "{ ?uri  rdf:type       parte_1:Rota ;" +
                                "rdfs:label     ?nome ;" +
                                "rdfs:comment   ?descricao ;" +
                                "parte_1:levaA  ?loja ;" 
                            "FILTER ( ?loja = "+ loja.uri +" )" +
                        "}";

        List<RotaDTO> rotas = ontology.executeQueryToType(queryString, RotaDTO.class);
        return rotas;
    }
}

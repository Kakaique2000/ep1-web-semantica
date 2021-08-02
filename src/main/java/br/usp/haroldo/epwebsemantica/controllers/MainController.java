package br.usp.haroldo.epwebsemantica.controllers;

import br.usp.haroldo.epwebsemantica.configuration.OntologyConfig;
import br.usp.haroldo.epwebsemantica.models.*;
import org.apache.jena.rdf.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("")
public class MainController {

    @Autowired
    OntologyConfig ontology;

    @Autowired
    Model model;

    @GetMapping("/lojas")
    public List<LojaDTO> getLojas(
            @RequestParam(value = "ordenar", defaultValue = "nome") String ordenar,
            @RequestParam(value = "pesquisa", defaultValue = "") String pesquisa) throws IOException {
            switch (LojaOrdenarPorEnum.valueOf(ordenar.toUpperCase(Locale.ROOT))) {
            case NOME: return filtraListaLojaComParametro(getLojasByNome(), pesquisa);
            case ATIVIDADE: return filtraListaLojaComParametro(getLojasByAtividade(), pesquisa);
            default: return filtraListaLojaComParametro(getLojasByNome(), pesquisa);
        }
    }


    //Listar Loja ordenado por nome
    private List<LojaDTO> getLojasByNome() throws IOException {
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
    private List<LojaDTO> getLojasByAtividade() throws IOException {
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
    public List<ProdutoDTO> getProdutos(
            @RequestParam(value = "loja", defaultValue = "") String lojaUri,
            @RequestParam(value = "pesquisa", defaultValue = "") String pesquisa,
            @RequestParam(value = "tipo_pesquisa", defaultValue = "nome") String tipoPesquisa
    ) throws IOException {
        String queryString =
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT  ?nome ?loja ?uri ?fotoUrl ?lojaLabel ?lojaAtividade ?codigo ?preco ?qtd" +
                        "WHERE" +
                        "{ ?uri  rdf:type                   parte_1:Produto ;" +
                                "rdfs:label                 ?nome ;" +
                                "parte_1:vendidoPor         ?loja ;" +
                                "parte_1:foto               ?fotoUrl ;" +
                                "parte_1:codigo             ?codigo ;" +
                                "parte_1:preco              ?preco ;" +
                                "parte_1:quantidadeEstoque  ?qtd ." +
                                "?loja rdfs:label ?lojaLabel ." +
                                "?loja parte_1:Atividade ?lojaAtividade ." +
                        "}" + 
                        "ORDER BY ?nome";

        List<ProdutoDTO> produtos = ontology.executeQueryToType(queryString, ProdutoDTO.class);
        List<ProdutoDTO> produtoFiltrados = filtraListaProdutoComLoja(produtos, lojaUri);
        if(tipoPesquisa.toUpperCase(Locale.ROOT).equals("ATIVIDADE"))
            return filtraPesquisaProdutoAtividade(produtoFiltrados, pesquisa);
        else return filtraPesquisaProdutoNome(produtoFiltrados, pesquisa);
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
                                "parte_1:levaA  ?loja ;" +
                            "FILTER ( ?loja = " + loja.getUri() +" )" +
                        "}";

        List<RotaDTO> rotas = ontology.executeQueryToType(queryString, RotaDTO.class);
        return rotas;
    }

    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginForm loginForm) {
        return new LoginDTO(
                "Jhonny",
                "ABCDEFG"
        );
    }

    private List<LojaDTO> filtraListaLojaComParametro(List<LojaDTO> lojas, String param) {
        return lojas
                .stream()
                .filter(e -> e.getNome().toUpperCase(Locale.ROOT).contains(param.toUpperCase(Locale.ROOT)) || e.getUri().toUpperCase(Locale.ROOT).contains((param.toUpperCase(Locale.ROOT))))
                .collect(Collectors.toList());
    }

    private List<ProdutoDTO> filtraListaProdutoComLoja(List<ProdutoDTO> produtos, String param) {
        return produtos
                .stream()
                .filter(e -> e.getLoja().toUpperCase(Locale.ROOT).contains(param.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    private List<ProdutoDTO> filtraPesquisaProdutoNome(List<ProdutoDTO> produtos, String pesquisa) {
        return produtos
                .stream()
                .filter(e ->
                        e.getNome().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT)) ||
                        e.getUri().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT)) ||
                        String.valueOf(e.getCodigo()).contains(pesquisa.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    private List<ProdutoDTO> filtraPesquisaProdutoAtividade(List<ProdutoDTO> produtos, String pesquisa) {
        return produtos
                .stream()
                .filter(e ->
                        e.getLojaAtividade().toUpperCase(Locale.ROOT).contains(pesquisa.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }
}

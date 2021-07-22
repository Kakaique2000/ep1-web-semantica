package br.usp.haroldo.epwebsemantica.controllers;

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
    RDFLoader rdfLoader;

    @GetMapping("/lojas")
    public List<LojaDTO> getLojas() throws IOException {
        String queryString =
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX parte_1: <http://www.web-semantica/ep/parte_1#>" +
                        "SELECT ?loja ?label ?atividade ?atividadeLabel " +
                        "WHERE{ " +
                        "{ ?loja a <http://www.web-semantica/ep/parte_1#LojaServicos> } UNION { ?loja a <http://www.web-semantica/ep/parte_1#LojaProdutos> } ." +
                        "?loja rdfs:label ?label ." +
                        "?loja parte_1:oferece ?atividade ." +
                        "?atividade rdfs:label ?atividadeLabel" +
                        "}";
        Query query = QueryFactory.create(queryString);

        Resource resource = rdfLoader.loadOWL("ontologia_centro.owl");

        Model model = ModelFactory.createDefaultModel();

        model.read(resource.getInputStream(), null);

        QueryExecution qe = QueryExecutionFactory.create(query, model);
        var lojas = new ArrayList<LojaDTO>();
        try {
            ResultSet res = qe.execSelect();
            while (res.hasNext()) {
                QuerySolution soln = res.next();
                RDFNode label = soln.get("?label");
                RDFNode uri = soln.get("?loja");
                RDFNode atividade = soln.get("?atividadeLabel");
                lojas.add(LojaDTO
                        .builder()
                        .uri(uri.toString())
                        .nome(label.asLiteral().getString())
                        .atividade(atividade.asLiteral().getString())
                        .build()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            qe.close();
        }

        return lojas;
    }
}

/*
Listar Loja por nome

String queryString = 
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
            "SELECT ?label "+
            "WHERE{ "+
            "?x rdfs:label ?label "+
            "FILTER (?x IN (rdf:type <http://www.web-semantica/ep/parte_1#LojaServicos/>,  rdf:type <http://www.web-semantica/ep/parte_1#LojaProdutos>))"; 
Query query = QueryFactory.create(queryString);
QueryExecution qe = QueryExecutionFactory.create(query, model);
try {
    ResultSet res = qe.execSelect();
    while( res.hasNext()) {
        QuerySolution soln = res.next();
        RDFNode label = soln.get("?label");
        System.out.println(label);
    }
} finally {
    qe.close();
}

*/

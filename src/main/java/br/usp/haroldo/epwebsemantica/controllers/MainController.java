package br.usp.haroldo.epwebsemantica.controllers;

import br.usp.haroldo.epwebsemantica.RDFLoader;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    RDFLoader rdfLoader;

    @GetMapping()
    @ModelAttribute("modeloRDF")
    public ModelAndView test() throws IOException {

        var ontologiaCentro = rdfLoader.loadOWL("ontologia_centro.owl");

        Model model = ModelFactory.createDefaultModel();
        model.read(ontologiaCentro.getInputStream(), null);

        Resource subject = model.createResource("www.uri.com/foad.rdf#klebin");
        subject.addProperty(FOAF.name, "Klebin");

        ModelAndView retorno = new ModelAndView("index");
        retorno.addObject("modeloRDF", model);

        return retorno;
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

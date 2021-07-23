package br.usp.haroldo.epwebsemantica.configuration;

import br.usp.haroldo.epwebsemantica.services.RDFLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OntologyConfig {

    @Autowired
    RDFLoader rdfLoader;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    Model model;

    @Bean
    public Model getOntologyModel() throws IOException {
        Resource resource = rdfLoader.loadOWL("ontologia_centro.owl");
        Model model = ModelFactory.createDefaultModel();
        model.read(resource.getInputStream(), null);
        return model;
    }

    public <T> List<T> executeQueryToType(String queryString, Class<T> klass) {
        List<Map<String, String>> maps = executeQueryToMap(queryString);
        return maps.stream().map(e -> mapper.convertValue(e, klass)).collect(Collectors.toList());
    }

    public List<Map<String, String>> executeQueryToMap (String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet res = qe.execSelect();

        List<String> resultVars = res.getResultVars();
        List<Map<String, String>> finalList = new ArrayList<>();

        while (res.hasNext()) {
            QuerySolution soln = res.next();
            LinkedHashMap<String, String> resultObject = new LinkedHashMap<>();
            resultVars.forEach(var -> {
                resultObject.put(var, extractLiteral(soln.get(var)));
            });
            finalList.add(resultObject);
        }

        return finalList;
    }

    private String extractLiteral(RDFNode node) {
        try {
            return node.asLiteral().getString();
        } catch (Exception e) {
            try {
                return node.toString();
            } catch (Exception ex) {
                return null;
            }
        }
    }

}

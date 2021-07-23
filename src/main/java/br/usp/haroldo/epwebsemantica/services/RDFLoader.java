package br.usp.haroldo.epwebsemantica.services;

import br.usp.haroldo.epwebsemantica.models.LojaDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RDFLoader {

    public Resource loadOWL(String resourceName) {
        return new ClassPathResource(resourceName);
    }

}

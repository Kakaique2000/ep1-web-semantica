package br.usp.haroldo.epwebsemantica.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class RDFLoader {

    public Resource loadOWL(String resourceName) {
        return new ClassPathResource(resourceName);
    }

}

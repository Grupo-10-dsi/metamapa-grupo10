package ar.edu.utn.frba.ddsi.dinamica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.objenesis.instantiator.sun.SunReflectionFactorySerializationInstantiator;

@SpringBootApplication
public class DinamicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DinamicaApplication.class, args);
    }

}

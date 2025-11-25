package ar.edu.utn.frba.ddsi.estatica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EstaticaApplication {
    private static Logger log = LoggerFactory.getLogger(EstaticaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EstaticaApplication.class, args);
    }

}

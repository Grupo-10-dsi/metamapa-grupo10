package ar.edu.utn.frba.ddsi.agregador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AgregadorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgregadorApplication.class, args);
    }

}

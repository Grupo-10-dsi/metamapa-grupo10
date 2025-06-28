package ar.edu.utn.frba.ddsi.dinamica.services;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public interface DetectorDeSpam {

    public final AlgoritmoSpam algoritmoSpam = new AlgoritmoSpam(
            LectorCorpus.leerCorpus("corpus.txt")
    );
    
    public static boolean esSpam(String texto) {
         return algoritmoSpam.esTextoSpam(texto);
    }
    

}


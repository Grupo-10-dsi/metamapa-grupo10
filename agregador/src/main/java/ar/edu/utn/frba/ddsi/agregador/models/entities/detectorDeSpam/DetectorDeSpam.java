package ar.edu.utn.frba.ddsi.agregador.models.entities.detectorDeSpam;

public interface DetectorDeSpam {

    public final AlgoritmoSpam algoritmoSpam = new AlgoritmoSpam(
            LectorCorpus.leerCorpus("corpus.txt")
    );
    
    public static boolean esSpam(String texto) {
         return algoritmoSpam.esTextoSpam(texto);
    }
    

}


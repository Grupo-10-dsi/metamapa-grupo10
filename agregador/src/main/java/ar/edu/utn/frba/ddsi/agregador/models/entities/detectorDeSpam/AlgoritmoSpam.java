package ar.edu.utn.frba.ddsi.agregador.models.entities.detectorDeSpam;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AlgoritmoSpam {

    private final List<String> corpus;
    private final List<Set<String>> corpusTokenizado;

    public AlgoritmoSpam(List<String> corpus) {
        this.corpus = corpus; // conjunto de textos que se usa para entrenar el modelo
        this.corpusTokenizado = corpus.stream()
                .map(this::separarEnTokens)
                .map(HashSet::new) // Elimino duplicados
                .collect(Collectors.toList());

    }

    public boolean esTextoSpam(String texto) {
        Map<String, Double> tfidf = calcularTFIDF(texto);

        double puntaje = tfidf.entrySet().stream()
                .mapToDouble(Map.Entry::getValue)
                .sum();

        //System.out.println(puntaje);
        return puntaje < 2.5;
    }

    private Map<String, Double> calcularTFIDF(String texto) {
        List<String> palabras = separarEnTokens(texto);
        Map<String, Long> tf = palabras.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        Map<String, Double> tfidf = new HashMap<>();
        int totalDocs = corpus.size();

        for (String palabra : tf.keySet()) {
            double frecuencia = (double) tf.get(palabra) / palabras.size();
            long docsConPalabra = corpusTokenizado.stream()
                    .filter(tokens -> tokens.contains(palabra))
                    .count();
            double idf = Math.log(1 + (double) totalDocs / (1 + docsConPalabra));
            tfidf.put(palabra, frecuencia * idf);

            if(esPalabraClaveValida(palabra)) {
                tfidf.put(palabra, tfidf.get(palabra) * 1.5); // Aumento el peso de las palabras clave
            }
        }

        return tfidf;
    }

    static final Set<String> stopWords = Set.of(
            "el", "la", "los", "las", "un", "una", "unos", "unas", "de", "del", "al",
            "por", "para", "con", "sin", "en", "sobre", "entre", "hacia", "desde", "hasta",
            "y", "o", "u", "e", "a", "que", "se", "su", "sus", "mi", "mis", "tu", "tus",
            "es", "son", "era", "eran", "ser", "fue", "fueron", "soy", "está", "están", "estaba", "estaban",
            "hay", "había", "hubo", "tener", "tiene", "tienen", "tenía", "tenían", "puede", "pueden", "puedo",
            "lo", "le", "les", "me", "te", "nos", "os", "ya", "aquí", "allí", "entonces", "así", "también", "pero", "aunque"
    );

    private List<String> separarEnTokens(String texto) {
        if (texto == null || texto.isEmpty()) {
            return new ArrayList<>();
        } else
            return Arrays.stream(normalizarTexto(texto).split("\\W+")) // Divido por espacios y signos de puntuación
                    .filter(token -> !token.isBlank())
                    .filter(token -> !stopWords.contains(token))  // Saco palabras como "en", "la", "de", etc.
                    .collect(Collectors.toList());
    }

    private String normalizarTexto(String texto) {
        String temp = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Saco acentos
                .replaceAll("[^\\p{L}\\p{Nd}]+", " ") // Saco letras y números
                .toLowerCase();
        return temp.trim();
    }

    private boolean esPalabraClaveValida(String palabra) {
        String palabraNormalizada = normalizarTexto(palabra);
        return regexPalabrasClave.stream().anyMatch(p -> p.matcher(palabraNormalizada).matches());
    }

    private final List<Pattern> regexPalabrasClave = List.of(
            Pattern.compile(".solicit."),
            Pattern.compile(".(elimin|remov|supres|cancel|borr)."),
            Pattern.compile(".fals."),
            Pattern.compile(".mentir."),
            Pattern.compile(".enga."),
            Pattern.compile(".difam."),
            Pattern.compile(".calumni."),
            Pattern.compile(".erron."),
            Pattern.compile(".inexact."),
            Pattern.compile(".equivoc."),
            Pattern.compile(".privac."),
            Pattern.compile(".(íntim|confidencial|datos|exposici|divulgaci|revelaci)."),
            Pattern.compile(".reputaci."),
            Pattern.compile(".honor."),
            Pattern.compile(".dignid."),
            Pattern.compile(".acoso."),
            Pattern.compile(".abus."),
            Pattern.compile(".denunci."),
            Pattern.compile(".amenaz."),
            Pattern.compile(".intimid."),
            Pattern.compile(".consec."),
            Pattern.compile(".legal."),
            Pattern.compile(".judici."),
            Pattern.compile(".sanci."),
            Pattern.compile(".responsab."),
            Pattern.compile(".emocion."),
            Pattern.compile(".psicol."),
                    Pattern.compile(".estr[eé]s."),
                    Pattern.compile(".ansiedad."),
                    Pattern.compile(".afectad."),
                    Pattern.compile(".perjudicad."),
                    Pattern.compile(".laboral."),
                    Pattern.compile(".profesional."),
                    Pattern.compile(".trabaj."),
                    Pattern.compile(".emple."),
                    Pattern.compile(".despid."),
                    Pattern.compile(".impact."),
                    Pattern.compile(".grave."),
                    Pattern.compile(".inacept."),
                    Pattern.compile(".violaci."),
                    Pattern.compile(".[eé]tic."),
                    Pattern.compile(".rechaz."),
                    Pattern.compile(".expos."),
                    Pattern.compile(".divulg."),
                    Pattern.compile(".revel."),
                    Pattern.compile(".consent."),
                    Pattern.compile(".autoriz."),
                    Pattern.compile(".difund."),
                    Pattern.compile(".perjuici."),
                    Pattern.compile(".desprestig."),
                    Pattern.compile(".desacredit."),
                    Pattern.compile(".hostig."),
                    Pattern.compile(".infracci."),
                    Pattern.compile(".grad."),
                    Pattern.compile(".catastrof."),
                    Pattern.compile(".incendi."),
                    Pattern.compile(".incin."),
                    Pattern.compile(".temporal."),
                    Pattern.compile(".tormenta."),
                    Pattern.compile(".inundaci."),
                    Pattern.compile(".hurac[aá]n."),
                    Pattern.compile(".tornado."),
                    Pattern.compile(".deslizamiento."),
                    Pattern.compile(".sismo."),
                    Pattern.compile(".terremoto."),
                    Pattern.compile(".explosi[oó]n."),
                    Pattern.compile(".accidente."),
                    Pattern.compile(".colisi[oó]n."),
                    Pattern.compile(".choque."),
                    Pattern.compile(".derrame."),
                    Pattern.compile(".cataclismo."),
                    Pattern.compile(".desastre."),
                    Pattern.compile(".desapar."),
                    Pattern.compile("consentimiento")
    );
}

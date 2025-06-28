package ar.edu.utn.frba.ddsi.dinamica.services;

import java.text.Normalizer;
import java.util.*;
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

        return puntaje > 0.15;
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
}

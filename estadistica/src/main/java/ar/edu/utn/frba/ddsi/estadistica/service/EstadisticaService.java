package ar.edu.utn.frba.ddsi.estadistica.service;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.AgregadorClient;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Ubicacion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {

    private final AgregadorClient agregadorClient = new AgregadorClient("http://localhost:8080/api/agregador/estadisticas");

    public String obtenerProvinciaDeColeccion(Integer Id) {
        List<Ubicacion> ubicacionesColeccion = agregadorClient.obtenerUbicacionesDeColeccion(Id);
        List<String> provincias = convertirAProvincias(ubicacionesColeccion);
        return provinciaMasFrecuente(provincias);
    }

    public Categoria obtenerCategoriaConMasHechos() {
        return this.agregadorClient.obtenerCategoriaConMasHechos();
    }

    public String obtenerProvinciaDeCategoria(Integer id) {
        List<Ubicacion> ubicacionesCategoria = agregadorClient.obtenerUbicacionesDeCategoria(id);
        List<String> provincias = convertirAProvincias(ubicacionesCategoria);
        return provinciaMasFrecuente(provincias);
    }

    public LocalTime obtenerHoraMasFrecuenteDeCategoria(Integer id) {
        return this.agregadorClient.obtenerHoraMasFrecuenteDeCategoria(id);
    }

    /* La request deberia tener esto en el body por ejemplo:
    {"ubicaciones": [
        {
          "lat": -32.8551545,
          "lon": -60.697636,
          "campos": "basico",
          "aplanar": true
        },
        {
          "lat": -34.650936759047156,
          "lon": -58.559044689307086,
          "campos": "basico",
          "aplanar": true
        }
      ]
    }
    *
    * */
    public List<String> convertirAProvincias(List<Ubicacion> ubicaciones) {
        // Usamos una API q convierte lat y long en provincia
        // TODO: probar

        WebClient webClient = WebClient.builder()
                .baseUrl("https://apis.datos.gob.ar/georef/api")
                .defaultHeader("User-Agent", "SpringGeocoder/1.0")
                .build();

        JSONObject body = new JSONObject();
        JSONArray ubicacionesArray = new JSONArray();


        for (Ubicacion ubicacion : ubicaciones) {
             JSONObject obj = new JSONObject();
             obj.put("lat", ubicacion.getLatitud());
             obj.put("lon", ubicacion.getLongitud());
             obj.put("campos", "basico");
             obj.put("aplanar", true);
             ubicacionesArray.put(obj);
        }
        body.put("ubicaciones", ubicacionesArray);

        String response = webClient.post()
                .uri("/ubicacion")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray resultados = jsonResponse.optJSONArray("resultados");

        List<String> provincias = new ArrayList<>();

        for (int i = 0; i < resultados.length(); i++) {
            JSONObject resultado = resultados.getJSONObject(i);
            JSONObject ubicacion = resultado.getJSONObject("ubicacion");

            // Extraemos directamente el campo "provincia_nombre"
            String provincia = ubicacion.optString("provincia_nombre", "Provincia no encontrada");
            provincias.add(provincia);
        }
        return provincias;
    }

    public Integer obtenerCantidadDeSolicitudesSpam() {
        return this.agregadorClient.obtenerCantidadDeSolicitudesSpam();
    }

    public String provinciaMasFrecuente(List<String> provincias) {
        return provincias.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();
    }

}

package ar.edu.utn.frba.ddsi.estadistica.service;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {

    private final AgregadorClient agregadorClient = new AgregadorClient("http://localhost:8080/agregador");

    public Map<Integer, String> obtenerProvinciaDeColeccion(Integer Id) {
        Map<Integer, String> respuesta = new HashMap<>();
        if(Id == null) {
            List<Coleccion> colecciones = agregadorClient.obtenerTodasLasColecciones();
            for (Coleccion coleccion : colecciones) {
                List<Ubicacion> ubicacionesColeccion = agregadorClient.obtenerUbicacionesDeColeccion(coleccion.getId());
                List<String> provincias = convertirAProvincias(ubicacionesColeccion);
                String provincia = provinciaMasFrecuente(provincias);
                respuesta.put(coleccion.getId(), provincia);
            }
            return respuesta;
        }
        List<Ubicacion> ubicacionesColeccion = agregadorClient.obtenerUbicacionesDeColeccion(Id);
        List<String> provincias = convertirAProvincias(ubicacionesColeccion);
        String provincia = provinciaMasFrecuente(provincias);
        return Map.of(Id, provincia);
    }

//    public String obtenerProvinciaPorColeccion() {
//        List<Coleccion> colecciones = agregadorClient.obtenerTodasLasColecciones();
//
//    }

    public Categoria obtenerCategoriaConMasHechos() {
        return this.agregadorClient.obtenerCategoriaConMasHechos();
    }

    public Map<Integer, String> obtenerProvinciaDeCategoria(Integer Id) {
        Map<Integer, String> respuesta = new HashMap<>();
        if(Id == null){
            List<Categoria> categorias = agregadorClient.obtenerTodasLasCategorias();
            for(Categoria categoria : categorias) {
                List<Ubicacion> ubicacionesCategoria = agregadorClient.obtenerUbicacionesDeCategoria(categoria.getId());
                List<String> provincias = convertirAProvincias(ubicacionesCategoria);
                String provincia = provinciaMasFrecuente(provincias);
                respuesta.put(categoria.getId(), provincia);
            }
        }
        else {
            List<Ubicacion> ubicacionesCategoria = agregadorClient.obtenerUbicacionesDeCategoria(Id);
            List<String> provincias = convertirAProvincias(ubicacionesCategoria);
            String provincia = provinciaMasFrecuente(provincias);
            respuesta.put(Id, provincia);
        }

        return respuesta;
    }

    public Map <Integer, LocalTime> obtenerHoraMasFrecuenteDeCategoria(Integer id) {
        Map<Integer, LocalTime> respuesta = new HashMap<>();
        if (id == null){
            List<Categoria> categorias = agregadorClient.obtenerTodasLasCategorias();
            for(Categoria categoria : categorias){
                LocalTime hora = this.agregadorClient.obtenerHoraMasFrecuenteDeCategoria(categoria.getId());
                respuesta.put(categoria.getId(), hora);
            }
        }
        else{
            LocalTime hora = this.agregadorClient.obtenerHoraMasFrecuenteDeCategoria(id);
            respuesta.put(id, hora);
        }
        return respuesta;
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

    public List<SolicitudDTO> obtenerCantidadDeSolicitudesSpam() {

        return this.agregadorClient.obtenerSolicitudesSpam();
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

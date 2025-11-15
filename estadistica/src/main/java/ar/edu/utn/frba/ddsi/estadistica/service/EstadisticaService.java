package ar.edu.utn.frba.ddsi.estadistica.service;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.*;
import ar.edu.utn.frba.ddsi.estadistica.models.repositories.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EstadisticaService {


    private final AgregadorClient agregadorClient;
    private final ProvinciaRepository provinciaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProvinciaColeccionRepository provinciaColeccionRepository;
    private final HoraFrecuenteRepository horaFrecuenteRepository;
    private final SolicitudCantidadRepository solicitudCantidadRepository;

    public EstadisticaService(
            ProvinciaRepository provinciaRepository,
            CategoriaRepository categoriaRepository,
            ProvinciaColeccionRepository provinciaColeccionRepository,
            HoraFrecuenteRepository horaFrecuenteRepository,
            SolicitudCantidadRepository solicitudCantidadRepository,
            @Value("${agregador.url}") String agregadorUrl
    ) {
        this.provinciaRepository = provinciaRepository;
        this.categoriaRepository = categoriaRepository;
        this.provinciaColeccionRepository = provinciaColeccionRepository;
        this.horaFrecuenteRepository = horaFrecuenteRepository;
        this.solicitudCantidadRepository = solicitudCantidadRepository;
        this.agregadorClient = new AgregadorClient(agregadorUrl + "/agregador");
    }

    public List<String> obtenerProvinciaDeColeccion(Integer Id, Integer cantidadProvincias) {
        if(cantidadProvincias == null) {
            cantidadProvincias = 1;
        }
        List<Ubicacion> ubicacionesCategoria = agregadorClient.obtenerUbicacionesDeColeccion(Id);
        List<String> provincias = convertirAProvincias(ubicacionesCategoria);
        List<String> prov_frecuentes = provinciasMasFrecuente(provincias, cantidadProvincias);

        prov_frecuentes.forEach(provincia -> {;
            ProvinciaColeccion nuevaProvincia = new ProvinciaColeccion(provincia, Id);
            provinciaColeccionRepository.save(nuevaProvincia);
        });
        return prov_frecuentes;
    }

//    public String obtenerProvinciaPorColeccion() {
//        List<Coleccion> colecciones = agregadorClient.obtenerTodasLasColecciones();
//
//    }

    public List<Categoria> obtenerCategoriasConMasHechos(Integer cantidadCategorias) {
        if (cantidadCategorias == null) {
            cantidadCategorias = 1;
        }
        List<Categoria> categorias =  this.agregadorClient.obtenerCategoriaConMasHechos(cantidadCategorias);
        categorias.forEach(categoria -> {
            Categoria nuevaCategoria = new Categoria(categoria.getDetalle());
            categoriaRepository.save(nuevaCategoria);
        });
        return categorias;
    }

    public List<String> obtenerProvinciasDeCategoria(Integer Id, Integer cantidadProvincias) {
        if(cantidadProvincias == null) {
            cantidadProvincias = 1;
        }
        List<Ubicacion> ubicacionesCategoria = agregadorClient.obtenerUbicacionesDeCategoria(Id);
        List<String> provincias = convertirAProvincias(ubicacionesCategoria);
        List<String> prov_frecuentes = provinciasMasFrecuente(provincias, cantidadProvincias);

        prov_frecuentes.forEach(provincia -> {;
            Provincia nuevaProvincia = new Provincia(provincia, Id);
            provinciaRepository.save(nuevaProvincia);
        });
        return prov_frecuentes;

    }

    public List<LocalTime> obtenerHorasMasFrecuentesDeCategoria(Integer Id, Integer cantidadHoras) {
        if(cantidadHoras == null) {
            cantidadHoras = 1;
        }
        List<LocalTime> horas = this.agregadorClient.obtenerHorasMasFrecuentesDeCategoria(Id, cantidadHoras);
        horas.forEach(hora -> {
            Hora_Frecuente nuevaHora = new Hora_Frecuente(hora, Id);
            horaFrecuenteRepository.save(nuevaHora);
        });

        return horas;
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

        List<SolicitudDTO> solicitudes = this.agregadorClient.obtenerSolicitudesSpam();


        Solicitud_Cantidad nuevaSolicitud = new Solicitud_Cantidad(solicitudes.size());
        solicitudCantidadRepository.save(nuevaSolicitud);


        return solicitudes;
    }

    public List<String> provinciasMasFrecuente(List<String> provincias, Integer top) {
        if (provincias == null || provincias.isEmpty()) {
            return new ArrayList<>();
        }
        int limit = (top == null || top <= 0) ? 1 : Math.min(top, provincias.size());

        return provincias.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    int cmp = Long.compare(e2.getValue(), e1.getValue()); // frecuencia descendente
                    if (cmp != 0) return cmp;
                    return e1.getKey().compareTo(e2.getKey()); // desempate lexicográfico
                })
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
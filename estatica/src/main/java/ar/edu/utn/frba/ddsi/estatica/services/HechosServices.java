package ar.edu.utn.frba.ddsi.estatica.services;
import ar.edu.utn.frba.ddsi.estatica.models.entities.ArchivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.ArchivoProcesadoDTO;
import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.estatica.models.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechosServices {
    private final HechosRepository hechosRepository;

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;

    }

    public List<ArchivoProcesadoDTO> obtenerHechos(List<String> nombresArchivos) {

        this.hechosRepository.importarHechosSin(nombresArchivos);

        List<ArchivoProcesado> archivosProcesados = this.hechosRepository.findAllArchivosProcesados();

        return archivosProcesados.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ArchivoProcesadoDTO toDTO(ArchivoProcesado archivo) {
        return new ArchivoProcesadoDTO(archivo.getNombre(), LocalDateTime.now(), archivo.getHechos().stream().map(this::toHechoDTO).toList());
    }

    private HechoDTO toHechoDTO(Hecho hecho) {
        return new HechoDTO(hecho.getId(), hecho.getDescripcion(), hecho.getFechaAcontecimiento(), hecho.getCategoria());
    }

    // Por si se necesitara en un futuro la fecha de procesamiento
    private ArchivoProcesado fromDTO(ArchivoProcesadoDTO dto) {
        return new ArchivoProcesado(dto.getNombre(), dto.getUltimaConsulta(), List.of());
    }
}

package co.edu.uniandes.dse.vecindarioamigo.dto;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class NegocioDetailDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer numeroDeTelefonico;
    private Float Calificacion;
    private VecindarioDTO vecindario;
    private List<ComentarioDTO> comentarios = new ArrayList<>();
    private List<OfertaDTO> ofertas = new ArrayList<>();
}

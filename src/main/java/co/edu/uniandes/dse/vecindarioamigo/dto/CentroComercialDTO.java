package co.edu.uniandes.dse.vecindarioamigo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentroComercialDTO {
    private Long id;
    private String descripcion;
    private String ubicacion;
    private float calificacion;
    private VecindarioDTO vecindario;
}

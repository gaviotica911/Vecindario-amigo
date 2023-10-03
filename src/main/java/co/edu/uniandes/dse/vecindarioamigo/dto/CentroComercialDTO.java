package co.edu.uniandes.dse.vecindarioamigo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CentroComercialDTO {
    private String nombre;
    private Long id;
    private String descripcion;
    private String ubicacion;
    private float calificacion;
    private VecindarioDTO vecindario;
}

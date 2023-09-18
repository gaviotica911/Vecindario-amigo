package co.edu.uniandes.dse.vecindarioamigo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VecinoDTO {
    private Long id;
    private String nombre;
    private String edad;
    private String profilePic;
    private String descripcion;
    private VecindarioDTO vecindario;

}

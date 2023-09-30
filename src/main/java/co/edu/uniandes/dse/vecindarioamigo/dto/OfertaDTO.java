package co.edu.uniandes.dse.vecindarioamigo.dto;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfertaDTO {
    private Long id;
    private String descripcion;
    private NegocioEntity negocio;
}

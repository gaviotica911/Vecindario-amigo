package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CentroComercialDetailDTO extends CentroComercialDTO {
    private List<NegocioDTO> negocios = new ArrayList<>();
    private List<ComentarioDTO> comentarios = new ArrayList<>();
}

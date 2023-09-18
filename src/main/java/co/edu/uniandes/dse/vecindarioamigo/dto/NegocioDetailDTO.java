package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class NegocioDetailDTO {
    private List<ComentarioDTO> comentarios = new ArrayList<>();
    private List<OfertaDTO> ofertas = new ArrayList<>();

}

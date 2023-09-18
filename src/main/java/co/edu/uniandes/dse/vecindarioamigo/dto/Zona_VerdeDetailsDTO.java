package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Zona_VerdeDetailsDTO {

    private List<ComentarioDTO> comentarios = new ArrayList<>();

}

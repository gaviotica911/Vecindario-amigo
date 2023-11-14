package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Zona_VerdeDetailsDTO extends Zona_VerdeDTO{

    private List<ComentarioDTO> comentarios = new ArrayList<>();

}

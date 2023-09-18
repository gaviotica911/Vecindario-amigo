package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VecinoDetailDTO extends VecinoDTO
{
    private List<GrupoDeInteresDTO> gruposDeInteres = new ArrayList<>();
    private List<PublicacionDTO> publicaciones = new ArrayList<>();

    
}

package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GrupoDeInteresDetailDTO extends GrupoDeInteresDTO{
    private List<VecinoDTO> vecinos = new ArrayList<>();
}

package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VecindarioDetailDTO extends VecindarioDTO{
    private List<Zona_VerdeDTO> zonasVerdes = new ArrayList<>();
    private List<VecinoDTO> vecinos = new ArrayList<>();
    private List<CentroComercialDTO> centrosComerciales = new ArrayList<>();
    private List<NegocioDTO> negocios = new ArrayList<>();
}

package co.edu.uniandes.dse.vecindarioamigo.dto;

import lombok.Data;

@Data
public class ComentarioDetailDTO {
    private PublicacionDTO publicacion;
    private Zona_VerdeDTO zonaVerde;
    private NegocioDTO negocio;
    private CentroComercialDTO centroComercial;
}

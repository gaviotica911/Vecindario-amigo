package co.edu.uniandes.dse.vecindarioamigo.dto;

import lombok.Data;

@Data
public class NegocioDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer numeroDeTelefonico;
    private Float Calificacion;
}

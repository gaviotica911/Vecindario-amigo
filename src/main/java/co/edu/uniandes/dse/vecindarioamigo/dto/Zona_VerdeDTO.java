package co.edu.uniandes.dse.vecindarioamigo.dto;

import lombok.Data;

@Data
public class Zona_VerdeDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String preferencias;
    private Float calificacion;
    private String ubicacion;
}

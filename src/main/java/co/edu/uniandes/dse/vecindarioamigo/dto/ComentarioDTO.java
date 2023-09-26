package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.sql.Date;

import lombok.Data;


@Data
public class ComentarioDTO {
    private Long id;
    private String nombre;
    private Date fecha;
    private String descripcion;


}

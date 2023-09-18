package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class PublicacionDTO {

    private Long id;
    private String contenido;
    private String foto;
    private String video;
    private int likes;
    private int compartidos;
    private Date fecha;
    private VecinoDTO vecino;


}

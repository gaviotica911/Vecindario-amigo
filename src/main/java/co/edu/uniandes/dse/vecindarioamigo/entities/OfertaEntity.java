package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


import lombok.Data;

@Data
@Entity
public class OfertaEntity {
    private int ID;
    private String descripcion;

    @ManyToOne
    private NegocioEntity negocio;

}

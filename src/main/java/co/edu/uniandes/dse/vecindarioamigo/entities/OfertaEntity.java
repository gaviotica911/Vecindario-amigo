package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


import lombok.Data;

@Data
@Entity
public class OfertaEntity extends BaseEntity{
    private String descripcion;

    @ManyToOne
    private NegocioEntity negocio;

}

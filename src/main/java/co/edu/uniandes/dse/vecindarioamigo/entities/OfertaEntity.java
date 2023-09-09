package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class OfertaEntity extends BaseEntity{
    private String descripcion;

    @PodamExclude
    @ManyToOne
    private NegocioEntity negocio;

}

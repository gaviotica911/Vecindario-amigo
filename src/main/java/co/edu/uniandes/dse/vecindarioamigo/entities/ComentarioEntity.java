package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Date;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ComentarioEntity extends BaseEntity{
    private String nombre;
    private Date descripcion;

    @PodamExclude
    @ManyToOne
    private NegocioEntity negocio;

    @PodamExclude
    @ManyToOne
    private CentroComercialEntity centroComercial;

    @PodamExclude
    @ManyToOne
    private Zona_VerdeEntity zonaVerde;

    @PodamExclude
    @ManyToOne
    private PublicacionEntity publicacion;
}

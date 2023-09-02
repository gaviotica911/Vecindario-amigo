package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;

import java.util.List;
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

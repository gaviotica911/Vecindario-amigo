package co.edu.uniandes.dse.vecindarioamigo.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class CentroComercialEntity extends BaseEntity{
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private float calificacion;

    @PodamExclude
    @OneToMany(mappedBy = "centroComercial", fetch = FetchType.LAZY)
    private List<NegocioEntity> lista_negocios= new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "centroComercial", fetch = FetchType.LAZY)
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private VecindarioEntity vecindario;
}

package co.edu.uniandes.dse.vecindarioamigo.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;

import lombok.Data;

@Data
@Entity
public class CentroComercialEntity extends BaseEntity{
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private float calificacion;

    @OneToMany(mappedBy = "centroComercial", fetch = FetchType.LAZY)
    private List<NegocioEntity> lista_negocios= new ArrayList<>();

    @OneToMany(mappedBy = "centroComercial", fetch = FetchType.LAZY)
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @ManyToOne
    private VecindarioEntity vecindario;
}

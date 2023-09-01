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
public class CentroComercialEntity {
    private int ID;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private float calificacion;

    @OneToMany(mappedBy = "lista_negocios", fetch = FetchType.LAZY)
    private List<NegocioEntity>  centroComercial= new ArrayList<>();

    @OneToMany(mappedBy = "comentarios", fetch = FetchType.LAZY)
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @ManyToOne
    private VecindarioEntity vecindario;
}

package co.edu.uniandes.dse.vecindarioamigo.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;

import lombok.Data;

@Data
@Entity
public class VecinoEntity extends BaseEntity {
    private String nombre;
    private int edad;
    private String porfile_pic;
    private String descripcion;

    @ManyToMany(mappedBy = "vecinos", fetch = FetchType.LAZY)
    private List<GruposDeInteresEntity> GruposDeInteres = new ArrayList<>();

    @OneToMany(mappedBy = "vecino", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicacionEntity> publicaciones = new ArrayList<>();

    @ManyToOne
    private VecindarioEntity vecindario;

}

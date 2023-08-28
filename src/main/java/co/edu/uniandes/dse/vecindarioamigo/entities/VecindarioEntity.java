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
public class VecindarioEntity {
    private int id;
    private String nombre;
    private String ciudad;
    private String localidad;
    private String vecindarios_cercanos;

    @OneToMany(mappedBy = "habitantes", fetch = FetchType.LAZY)
    private List<VecinoEntity> habitantes = new ArrayList<>();

    @OneToMany(mappedBy = "negocios", fetch = FetchType.LAZY)
    private List<NegocioEntity> negocios = new ArrayList<>();

    @OneToMany(mappedBy = "ccs", fetch = FetchType.LAZY)
    private List<CentroComercialEntity> ccs = new ArrayList<>();

}

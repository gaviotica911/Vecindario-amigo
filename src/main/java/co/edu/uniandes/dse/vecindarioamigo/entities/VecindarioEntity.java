package co.edu.uniandes.dse.vecindarioamigo.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class VecindarioEntity extends BaseEntity{
    private String nombre;
    private String ciudad;
    private String localidad;

    @PodamExclude
    @OneToMany(mappedBy = "vecindario", fetch = FetchType.LAZY)
    private List<VecinoEntity> vecinos = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "Vecindario", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<NegocioEntity> negocios = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "vecindario", fetch = FetchType.LAZY,
               cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CentroComercialEntity> centrosComerciales = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "vecindario", fetch = FetchType.LAZY,
               cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Zona_VerdeEntity> zonasVerdes = new ArrayList<>();



}

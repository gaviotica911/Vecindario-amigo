package co.edu.uniandes.dse.vecindarioamigo.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;

import lombok.Data;

@Data
@Entity
public class VecindarioEntity {
    private int ID;
    private String nombre;
    private String ciudad;
    private String localidad;

    @OneToMany(mappedBy = "habitantes", fetch = FetchType.LAZY)
    private List<VecinoEntity> habitantes = new ArrayList<>();

    @OneToMany(mappedBy = "negocios", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<NegocioEntity> negocios = new ArrayList<>();

    @OneToMany(mappedBy = "centrosComerciales", fetch = FetchType.LAZY,
               cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CentroComercialEntity> centrosComerciales = new ArrayList<>();

    @OneToMany(mappedBy = "vecindariosCercanos", fetch = FetchType.LAZY,
               cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<VecindarioEntity> vecindariosCercanos = new ArrayList<>();

    @OneToMany(mappedBy = "zonasVerdes", fetch = FetchType.LAZY,
               cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Zona_VerdeEntity> zonasVerdes = new ArrayList<>();



}

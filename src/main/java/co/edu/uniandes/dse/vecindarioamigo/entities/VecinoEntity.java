package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;


import antlr.collections.List;
import lombok.Data;


@Data
@Entity
public class VecinoEntity extends BaseEntity {

    private int ID;
    private String nombre;
    private int edad;
    private String porfile_pic;
    private String descripcion;

    @OneToMany(mappedBy="perteneceA", fetch= FetchType.LAZY)
    private List<GruposDeInteresEntity> perteneceA= new ArrayList<>();

    @OneToMany(mappedBy="publicaciones", fetch= FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PublicacionEntity>  publicaicones=new ArrayList<>();

    @ManyToOne
    private VecindarioEntity vecindario;







}


    
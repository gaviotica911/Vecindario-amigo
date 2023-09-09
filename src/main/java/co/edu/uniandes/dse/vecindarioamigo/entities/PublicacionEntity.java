package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

import java.sql.Date;
import java.util.ArrayList;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;


@Data
@Entity

public class PublicacionEntity extends BaseEntity{
    private String contenido;
    private Date fecha ;
    private String foto;
    private String video;
    private int likes;
    private int compartidos;

    @PodamExclude
    @ManyToOne
    private VecinoEntity vecino;

    @PodamExclude
    @OneToMany(mappedBy="publicacion", fetch= FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComentarioEntity> comentarios= new ArrayList<>();
 
}

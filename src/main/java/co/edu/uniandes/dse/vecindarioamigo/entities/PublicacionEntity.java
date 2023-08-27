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


@Data
@Entity

public class PublicacionEntity {
    private int ID;
    private String contenido;
    private Date fecha ;
    private String foto;
    private String video;
    private int likes;
    private int compartidos;

    @ManyToOne
    private VecinoEntity autor;

    @OneToMany(mappedBy="comentarios", fetch= FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ComentarioEntity> comentarios= new ArrayList<>();




    
}

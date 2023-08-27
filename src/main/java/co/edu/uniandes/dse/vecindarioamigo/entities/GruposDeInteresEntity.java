package co.edu.uniandes.dse.vecindarioamigo.entities;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Date;
import java.util.ArrayList;

import uk.co.jemos.podam.common.PodamExclude;
import antlr.collections.List;
import lombok.Data;



@Data
@Entity
public class GruposDeInteresEntity {
    private int ID;
    private String autor;
    private Date fecha;
    private String contenido;

    @PodamExclude
    @ManyToOne
    private VecinoEntity editorial;


    
}

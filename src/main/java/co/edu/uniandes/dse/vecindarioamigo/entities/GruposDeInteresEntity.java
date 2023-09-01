package co.edu.uniandes.dse.vecindarioamigo.entities;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.*;
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
    private String nombre;
    private String descripcion;

    @PodamExclude
    @ManyToMany
    private ArrayList<VecinoEntity> miembros = new ArrayList<>();


    
}

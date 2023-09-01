package co.edu.uniandes.dse.vecindarioamigo.entities;
import javax.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import uk.co.jemos.podam.common.PodamExclude;
import antlr.collections.List;
import lombok.Data;



@Data
@Entity
public class GruposDeInteresEntity extends BaseEntity{
    private String nombre;
    private String descripcion;

    @PodamExclude
    @ManyToMany
    private ArrayList<VecinoEntity> miembros = new ArrayList<>();


    
}

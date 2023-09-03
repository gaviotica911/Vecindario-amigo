package co.edu.uniandes.dse.vecindarioamigo.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;



@Data
@Entity
public class GruposDeInteresEntity extends BaseEntity{
    private String nombre;
    private String descripcion;

    @PodamExclude
    @ManyToMany
    private List<VecinoEntity> vecinos = new ArrayList<>();


    
}

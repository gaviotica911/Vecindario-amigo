package co.edu.uniandes.dse.vecindarioamigo.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Zona_VerdeEntity extends BaseEntity {
	private String nombre;
	private String descripcion;
	private List<String> preferencias;
	private Float calificacion;

	@ManyToOne
	private VecindarioEntity vecindario;

	@OneToMany(mappedBy = "zonaVerde", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ComentarioEntity> reviews = new ArrayList<>();
	

}

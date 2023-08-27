package co.edu.uniandes.dse.vecindarioamigo.entities;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class Zona_VerdeEntity extends BaseEntity {
	private Integer ID;
	private String nombre;
	private String descripcion;
	private List<String> preferencias;
	private Float calificacion;
	
	
	@ManyToOne
	private VecindarioEntity vecindario;

	@OneToMany(mappedBy = "comentarios", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ComentatiosEntity> reviews = new ArrayList<>();
}

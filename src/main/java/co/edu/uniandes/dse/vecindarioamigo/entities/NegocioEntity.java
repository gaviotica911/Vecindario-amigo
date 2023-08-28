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
public class NegocioEntity extends BaseEntity {

	private Integer ID;
	private String nombre;
	private String descripcion;
	private Integer numeroDeTelefonico;
	private Float Calificacion;

	@OneToMany(mappedBy = "ofertas", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<OfertaEntity> ofertas = new ArrayList<>();

	@OneToMany(mappedBy = "comentarios", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ComentarioEntity> comentarios = new ArrayList<>();

	@ManyToOne
	private CentroComercialEntity CentroComercial;

	@ManyToOne
	private VecindarioEntity Vecindario;
}

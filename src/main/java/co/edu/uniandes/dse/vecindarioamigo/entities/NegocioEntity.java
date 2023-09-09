package co.edu.uniandes.dse.vecindarioamigo.entities;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class NegocioEntity extends BaseEntity {

	private String nombre;
	private String descripcion;
	private Integer numeroDeTelefonico;
	private Float Calificacion;

	@PodamExclude
	@OneToMany(mappedBy = "negocio", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<OfertaEntity> ofertas = new ArrayList<>();

	@PodamExclude
	@OneToMany(mappedBy = "negocio", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ComentarioEntity> comentarios = new ArrayList<>();

	@PodamExclude
	@ManyToOne
	private CentroComercialEntity centroComercial;

	@PodamExclude
	@ManyToOne
	private VecindarioEntity Vecindario;
	
	
}

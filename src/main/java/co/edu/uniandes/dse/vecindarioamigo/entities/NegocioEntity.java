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
public class NegocioEntity extends BaseEntity {

	private Integer ID;

	private String Nombre;
	private String Descripcion;
	private Integer NumeroDeTelefonico;
	private Float Calificacion;

	@OneToMany(mappedBy = "negocio", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<OfertaEntity> ofertas = new ArrayList<>();

	@OneToMany(mappedBy = "negocio", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ComentarioEntity> comentarios = new ArrayList<>();

	@ManyToOne
	private CentroComercialEntity centroComercial;

	@ManyToOne
	private VecindarioEntity Vecindario;
	
	
}

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
	private List<ComentarioEntity> reviews = new ArrayList<>();
	
	//getters y setters
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<String> getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(List<String> preferencias) {
		this.preferencias = preferencias;
	}

	public Float getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Float calificacion) {
		this.calificacion = calificacion;
	}

	public VecindarioEntity getVecindario() {
		return vecindario;
	}

	public void setVecindario(VecindarioEntity vecindario) {
		this.vecindario = vecindario;
	}

	public List<ComentarioEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<ComentarioEntity> reviews) {
		this.reviews = reviews;
	}

}

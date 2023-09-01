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

	private String nombre;
	private String descripcion;
	private Integer numeroDeTelefonico;
	private Float Calificacion;

	@OneToMany(mappedBy = "negocio", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<OfertaEntity> ofertas = new ArrayList<>();

	@OneToMany(mappedBy = "negocio", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<ComentarioEntity> comentarios = new ArrayList<>();

	@ManyToOne
	private CentroComercialEntity centroComercial;

	@ManyToOne
	private VecindarioEntity Vecindario;
	
	// getters y setters
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

	public Integer getNumeroDeTelefonico() {
		return numeroDeTelefonico;
	}

	public void setNumeroDeTelefonico(Integer numeroDeTelefonico) {
		this.numeroDeTelefonico = numeroDeTelefonico;
	}

	public Float getCalificacion() {
		return Calificacion;
	}

	public void setCalificacion(Float calificacion) {
		Calificacion = calificacion;
	}

	public List<OfertaEntity> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<OfertaEntity> ofertas) {
		this.ofertas = ofertas;
	}

	public List<ComentarioEntity> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<ComentarioEntity> comentarios) {
		this.comentarios = comentarios;
	}

	public CentroComercialEntity getCentroComercial() {
		return CentroComercial;
	}

	public void setCentroComercial(CentroComercialEntity centroComercial) {
		CentroComercial = centroComercial;
	}

	public VecindarioEntity getVecindario() {
		return Vecindario;
	}

	public void setVecindario(VecindarioEntity vecindario) {
		Vecindario = vecindario;
	}
}

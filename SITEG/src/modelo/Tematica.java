package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tematica")
public class Tematica {

	@Id
	@GeneratedValue
	@Column(name="id", nullable = false)
	private long id;
	
	@Column(name="nombre", length = 100)
	private String nombre;
	
	@Column(name="descripcion", length = 500)
	private String descripcion;
	
	@Column(name="estatus")
	private Boolean estatus;
	
	@ManyToMany(mappedBy="tematicas")
	private Set<Profesor> profesores;
	
	@OneToMany(mappedBy="tematica")
	private Set<Teg> teg;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="area_investigacion_id", referencedColumnName="id")
	private AreaInvestigacion areaInvestigacion;

	@OneToMany(mappedBy="tematica")
	private Set<SolicitudTutoria> solicitudesTutoria;
	
	public Tematica() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tematica(long id, String nombre, String descripcion, Boolean estatus,
			AreaInvestigacion areaInvestigacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.areaInvestigacion = areaInvestigacion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Set<Teg> getTeg() {
		return teg;
	}

	public void setTeg(Set<Teg> teg) {
		this.teg = teg;
	}

	public AreaInvestigacion getareaInvestigacion() {
		return areaInvestigacion;
	}

	public void setareaInvestigacion(AreaInvestigacion areaInvestigacion) {
		this.areaInvestigacion = areaInvestigacion;
	}

	public Set<SolicitudTutoria> getSolicitudesTutoria() {
		return solicitudesTutoria;
	}

	public void setSolicitudesTutoria(Set<SolicitudTutoria> solicitudesTutoria) {
		this.solicitudesTutoria = solicitudesTutoria;
	}

	
	public Set<Profesor> getProfesores() {
		return profesores;
	}

	public void setProfesores(Set<Profesor> profesores) {
		this.profesores = profesores;
	}

	
	
	
}


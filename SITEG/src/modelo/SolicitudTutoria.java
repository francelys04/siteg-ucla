package modelo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "solicitud_tutoria")
public class SolicitudTutoria {

	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(name="fecha")
	private Date fecha;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="estado")
	private Boolean estado;
	
	@ManyToOne
	@JoinColumn(name="profesor_cedula", referencedColumnName="cedula")
	private Profesor profesor;
	
	@ManyToOne
	@JoinColumn(name="tematica_id", referencedColumnName="id")
	private Tematica tematica;
	
	@ManyToMany
	@JoinTable(name = "solicitud_tutoria_estudiante", joinColumns = { @JoinColumn(name = "solicitud_id") }, inverseJoinColumns = { @JoinColumn(name = "estudiante_cedula") })
	private Set<Estudiante> estudiantes;

	public SolicitudTutoria() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SolicitudTutoria(long id, Date fecha, String descripcion,
			Boolean estado, Profesor profesor, Tematica tematica, Set<Estudiante> estudiantes) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.estado = estado;
		this.profesor = profesor;
		this.tematica = tematica;
		this.estudiantes = estudiantes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public Tematica getTematica() {
		return tematica;
	}

	public void setTematica(Tematica tematica) {
		this.tematica = tematica;
	}

	public Set<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(Set<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}	
}

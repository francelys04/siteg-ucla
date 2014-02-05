package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.Table;

import modelo.compuesta.CondicionPrograma;
import modelo.compuesta.Cronograma;
import modelo.compuesta.ProgramaArea;
import modelo.compuesta.ProgramaItem;
import modelo.compuesta.ProgramaRequisito;

@Entity
@Table(name = "programa")
public class Programa {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "nombre", length = 100)
	private String nombre;

	@Column(name = "descripcion", length = 500)
	private String descripcion;

	@Column(name = "correo", length = 100)
	private String correo;

	@Column(name = "estatus")
	private Boolean estatus;

	@OneToMany(mappedBy = "programa")
	private Set<Estudiante> estudiantes;

	@OneToOne
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor directorPrograma;

	@OneToMany(mappedBy = "programa")
	private Set<CondicionPrograma> condicionesProgramas;

	@OneToMany(mappedBy = "programa")
	private Set<Cronograma> cronogramas;

	@OneToMany(mappedBy = "programa")
	private Set<ProgramaArea> programasAreas;

	@OneToMany(mappedBy = "programa")
	private Set<ProgramaRequisito> programaRequisitos;

	@OneToMany(mappedBy = "programa")
	private Set<ProgramaItem> programasItems;
	
	@OneToMany(mappedBy = "programa")
	private Set<Archivo> archivos;

	public Programa(long id, String nombre, String descripcion, String correo,
			Boolean estatus, Profesor directorPrograma) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.correo = correo;
		this.estatus = estatus;
		this.directorPrograma = directorPrograma;
	}

	public Programa() {
		super();
		// TODO Auto-generated constructor stub
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
	
	
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Set<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(Set<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public Set<CondicionPrograma> getCondicionesProgramas() {
		return condicionesProgramas;
	}

	public void setCondicionesProgramas(
			Set<CondicionPrograma> condicionesProgramas) {
		this.condicionesProgramas = condicionesProgramas;
	}

	public Set<Cronograma> getCronogramas() {
		return cronogramas;
	}

	public void setCronogramas(Set<Cronograma> cronogramas) {
		this.cronogramas = cronogramas;
	}

	public Set<ProgramaRequisito> getProgramaRequisitos() {
		return programaRequisitos;
	}

	public void setProgramaRequisitos(Set<ProgramaRequisito> programaRequisitos) {
		this.programaRequisitos = programaRequisitos;
	}

	public Set<ProgramaArea> getProgramasAreas() {
		return programasAreas;
	}

	public void setProgramasAreas(Set<ProgramaArea> programasAreas) {
		this.programasAreas = programasAreas;
	}

	public Set<ProgramaItem> getProgramasItems() {
		return programasItems;
	}

	public void setProgramasItems(Set<ProgramaItem> programasItems) {
		this.programasItems = programasItems;
	}

	public Set<Archivo> getArchivos() {
		return archivos;
	}

	public void setArchivos(Set<Archivo> archivos) {
		this.archivos = archivos;
	}

	public Profesor getDirectorPrograma() {
		return directorPrograma;
	}

	public void setDirectorPrograma(Profesor directorPrograma) {
		this.directorPrograma = directorPrograma;
	}

}

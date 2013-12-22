package modelo;

import java.util.Set;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "programa")
public class Programa {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "estatus")
	private Boolean estatus;

	@OneToMany(mappedBy = "programa")
	private Set<Estudiante> estudiantes;
	
	@OneToMany(mappedBy="programa")
	private Set<Profesor> profesores;

	@OneToMany(mappedBy = "programa")
	private Set<CondicionPrograma> condicionesProgramas;
	
	@OneToMany(mappedBy="programa")
	private Set<Cronograma> cronogramas;
	
	
	@OneToMany(mappedBy="programa")
	private Set<ProgramaArea> programasAreas;
	
	@OneToMany(mappedBy="programa")
	private Set<ProgramaRequisito> programaRequisitos;

	@OneToMany(mappedBy="programa")
	private Set<ProgramaItem> programasItems;
	
	public Programa(long id, String nombre, String descripcion,
			Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
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

	public Set<Profesor> getProfesores() {
		return profesores;
	}

	public void setProfesores(Set<Profesor> profesores) {
		this.profesores = profesores;
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

}

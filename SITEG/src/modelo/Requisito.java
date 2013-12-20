package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "requisito")
public class Requisito {

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

	@OneToMany(mappedBy = "requisito")
	private Set<TegRequisito> requisitosTeg;
	
	@OneToMany(mappedBy="requisito")
	private Set<ProgramaRequisito> programaRequisitos;
	
	
	
	public Requisito(long id, String nombre, String descripcion,
			Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Requisito() {
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

	public Set<TegRequisito> getRequisitosTeg() {
		return requisitosTeg;
	}

	public void setRequisitosTeg(Set<TegRequisito> requisitosTeg) {
		this.requisitosTeg = requisitosTeg;
	}
	
	
	public Set<ProgramaRequisito> getProgramaRequisitos() {
		return programaRequisitos;
	}

	public void setProgramaRequisitos(
			Set<ProgramaRequisito> programaRequisitos) {
		this.programaRequisitos = programaRequisitos;
	}
	
}

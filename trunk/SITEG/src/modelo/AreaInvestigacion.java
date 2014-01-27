package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="area_investigacion")
public class AreaInvestigacion {
	
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
	
	@OneToMany(mappedBy="areaInvestigacion")
	private Set<Tematica> tematicas;
	
	@OneToMany(mappedBy="area")
	private Set<ProgramaArea> programasAreas;

	public AreaInvestigacion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AreaInvestigacion(long id, String nombre, String descripcion,
			Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
		
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

	public Set<Tematica> getTemas() {
		return tematicas;
	}

	public void setTemas(Set<Tematica> temas) {
		this.tematicas = temas;
	}

	public Set<ProgramaArea> getProgramasAreas() {
		return programasAreas;
	}

	public void setProgramasAreas(Set<ProgramaArea> programasAreas) {
		this.programasAreas = programasAreas;
	}

	
	

}


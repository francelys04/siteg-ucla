package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "condicion")
public class Condicion {

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
	
	@OneToMany(mappedBy = "condicion")
	private Set<CondicionPrograma> condicionesProgramas;

	public Condicion(long id, String nombre, String descripcion, Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Condicion() {
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

	public Set<CondicionPrograma> getCondicionesProgramas() {
		return condicionesProgramas;
	}

	public void setCondicionesProgramas(Set<CondicionPrograma> condicionesProgramas) {
		this.condicionesProgramas = condicionesProgramas;
	}
	
}

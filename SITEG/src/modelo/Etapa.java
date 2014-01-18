package modelo;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="etapa")
public class Etapa {

	@Id
	@GeneratedValue
	@Column(name="id", nullable = false)
	private long id;
	
	@Column(name="nombre", length = 100)
	private String nombre;
	
	@Column(name="descripcion", length = 500)
	private String descripcion;
	
	@Column(name="duracion")
	private long duracion;
	
	@Column(name="estatus")
	private Boolean estatus;
	
	@OneToMany(mappedBy="etapa")
	private Set<TegEtapa> etapasTeg;

	public Etapa(long id, String nombre, String descripcion, long duracion,
			Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.duracion = duracion;
		this.estatus = estatus;
	}

	public Etapa() {
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

	public Set<TegEtapa> getEtapasTeg() {
		return etapasTeg;
	}

	public void setEtapasTeg(Set<TegEtapa> etapasTeg) {
		this.etapasTeg = etapasTeg;
	}

	public long getDuracion() {
		return duracion;
	}

	public void setDuracion(long duracion) {
		this.duracion = duracion;
	}	
}

package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import modelo.compuesta.Jurado;

@Entity
@Table(name="tipo_jurado")
public class TipoJurado {
	
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
	
	@OneToMany(mappedBy = "tipoJurado")
	private Set<Jurado> juradosTegss;

	public TipoJurado() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoJurado(long id, String nombre, String descripcion,
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

	public Set<Jurado> getJuradosTegss() {
		return juradosTegss;
	}

	public void setJuradosTegss(Set<Jurado> juradosTegss) {
		this.juradosTegss = juradosTegss;
	}

}


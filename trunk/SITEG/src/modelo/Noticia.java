package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="noticia")
public class Noticia {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(name="nombre")
	private String nombre;

	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="estatus")
	private Boolean estatus;
	
	@Column(name="imagen")
	private byte[] imagen;
	



	public Noticia() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Noticia(long id, String nombre, String descripcion, Boolean estatus,
			byte[] imagen) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.imagen = imagen;
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

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}
	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
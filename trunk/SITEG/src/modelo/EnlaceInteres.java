package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import modelo.seguridad.Usuario;

@Entity
@Table(name="enlace_interes")
public class EnlaceInteres {
	
	@Id
	@GeneratedValue
	@Column(name="id", nullable = false)
	private long id;
	
	@Column(name="nombre", length = 100)
	private String nombre;

	@Column(name="url", length = 500)
	private String url;
	
	@Column(name="estatus")
	private Boolean estatus;
	
	@Column(name="imagen")
	private byte[] imagen;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario;
	
	public EnlaceInteres() {
		super();
		// TODO Auto-generated constructor stub
	}


	public EnlaceInteres(long id, String nombre, String descripcion, Boolean estatus,
			byte[] imagen, Usuario usuario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.url = descripcion;
		this.estatus = estatus;
		this.imagen = imagen;
		this.usuario = usuario;
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

	

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
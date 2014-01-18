package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="arbol")
public class Arbol {

	@Id
	@GeneratedValue
	@Column(name="id", nullable = false)
	private long id;
	
	@Column(name="hijo")
	private long hijo;
	
	@Column(name="nombre", length = 100, unique = true)
	private String nombre;
	
	@Column(name="url", length = 250, nullable = false)
	private String url;

	@ManyToMany(mappedBy="arboles")
	private Set<Grupo> grupos;

	public Arbol() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Arbol(long id, long hijo, String nombre, String url) {
		super();
		this.id = id;
		this.hijo = hijo;
		this.nombre = nombre;
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getHijo() {
		return hijo;
	}

	public void setHijo(long hijo) {
		this.hijo = hijo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}
	
}

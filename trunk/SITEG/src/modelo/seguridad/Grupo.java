package modelo.seguridad;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "grupo")
public class Grupo {
	
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;
	
	@Column(name = "nombre", length = 100, unique = true)
	private String nombre;
	
	@Column(name = "estatus")
	private Boolean estatus;
	
	@ManyToMany(mappedBy="grupos")
	private Set<Usuario> usuarios ;
	
	@ManyToMany
	@JoinTable(name="arbol_grupo",
	     joinColumns={@JoinColumn(name="grupo_id")},
	     inverseJoinColumns={@JoinColumn(name="arbol_id")})
	     private Set<Arbol> arboles;

	public Grupo(long id, String nombre, Boolean estatus, Set<Arbol> arboles) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.setEstatus(estatus);
		this.arboles = arboles;
	}

	public Grupo() {
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

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Set<Arbol> getArboles() {
		return arboles;
	}

	public void setArboles(Set<Arbol> arboles) {
		this.arboles = arboles;
	}

}

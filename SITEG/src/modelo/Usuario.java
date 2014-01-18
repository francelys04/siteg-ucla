package modelo;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;
	
	@Column(name = "nombre", length = 100, unique = true)
	private String nombre;
	
	@Column(name = "password", length = 100)
	private String password;
	
	@Column(name = "estatus")
	private Boolean estatus;
	
	@Column(name = "imagen")
	private byte[] imagen;
	
	@ManyToMany
	@JoinTable(name="grupo_usuario",
	     joinColumns={@JoinColumn(name="usuario_id")},
	     inverseJoinColumns={@JoinColumn(name="grupo_id")})
	     private Set<Grupo> grupos;

	@OneToOne(mappedBy="usuario")
	private Profesor profesor;
	
	@OneToOne(mappedBy="usuario")
	private Estudiante estudiante;
	
	@OneToMany(mappedBy = "usuario")
	private Set<Noticia> noticias;
	
	public Usuario(long codigo, String nombre, String password, Boolean estatus, Set<Grupo> grupos, byte[] imagen) {
		super();
		this.id = codigo;
		this.nombre = nombre;
		this.password = password;
		this.setEstatus(estatus);
		this.grupos=grupos;
		this.imagen = imagen;
	}

	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long codigo) {
		this.id = codigo;
	}

	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Set<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public Estudiante getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}

	public Set<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(Set<Noticia> noticias) {
		this.noticias = noticias;
	}

}

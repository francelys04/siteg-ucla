package modelo;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import modelo.seguridad.Usuario;

@Entity
@Table(name = "estudiante")
public class Estudiante {

	@Id
	@Column(name = "cedula", nullable = false, length = 10)
	private String cedula;

	@Column(name = "nombre", length = 100)
	private String nombre;

	@Column(name = "apellido", length = 100)
	private String apellido;

	@Column(name = "correo_electronico", length = 100)
	private String correoElectronico;

	@Column(name = "sexo", length = 100)
	private String sexo;

	@Column(name = "direcion", length = 500)
	private String direccion;

	@Column(name = "telefono_movil", length = 15)
	private String telefono_movil;
	
	@Column(name = "telefono_fijo", length = 15)
	private String telefono_fijo;

	@Column(name = "estatus")
	private Boolean estatus;

	@ManyToOne(optional = false)
	@JoinColumn(name="programa_id", referencedColumnName = "id")
	private Programa programa;
	
	@ManyToMany(mappedBy="estudiantes")
	private Set<SolicitudTutoria> solicitudesTutorias;
	
	@ManyToMany(mappedBy="estudiantes")
	private Set<Teg> tegs;
	
	@OneToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario;
	
	public Estudiante() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Estudiante(String cedula, String nombre, String apellido,
			String correoElectronico, String sexo,
			String direccion, String telefono,String telefono_fijo, Boolean estatus, Programa programa, Usuario usuario) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.correoElectronico = correoElectronico;
		this.sexo = sexo;
		this.direccion = direccion;
		this.telefono_movil = telefono;
		this.telefono_fijo = telefono_fijo;
		this.estatus = estatus;
		this.programa = programa;
		this.usuario=usuario;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono_movil;
	}

	public void setTelefono(String telefono) {
		this.telefono_movil = telefono;
	}

	public String getTelefono_fijo() {
		return telefono_fijo;
	}

	public void setTelefono_fijo(String telefono_fijo) {
		this.telefono_fijo = telefono_fijo;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public Set<SolicitudTutoria> getSolicitudesTutorias() {
		return solicitudesTutorias;
	}

	public void setSolicitudesTutorias(Set<SolicitudTutoria> solicitudesTutorias) {
		this.solicitudesTutorias = solicitudesTutorias;
	}

	public Set<Teg> getTegs() {
		return tegs;
	}

	public void setTegs(Set<Teg> tegs) {
		this.tegs = tegs;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


}


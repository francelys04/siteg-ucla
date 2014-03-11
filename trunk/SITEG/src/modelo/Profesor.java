package modelo;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import modelo.compuesta.Jurado;
import modelo.seguridad.Usuario;

@Entity
@Table(name = "profesor")
public class Profesor {

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

	@Column(name = "direccion", length = 500)
	private String direccion;

	@Column(name = "telefono_movil", length = 15)
	private String telefono_movil;

	@Column(name = "telefono_fijo", length = 15)
	private String telefono_fijo;

	@Column(name = "estatus")
	private Boolean estatus;

	@ManyToOne(optional = false)
	@JoinColumn(name = "categoria_id", referencedColumnName = "id")
	private Categoria categoria;

	@OneToMany(mappedBy = "profesor")
	private Set<Factibilidad> factibilidad;

	@OneToMany(mappedBy = "profesor")
	private Set<Defensa> defensas;

	@OneToMany(mappedBy = "profesor")
	private Set<SolicitudTutoria> solicitudesTutoria;

	@ManyToMany(mappedBy = "profesores")
	private Set<Teg> tegs;

	@OneToMany(mappedBy = "tutor")
	private Set<Teg> teg;

	@OneToMany(mappedBy = "profesor")
	private Set<Jurado> juradosTeg;

	@ManyToMany
	@JoinTable(name = "profesor_tematica", joinColumns = { @JoinColumn(name = "profesor_cedula") }, inverseJoinColumns = { @JoinColumn(name = "tematica_id") })
	private Set<Tematica> tematicas;
	
	@OneToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario;
	
	@OneToOne(mappedBy="directorPrograma")
	private Programa programa;
	
	public Profesor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Profesor(String cedula, String nombre, String apellido,
			String correoElectronico, String sexo, String direccion,
			String telefono_movil, String telefono_fijo, Boolean estatus,
			Categoria categoria, Set<Tematica> tematicas, Usuario usuario) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.correoElectronico = correoElectronico;
		this.sexo = sexo;
		this.direccion = direccion;
		this.telefono_fijo = telefono_fijo;
		this.telefono_movil = telefono_movil;
		this.estatus = estatus;
		this.categoria = categoria;
		this.tematicas=tematicas;
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

	public String getTelefono_movil() {
		return telefono_movil;
	}

	public void setTelefono_movil(String telefono_movil) {
		this.telefono_movil = telefono_movil;
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Set<SolicitudTutoria> getSolicitudesTutoria() {
		return solicitudesTutoria;
	}

	public void setSolicitudesTutoria(Set<SolicitudTutoria> solicitudesTutoria) {
		this.solicitudesTutoria = solicitudesTutoria;
	}

	public Set<Teg> getTegs() {
		return tegs;
	}

	public void setTegs(Set<Teg> tegs) {
		this.tegs = tegs;
	}

	public Set<Teg> getTeg() {
		return teg;
	}

	public void setTeg(Set<Teg> teg) {
		this.teg = teg;
	}

	public Set<Jurado> getJuradosTeg() {
		return juradosTeg;
	}

	public void setJuradosTeg(Set<Jurado> juradosTeg) {
		this.juradosTeg = juradosTeg;
	}

	public Set<Factibilidad> getFactibilidad() {
		return factibilidad;
	}

	public void setFactibilidad(Set<Factibilidad> factibilidad) {
		this.factibilidad = factibilidad;
	}

	public Set<Defensa> getDefensas() {
		return defensas;
	}

	public void setDefensas(Set<Defensa> defensas) {
		this.defensas = defensas;
	}

	public Set<Tematica> getTematicas() {
		return tematicas;
	}

	public void setTematicas(Set<Tematica> tematicas) {
		this.tematicas = tematicas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}

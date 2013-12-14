package modelo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "estudiante")
public class Estudiante {

	@Id
	@Column(name = "cedula")
	private String cedula;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellido")
	private String apellido;

	@Column(name = "correo_electronico")
	private String correoElectronico;

	@Column(name = "sexo")
	private String sexo;

	@Column(name = "direcion")
	private String direccion;

	@Column(name = "telefono_movil")
	private String telefono_movil;
	
	@Column(name = "telefono_fijo")
	private String telefono_fijo;

	@Column(name = "estatus")
	private Boolean estatus;

	@ManyToOne
	@JoinColumn(name="programa_id", referencedColumnName = "id")
	private Programa programa;
	
	@ManyToMany(mappedBy="estudiantes")
	private Set<SolicitudTutoria> solicitudesTutorias;
	
	@ManyToMany(mappedBy="estudiantes")
	private Set<Teg> tegs;
	
	public Estudiante() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Estudiante(String cedula, String nombre, String apellido,
			String correoElectronico, String sexo,
			String direccion, String telefono,String telefono_fijo, Boolean estatus, Programa programa) {
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


}


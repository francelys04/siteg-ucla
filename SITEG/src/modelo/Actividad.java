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
import javax.persistence.Table;

@Entity
@Table(name = "actividad")
public class Actividad {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "estatus")
	private Boolean estatus;
	
	@OneToMany(mappedBy="actividad")
	private Set<Cronograma> cronogramas;
	
	@OneToMany(mappedBy="actividad")
	private Set<ActividadRequisito> actividadesRequisitos;

	public Actividad(long id, String nombre, String descripcion, Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Actividad() {
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

	public Set<Cronograma> getActividades() {
		return cronogramas;
	}

	public void setActividades(Set<Cronograma> actividades) {
		this.cronogramas = actividades;
	}

	public Set<ActividadRequisito> getActividadesRequisitos() {
		return actividadesRequisitos;
	}

	public void setActividadesRequisitos(
			Set<ActividadRequisito> actividadesRequisitos) {
		this.actividadesRequisitos = actividadesRequisitos;
	}
}

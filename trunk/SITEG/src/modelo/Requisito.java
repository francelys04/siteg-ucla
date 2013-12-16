package modelo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "requisito")
public class Requisito {

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

	@OneToMany(mappedBy = "requisito")
	private Set<TegRequisito> requisitosTeg;
	
	@OneToMany(mappedBy="requisito")
	private Set<ActividadRequisito> actividadesRequisitos;
	
	@OneToMany(mappedBy="requisito")
	private Set<ProgramaRequisito> programasRequisitos;
	
	public Requisito(long id, String nombre, String descripcion,
			Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}

	public Requisito() {
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

	public Set<TegRequisito> getRequisitosTeg() {
		return requisitosTeg;
	}

	public void setRequisitosTeg(Set<TegRequisito> requisitosTeg) {
		this.requisitosTeg = requisitosTeg;
	}

	public Set<ActividadRequisito> getActividadesRequisitos() {
		return actividadesRequisitos;
	}

	public void setActividadesRequisitos(
			Set<ActividadRequisito> actividadesRequisitos) {
		this.actividadesRequisitos = actividadesRequisitos;
	}

	public Set<ProgramaRequisito> getProgramasRequisitos() {
		return programasRequisitos;
	}

	public void setProgramasRequisitos(Set<ProgramaRequisito> programasRequisitos) {
		this.programasRequisitos = programasRequisitos;
	}
	@Override
	public String toString() {
		return this.nombre.toUpperCase();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((estatus == null) ? 0 : estatus.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Requisito other = (Requisito) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (estatus == null) {
			if (other.estatus != null)
				return false;
		} else if (!estatus.equals(other.estatus))
			return false;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	
}

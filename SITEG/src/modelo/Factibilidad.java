package modelo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import modelo.compuesta.ItemFactibilidad;

@Entity
@Table(name = "factibilidad")
public class Factibilidad {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor profesor;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha")
	private Date fecha;
	
	@Column(name="observacion", length = 500)
	private String observacion;
	
	@Column(name="estatus", length = 100)
	private String estatus;

	@OneToMany(mappedBy = "factibilidad")
	private Set<ItemFactibilidad> itemsFactibilidad;
	
	public Factibilidad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Factibilidad(long id, Teg teg, Profesor profesor, Date fecha,
			String observacion, String estatus) {
		super();
		this.id = id;
		this.teg = teg;
		this.profesor = profesor;
		this.fecha = fecha;
		this.observacion = observacion;
		this.estatus = estatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Teg getTeg() {
		return teg;
	}

	public void setTeg(Teg teg) {
		this.teg = teg;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Set<ItemFactibilidad> getItemsFactibilidad() {
		return itemsFactibilidad;
	}

	public void setItemsFactibilidad(Set<ItemFactibilidad> itemsFactibilidad) {
		this.itemsFactibilidad = itemsFactibilidad;
	}
}

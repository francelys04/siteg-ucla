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
import javax.persistence.Table;

@Entity
@Table(name = "factibilidad")
public class Factibilidad {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@ManyToOne
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor profesor;
	
	@Column(name="fecha")
	private Date fecha;
	
	@Column(name="observacion")
	private String observacion;
	
	@Column(name="estatus")
	private Boolean estatus;

	@OneToMany(mappedBy = "factibilidad")
	private Set<ItemFactibilidad> itemsFactibilidad;
	
	public Factibilidad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Factibilidad(long id, Teg teg, Profesor profesor, Date fecha,
			String observacion, Boolean estatus) {
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

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Set<ItemFactibilidad> getItemsFactibilidad() {
		return itemsFactibilidad;
	}

	public void setItemsFactibilidad(Set<ItemFactibilidad> itemsFactibilidad) {
		this.itemsFactibilidad = itemsFactibilidad;
	}
}

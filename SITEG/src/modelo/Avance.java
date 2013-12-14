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
@Table(name="avance")
public class Avance {

	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(name="fecha")
	private Date fecha;
	
	@Column(name="observacion")
	private String observacion;
	
	@Column(name="estatus")
	private Boolean estatus;
	
	@ManyToOne
	@JoinColumn(name="teg_id", referencedColumnName="id")
	private Teg teg;

	public Avance(long id, Date fecha, String observacion, Boolean estatus,
			Teg teg) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.observacion = observacion;
		this.estatus = estatus;
		this.teg = teg;
	}

	public Avance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Teg getTeg() {
		return teg;
	}

	public void setTeg(Teg teg) {
		this.teg = teg;
	}
	
}

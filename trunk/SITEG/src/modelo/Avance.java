package modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="avance")
public class Avance {

	@Id
	@GeneratedValue
	@Column(name="id", nullable = false)
	private long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha")
	private Date fecha;
	
	@Column(name="observacion", length = 500)
	private String observacion;
	
	@Column(name="estatus", length = 100)
	private String estatus;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="teg_id", referencedColumnName="id")
	private Teg teg;

	public Avance(long id, Date fecha, String observacion, String estatus,
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

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Teg getTeg() {
		return teg;
	}

	public void setTeg(Teg teg) {
		this.teg = teg;
	}
	
}

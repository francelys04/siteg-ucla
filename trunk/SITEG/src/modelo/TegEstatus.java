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
@Table(name = "teg_estatus")
public class TegEstatus {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@Column(name = "nombre", length = 100)
	private String nombre;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_estatus")
	private Date fechaEstatus;

	public TegEstatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TegEstatus(long id, Teg teg, String nombre, Date fechaEstatus) {
		super();
		this.id = id;
		this.teg = teg;
		this.nombre = nombre;
		this.fechaEstatus = fechaEstatus;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaEstatus() {
		return fechaEstatus;
	}

	public void setFechaEstatus(Date fechaEstatus) {
		this.fechaEstatus = fechaEstatus;
	}
}

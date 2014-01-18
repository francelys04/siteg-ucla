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

@Entity
@Table(name = "defensa")
public class Defensa {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;

	@OneToOne
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date fecha;

	@Temporal(TemporalType.TIME)
	@Column(name = "hora")
	private Date hora;

	@Column(name = "lugar", length = 100)
	private String lugar;

	@Column(name = "estatus", length = 100)
	private String estatus;

	@OneToMany(mappedBy = "defensa")
	private Set<ItemDefensa> itemsDefensas;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor profesor;
	
	public Defensa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Defensa(long id, Teg teg, Date fecha, Date hora, String lugar,
			String estatus, Profesor profesor) {
		super();
		this.id = id;
		this.teg = teg;
		this.fecha = fecha;
		this.hora = hora;
		this.lugar = lugar;
		
		this.estatus = estatus;
		this.profesor = profesor;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Set<ItemDefensa> getItemsDefensas() {
		return itemsDefensas;
	}

	public void setItemsDefensas(Set<ItemDefensa> itemsDefensas) {
		this.itemsDefensas = itemsDefensas;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

}

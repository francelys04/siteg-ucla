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

@Entity
@Table(name = "defensa")
public class Defensa {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@OneToOne
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;

	@Column(name = "fecha")
	private Date fecha;

	@Column(name = "hora")
	private Date hora;

	@Column(name = "lugar")
	private String lugar;

	@Column(name = "nota")
	private int nota;

	@Column(name = "estatus")
	private Boolean estatus;

	@OneToMany(mappedBy = "defensa")
	private Set<ItemDefensa> itemsDefensas;
	
	@ManyToOne
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor profesor;
	
	public Defensa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Defensa(long id, Teg teg, Date fecha, Date hora, String lugar,
			int nota, Boolean estatus, Profesor profesor) {
		super();
		this.id = id;
		this.teg = teg;
		this.fecha = fecha;
		this.hora = hora;
		this.lugar = lugar;
		this.nota = nota;
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

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
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

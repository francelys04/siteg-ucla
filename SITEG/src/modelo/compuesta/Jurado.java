package modelo.compuesta;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import modelo.Profesor;
import modelo.Teg;
import modelo.TipoJurado;
import modelo.compuesta.id.JuradoId;

@Entity
@Table(name="jurado")
@IdClass(JuradoId.class)
public class Jurado {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor profesor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "tipo_jurado_id", referencedColumnName = "id")
	private TipoJurado tipoJurado;

	public Jurado() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Jurado(Teg teg, Profesor profesor, TipoJurado tipoJurado) {
		super();
		this.teg = teg;
		this.profesor = profesor;
		this.tipoJurado = tipoJurado;
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

	public TipoJurado getTipoJurado() {
		return tipoJurado;
	}

	public void setTipoJurado(TipoJurado tipoJurado) {
		this.tipoJurado = tipoJurado;
	}
	
}

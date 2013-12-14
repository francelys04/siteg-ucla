package modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="jurado")
@IdClass(JuradoId.class)
public class Jurado {

	@Id
	@ManyToOne
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor profesor;
	
	@ManyToOne
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

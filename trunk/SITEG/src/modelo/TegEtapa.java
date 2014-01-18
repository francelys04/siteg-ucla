package modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="teg_etapa")
@IdClass(TegEtapaId.class)
public class TegEtapa {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "etapa_id", referencedColumnName = "id")
	private Etapa etapa;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date fecha;

	public TegEtapa(Teg teg, Etapa etapa, Date fecha) {
		super();
		this.teg = teg;
		this.etapa = etapa;
		this.fecha = fecha;
	}

	public TegEtapa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Teg getTeg() {
		return teg;
	}

	public void setTeg(Teg teg) {
		this.teg = teg;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}

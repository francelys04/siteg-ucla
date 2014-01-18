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
@Table(name="teg_requisito")
@IdClass(TegRequisitoId.class)
public class TegRequisito {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "requisito_id", referencedColumnName = "id")
	private Requisito requisito;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_entrega")
	private Date fechaEntrega;
	
	public TegRequisito() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TegRequisito(Requisito requisito, Teg teg, Date fechaEntrega) {
		super();
		this.requisito = requisito;
		this.teg = teg;
		this.fechaEntrega = fechaEntrega;
	}

	public Requisito getRequisito() {
		return requisito;
	}

	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}

	public Teg getTeg() {
		return teg;
	}

	public void setTeg(Teg teg) {
		this.teg = teg;
	}

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

}

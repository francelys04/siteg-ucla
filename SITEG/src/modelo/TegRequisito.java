package modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="teg_requisito")
@IdClass(TegRequisitoId.class)
public class TegRequisito {

	@Id
	@ManyToOne
	@JoinColumn(name = "requisito_id", referencedColumnName = "id")
	private Requisito requisito;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "teg_id", referencedColumnName = "id")
	private Teg teg;
	
	@Column(name = "fecha_entrega")
	private Date fechaEntrega;
	
	@Column(name = "estado")
	private String estado;

	public TegRequisito() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TegRequisito(Requisito requisito, Teg teg, Date fechaEntrega,
			String estado) {
		super();
		this.requisito = requisito;
		this.teg = teg;
		this.fechaEntrega = fechaEntrega;
		this.estado = estado;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}

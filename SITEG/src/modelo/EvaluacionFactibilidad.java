package modelo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "evaluacion_factibilidad")
public class EvaluacionFactibilidad {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "observacion")
	private String observacion;

	@Column(name = "fecha_evaluacion")
	private Date fechaEvaluacion;

	@Column(name = "estatus")
	private Boolean estatus;

	public EvaluacionFactibilidad(long id, String observacion,
			Date fechaEvaluacion, Boolean estatus) {
		super();
		this.id = id;
		this.observacion = observacion;
		this.fechaEvaluacion = fechaEvaluacion;
		this.estatus = estatus;
	}

	public EvaluacionFactibilidad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFechaEvaluacion() {
		return fechaEvaluacion;
	}

	public void setFechaEvaluacion(Date fechaEvaluacion) {
		this.fechaEvaluacion = fechaEvaluacion;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

}

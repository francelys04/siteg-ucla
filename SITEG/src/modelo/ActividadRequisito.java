package modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "actividad_requisito")
@IdClass(ActividadRequisitoId.class)
public class ActividadRequisito {

	@Id
	@ManyToOne
	@JoinColumn(name = "actividad_id", referencedColumnName = "id")
	private Actividad actividad;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "requisito_id", referencedColumnName = "id")
	private Requisito requisito;
	
	@ManyToOne
	@JoinColumn(name = "lapso_id", referencedColumnName = "id")
	private Lapso lapso;

	public ActividadRequisito() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ActividadRequisito(Actividad actividad, Requisito requisito,
			Lapso lapso) {
		super();
		this.actividad = actividad;
		this.requisito = requisito;
		this.lapso = lapso;
	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public Requisito getRequisito() {
		return requisito;
	}

	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}

	public Lapso getLapso() {
		return lapso;
	}

	public void setLapso(Lapso lapso) {
		this.lapso = lapso;
	}
}

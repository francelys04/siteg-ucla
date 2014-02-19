package modelo.compuesta;

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

import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.id.CronogramaId;

@Entity
@Table(name = "cronograma")
@IdClass(CronogramaId.class)
public class Cronograma {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "programa_id", referencedColumnName = "id")
	private Programa programa;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "actividad_id", referencedColumnName = "id")
	private Actividad actividad;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "lapso_id", referencedColumnName = "id")
	private Lapso lapso;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_inicio")
	private Date fechaInicio;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_fin")
	private Date fechaFin;

	public Cronograma(Lapso lapso, Programa programa,
			Actividad actividad, Date fechaInicio, Date fechaFin) {
		super();
		this.lapso = lapso;
		this.programa = programa;
		this.actividad = actividad;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	public Cronograma() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Lapso getLapso() {
		return lapso;
	}

	public void setLapso(Lapso lapso) {
		this.lapso = lapso;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}

package modelo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import modelo.compuesta.CondicionPrograma;
import modelo.compuesta.Cronograma;
import modelo.compuesta.ProgramaArea;
import modelo.compuesta.ProgramaItem;
import modelo.compuesta.ProgramaRequisito;

@Entity
@Table(name = "lapso")
public class Lapso {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;
	
	@Column(name = "nombre", length = 100)
	private String nombre;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_inicial")
	private Date fechaInicial;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_final")
	private Date fechaFinal;
	
	@Column(name = "estatus")
	private Boolean estatus;
	
	@OneToMany(mappedBy="lapso")
	private Set<Cronograma> cronogramas;
	
	@OneToMany(mappedBy="lapso")
	private Set<ProgramaArea> programasAreas;
	
	@OneToMany(mappedBy="lapso")
	private Set<ProgramaItem> programasItems;
	
	@OneToMany(mappedBy="lapso")
	private Set<ProgramaRequisito> programasRequisitos;
	
	@OneToMany(mappedBy="lapso")
	private Set<CondicionPrograma> programasLapsos;
	
	public Lapso(long id, String nombre, Date fechaInicial, Date fechaFinal,
			Boolean estatus) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
		this.estatus = estatus;
	}

	public Lapso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Set<Cronograma> getCronogramas() {
		return cronogramas;
	}

	public void setCronogramas(Set<Cronograma> cronogramas) {
		this.cronogramas = cronogramas;
	}

	public Set<ProgramaArea> getProgramasAreas() {
		return programasAreas;
	}

	public void setProgramasAreas(Set<ProgramaArea> programasAreas) {
		this.programasAreas = programasAreas;
	}

	public Set<ProgramaItem> getProgramasItems() {
		return programasItems;
	}

	public void setProgramasItems(Set<ProgramaItem> programasItems) {
		this.programasItems = programasItems;
	}

	public Set<CondicionPrograma> getProgramasLapsos() {
		return programasLapsos;
	}

	public void setProgramasLapsos(Set<CondicionPrograma> programasLapsos) {
		this.programasLapsos = programasLapsos;
	}

	public Set<ProgramaRequisito> getProgramasRequisitos() {
		return programasRequisitos;
	}

	public void setProgramasRequisitos(Set<ProgramaRequisito> programasRequisitos) {
		this.programasRequisitos = programasRequisitos;
	}

	
}

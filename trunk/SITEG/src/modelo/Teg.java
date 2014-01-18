package modelo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "teg")
public class Teg {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "titulo", length = 500)
	private String titulo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date fecha;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_inicio")
	private Date fechaInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_entrega")
	private Date fechaEntrega;

	@Column(name = "descripcion", length = 500)
	private String descripcion;

	@Column(name = "duracion")
	private long duracion;

	@ManyToOne(optional = false)
	@JoinColumn(name = "profesor_cedula", referencedColumnName = "cedula")
	private Profesor tutor;

	@OneToOne(mappedBy="teg")
	private Defensa defensa;

	@Column(name = "estatus", length = 100)
	private String estatus;

	@ManyToOne(optional = false)
	@JoinColumn(name = "tematica_id", referencedColumnName = "id")
	private Tematica tematica;

	@OneToMany(mappedBy = "teg")
	private Set<Avance> avances;
	
	@OneToMany(mappedBy = "teg")
	private Set<Factibilidad> factibilidades;

	@OneToMany(mappedBy = "teg")
	private Set<TegEtapa> etapasTeg;

	@OneToMany(mappedBy = "teg")
	private Set<TegRequisito> requisitosTegs;

	@OneToMany(mappedBy = "teg")
	private Set<Jurado> juradosTegs;
	
	@ManyToMany
	@JoinTable(name = "comision", joinColumns = { @JoinColumn(name = "teg_id") }, inverseJoinColumns = { @JoinColumn(name = "profesor_cedula") })
	private Set<Profesor> profesores;
	
	@ManyToMany
	@JoinTable(name = "estudiante_teg", joinColumns = { @JoinColumn(name = "teg_id") }, inverseJoinColumns = { @JoinColumn(name = "estudiante_cedula") })
	private Set<Estudiante> estudiantes;

	public Teg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Teg(long id, String titulo, Date fecha, Date fechaInicio, Date fechaEntrega,
			String descripcion, long duracion, Profesor tutor, String estatus,
			Tematica tematica, Set<Profesor> profesores, Set<Estudiante> estudiantes) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.fecha = fecha;
		this.fechaInicio = fechaInicio;
		this.fechaEntrega = fechaEntrega;
		this.descripcion = descripcion;
		this.duracion = duracion;
		this.tutor = tutor;
		this.estatus = estatus;
		this.tematica = tematica;
		this.profesores = profesores;
		this.estudiantes = estudiantes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Tematica getTematica() {
		return tematica;
	}

	public void setTematica(Tematica tematica) {
		this.tematica = tematica;
	}

	public Set<Avance> getAvances() {
		return avances;
	}

	public void setAvances(Set<Avance> avances) {
		this.avances = avances;
	}

	public Set<TegEtapa> getEtapasTeg() {
		return etapasTeg;
	}

	public void setEtapasTeg(Set<TegEtapa> etapasTeg) {
		this.etapasTeg = etapasTeg;
	}

	public Set<TegRequisito> getRequisitosTegs() {
		return requisitosTegs;
	}

	public void setRequisitosTegs(Set<TegRequisito> requisitosTegs) {
		this.requisitosTegs = requisitosTegs;
	}

	public Set<Profesor> getProfesores() {
		return profesores;
	}

	public void setProfesores(Set<Profesor> profesores) {
		this.profesores = profesores;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public long getDuracion() {
		return duracion;
	}

	public void setDuracion(long duracion) {
		this.duracion = duracion;
	}

	public Profesor getTutor() {
		return tutor;
	}

	public void setTutor(Profesor tutor) {
		this.tutor = tutor;
	}

	public Set<Jurado> getJuradosTegs() {
		return juradosTegs;
	}

	public void setJuradosTegs(Set<Jurado> juradosTegs) {
		this.juradosTegs = juradosTegs;
	}

	public Set<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(Set<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public Set<Factibilidad> getFactibilidades() {
		return factibilidades;
	}

	public void setFactibilidades(Set<Factibilidad> factibilidades) {
		this.factibilidades = factibilidades;
	}

	public Defensa getDefensa() {
		return defensa;
	}

	public void setDefensa(Defensa defensa) {
		this.defensa = defensa;
	}

}

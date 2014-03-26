package modelo.reporte;

public class InformeFactibilidad {
	private String estatus;
	private String nestudiante;
	private String cestudiante;
	private String titulo;
	private String programateg;
	private String tutor;
	private String observacion;
	private String director;

	public InformeFactibilidad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getNestudiante() {
		return nestudiante;
	}

	public void setNestudiante(String nestudiante) {
		this.nestudiante = nestudiante;
	}

	public String getCestudiante() {
		return cestudiante;
	}

	public void setCestudiante(String cestudiante) {
		this.cestudiante = cestudiante;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getProgramateg() {
		return programateg;
	}

	public void setProgramateg(String programateg) {
		this.programateg = programateg;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getTutor() {
		return tutor;
	}

	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public InformeFactibilidad(String estatus, String nestudiante,
			String cestudiante, String titulo, String programateg,
			String tutor, String observacion, String director) {
		super();

		this.estatus = estatus;
		this.nestudiante = nestudiante;
		this.cestudiante = cestudiante;
		this.titulo = titulo;
		this.programateg = programateg;
		this.tutor = tutor;
		this.observacion = observacion;
		this.director = director;

	}

}
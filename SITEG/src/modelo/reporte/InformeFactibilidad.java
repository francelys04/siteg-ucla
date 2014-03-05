package modelo.reporte;

import java.util.Date;
import java.util.List;

import modelo.Estudiante;
import modelo.Teg;

public class InformeFactibilidad{
	private String estatus;
	private String nestudiante;
	private String aestudiante;
	private String cestudiante;
	private String titulo;
	private String programateg;
	private String nprofesor;
	private String aprofesor;
	private String observacion;	
	private String ndirector;
	private String adirector;
	 /* List<Estudiante> est = servicioEstudiante.buscarEstudiantesDelTeg(teg);*/
	
	
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
	public String getAestudiante() {
		return aestudiante;
	}
	public void setAestudiante(String aestudiante) {
		this.aestudiante = aestudiante;
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
	public String getNprofesor() {
		return nprofesor;
	}
	public void setNprofesor(String nprofesor) {
		this.nprofesor = nprofesor;
	}
	public String getAprofesor() {
		return aprofesor;
	}
	public void setAprofesor(String aprofesor) {
		this.aprofesor = aprofesor;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getNdirector() {
		return ndirector;
	}
	public void setNdirector(String ndirector) {
		this.ndirector = ndirector;
	}
	public String getAdirector() {
		return adirector;
	}
	public void setAdirector(String adirector) {
		this.adirector = adirector;
	}
	
  
    
	public InformeFactibilidad( String estatus, String nestudiante,
			String aestudiante, String cestudiante, String titulo,
			String programateg, String nprofesor, String aprofesor,
			String observacion, String ndirector, String adirector) {
		super();
		
		this.estatus = estatus;
		this.nestudiante = nestudiante;
		this.aestudiante = aestudiante;
		this.cestudiante = cestudiante;
		this.titulo = titulo;
		this.programateg = programateg;
		this.nprofesor = nprofesor;
		this.aprofesor = aprofesor;
		this.observacion = observacion;
		this.ndirector = ndirector;
		this.adirector = adirector;
	}


	
	
	

}
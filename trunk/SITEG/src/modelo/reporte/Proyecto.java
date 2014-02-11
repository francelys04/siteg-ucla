package modelo.reporte;

import java.util.Date;

import modelo.Teg;

public class Proyecto{
	private Teg teg;
	
	private String tituloTeg;
	private String nombreTutor;
	private String nombreEstudiante;
	
	
	private String programa;
	private String tematica;
	private String area;
	
	public Proyecto(Teg teg, String nombreEstudiante) {
		super();
		this.teg = teg;
		this.nombreEstudiante = nombreEstudiante;
	}
	public Proyecto(String tituloTeg, String nombreTutor,
			String nombreEstudiante,
			String programa, String tematica, String area) {
		super();
		this.tituloTeg = tituloTeg;
		this.nombreTutor = nombreTutor;
		this.nombreEstudiante = nombreEstudiante;
		
		
		this.programa = programa;
		this.tematica = tematica;
		this.area = area;
	}
	public String getTituloTeg() {
		return tituloTeg;
	}
	public void setTituloTeg(String tituloTeg) {
		this.tituloTeg = tituloTeg;
	}
	public String getNombreTutor() {
		return nombreTutor;
	}
	public void setNombreTutor(String nombreTutor) {
		this.nombreTutor = nombreTutor;
	}
	public String getNombreEstudiante() {
		return nombreEstudiante;
	}
	public void setNombreEstudiante(String nombreEstudiante) {
		this.nombreEstudiante = nombreEstudiante;
	}
	
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public String getTematica() {
		return tematica;
	}
	public void setTematica(String tematica) {
		this.tematica = tematica;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Teg getTeg() {
		return teg;
	}
	public void setTeg(Teg teg) {
		this.teg = teg;
	}
	
	
	

}
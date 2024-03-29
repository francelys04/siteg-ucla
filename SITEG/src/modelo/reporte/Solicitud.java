package modelo.reporte;

import java.util.Date;

import modelo.Teg;

public class Solicitud{
	private Teg teg;
	
	private String tituloTeg;
	private String descripcionTeg;	
	private java.util.Date fechaTeg;
	private String estatus;
	private String cedulaProfesor;
	private String nombreTutor;
	private String apellidoTutor;
	private String correoTutor;
	private String direccionTutor;	
	private String cedulaEstudiante;;
	private String apellidoEstudiante;
	private String nombreEstudiante;
	private String correoEstudiante;
	private String direccionEstudiante;
	private String programa;
	private String correoPrograma;
	private String descripcionPrograma;	
	private String tematica;
	private String descripcionTematica;
	private String area;
	private String descripcionArea;
	
	
	public Solicitud(Teg teg, String nombreEstudiante) {
		super();
		this.teg = teg;
		this.nombreEstudiante = nombreEstudiante;
	}
	
	public Solicitud(String tituloTeg, Date fechaTeg, String cedulaProfesor,
			String nombreTutor, String apellidoTutor, String cedulaEstudiante,
			String apellidoEstudiante, String nombreEstudiante) {
		super();
		this.tituloTeg = tituloTeg;
		this.fechaTeg = fechaTeg;
		this.cedulaProfesor = cedulaProfesor;
		this.nombreTutor = nombreTutor;
		this.apellidoTutor = apellidoTutor;
		this.cedulaEstudiante = cedulaEstudiante;
		this.apellidoEstudiante = apellidoEstudiante;
		this.nombreEstudiante = nombreEstudiante;
	}

	public Solicitud(String tituloTeg, String nombreTutor,
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
	
	
	public Solicitud(String tituloTeg, String nombreTutor,
			String nombreEstudiante, String programa, String tematica,
			String area, String estatus) {
		super();
		this.tituloTeg = tituloTeg;
		this.nombreTutor = nombreTutor;
		this.nombreEstudiante = nombreEstudiante;
		this.programa = programa;
		this.tematica = tematica;
		this.area = area;
		this.estatus = estatus;
	}

		
	public Solicitud(String tituloTeg, String cedulaProfesor,
			String nombreTutor, String cedulaEstudiante, String nombreEstudiante) {
		super();
		this.tituloTeg = tituloTeg;
		this.cedulaProfesor = cedulaProfesor;
		this.nombreTutor = nombreTutor;
		this.cedulaEstudiante = cedulaEstudiante;
		this.nombreEstudiante = nombreEstudiante;
	}

	public Teg getTeg() {
		return teg;
	}
	public void setTeg(Teg teg) {
		this.teg = teg;
	}
	public String getTituloTeg() {
		return tituloTeg;
	}
	public void setTituloTeg(String tituloTeg) {
		this.tituloTeg = tituloTeg;
	}
	public String getDescripcionTeg() {
		return descripcionTeg;
	}
	public void setDescripcionTeg(String descripcionTeg) {
		this.descripcionTeg = descripcionTeg;
	}
	public java.util.Date getFechaTeg() {
		return fechaTeg;
	}
	public void setFechaTeg(java.util.Date fechaTeg) {
		this.fechaTeg = fechaTeg;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getCedulaProfesor() {
		return cedulaProfesor;
	}
	public void setCedulaProfesor(String cedulaProfesor) {
		this.cedulaProfesor = cedulaProfesor;
	}
	public String getNombreTutor() {
		return nombreTutor;
	}
	public void setNombreTutor(String nombreTutor) {
		this.nombreTutor = nombreTutor;
	}
	public String getApellidoTutor() {
		return apellidoTutor;
	}
	public void setApellidoTutor(String apellidoTutor) {
		this.apellidoTutor = apellidoTutor;
	}
	public String getCorreoTutor() {
		return correoTutor;
	}
	public void setCorreoTutor(String correoTutor) {
		this.correoTutor = correoTutor;
	}
	public String getDireccionTutor() {
		return direccionTutor;
	}
	public void setDireccionTutor(String direccionTutor) {
		this.direccionTutor = direccionTutor;
	}
	public String getCedulaEstudiante() {
		return cedulaEstudiante;
	}
	public void setCedulaEstudiante(String cedulaEstudiante) {
		this.cedulaEstudiante = cedulaEstudiante;
	}
	public String getApellidoEstudiante() {
		return apellidoEstudiante;
	}
	public void setApellidoEstudiante(String apellidoEstudiante) {
		this.apellidoEstudiante = apellidoEstudiante;
	}
	public String getNombreEstudiante() {
		return nombreEstudiante;
	}
	public void setNombreEstudiante(String nombreEstudiante) {
		this.nombreEstudiante = nombreEstudiante;
	}
	public String getCorreoEstudiante() {
		return correoEstudiante;
	}
	public void setCorreoEstudiante(String correoEstudiante) {
		this.correoEstudiante = correoEstudiante;
	}
	public String getDireccionEstudiante() {
		return direccionEstudiante;
	}
	public void setDireccionEstudiante(String direccionEstudiante) {
		this.direccionEstudiante = direccionEstudiante;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public String getCorreoPrograma() {
		return correoPrograma;
	}
	public void setCorreoPrograma(String correoPrograma) {
		this.correoPrograma = correoPrograma;
	}
	public String getDescripcionPrograma() {
		return descripcionPrograma;
	}
	public void setDescripcionPrograma(String descripcionPrograma) {
		this.descripcionPrograma = descripcionPrograma;
	}
	public String getTematica() {
		return tematica;
	}
	public void setTematica(String tematica) {
		this.tematica = tematica;
	}
	public String getDescripcionTematica() {
		return descripcionTematica;
	}
	public void setDescripcionTematica(String descripcionTematica) {
		this.descripcionTematica = descripcionTematica;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDescripcionArea() {
		return descripcionArea;
	}
	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}
	public Solicitud(String descripcionTeg, Date fechaTeg, String estatus,
			String cedulaProfesor, String nombreTutor, String apellidoTutor,
			String correoTutor, String direccionTutor, String cedulaEstudiante,
			String apellidoEstudiante, String nombreEstudiante,
			String correoEstudiante, String direccionEstudiante,
			String programa, String correoPrograma, String descripcionPrograma,
			String tematica, String descripcionTematica, String area,
			String descripcionArea) {
		super();
		this.descripcionTeg = descripcionTeg;
		this.fechaTeg = fechaTeg;
		this.estatus = estatus;
		this.cedulaProfesor = cedulaProfesor;
		this.nombreTutor = nombreTutor;
		this.apellidoTutor = apellidoTutor;
		this.correoTutor = correoTutor;
		this.direccionTutor = direccionTutor;
		this.cedulaEstudiante = cedulaEstudiante;
		this.apellidoEstudiante = apellidoEstudiante;
		this.nombreEstudiante = nombreEstudiante;
		this.correoEstudiante = correoEstudiante;
		this.direccionEstudiante = direccionEstudiante;
		this.programa = programa;
		this.correoPrograma = correoPrograma;
		this.descripcionPrograma = descripcionPrograma;
		this.tematica = tematica;
		this.descripcionTematica = descripcionTematica;
		this.area = area;
		this.descripcionArea = descripcionArea;
	}
	
	
	
	
	
	

}
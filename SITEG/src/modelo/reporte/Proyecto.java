package modelo.reporte;

import java.sql.Date;

import modelo.Teg;

public class Proyecto{
	private Teg teg;	
	private String tituloTeg;
	private String descripcionTeg;	
	private java.util.Date fechaTeg;
	private String cedulaProfesor;
	private String nombreTutor;
	private String apellidoTutor;
	private String correoTutor;
	private String direccionTutor;	
	private String cedulaEstudiante;
	private String nombreEstudiante;
	private String apellidoEstudiante;
	private String correoEstudiante;
	private String direccionEstudiante;
	private String programa;
	private String correoPrograma;
	private String descripcionPrograma;		
	private String tematica;
	private String descripcionTematica;
	private String area;
	private String descripcionArea;
	private java.util.Date fechadefensa;
	private java.util.Date horadefensa;
	private String lugardefensa;
	private String profesordefensa;
	private String observacionfactibilidad;
	
	public Proyecto(Teg teg, String nombreEstudiante) {
		super();
		this.teg = teg;
		this.nombreEstudiante = nombreEstudiante;
	}
	
	
	
	
	


	public Proyecto(String tituloTeg, String descripcionTeg,
			java.util.Date fechaTeg, String cedulaProfesor, String nombreTutor,
			String apellidoTutor, String correoTutor, String direccionTutor,
			String cedulaEstudiante, String nombreEstudiante,
			String apellidoEstudiante, String correoEstudiante,
			String direccionEstudiante, String programa, String correoPrograma,
			String descripcionPrograma, String tematica,
			String descripcionTematica, String area, String descripcionArea,
			java.util.Date fechadefensa, java.util.Date horadefensa,
			String lugardefensa, String profesordefensa) {
		super();
		this.tituloTeg = tituloTeg;
		this.descripcionTeg = descripcionTeg;
		this.fechaTeg = fechaTeg;
		this.cedulaProfesor = cedulaProfesor;
		this.nombreTutor = nombreTutor;
		this.apellidoTutor = apellidoTutor;
		this.correoTutor = correoTutor;
		this.direccionTutor = direccionTutor;
		this.cedulaEstudiante = cedulaEstudiante;
		this.nombreEstudiante = nombreEstudiante;
		this.apellidoEstudiante = apellidoEstudiante;
		this.correoEstudiante = correoEstudiante;
		this.direccionEstudiante = direccionEstudiante;
		this.programa = programa;
		this.correoPrograma = correoPrograma;
		this.descripcionPrograma = descripcionPrograma;
		this.tematica = tematica;
		this.descripcionTematica = descripcionTematica;
		this.area = area;
		this.descripcionArea = descripcionArea;
		this.fechadefensa = fechadefensa;
		this.horadefensa = horadefensa;
		this.lugardefensa = lugardefensa;
		this.profesordefensa = profesordefensa;
	}







	public Proyecto(String tituloTeg, String descripcionTeg,
			java.util.Date fechaTeg, String cedulaProfesor, String nombreTutor,
			String apellidoTutor, String correoTutor, String direccionTutor,
			String cedulaEstudiante, String nombreEstudiante,
			String apellidoEstudiante, String correoEstudiante,
			String direccionEstudiante, String programa, String correoPrograma,
			String descripcionPrograma, String tematica,
			String descripcionTematica, String area, String descripcionArea,
			java.util.Date fechadefensa, String profesordefensa,
			String observacionfactibilidad) {
		super();
		this.tituloTeg = tituloTeg;
		this.descripcionTeg = descripcionTeg;
		this.fechaTeg = fechaTeg;
		this.cedulaProfesor = cedulaProfesor;
		this.nombreTutor = nombreTutor;
		this.apellidoTutor = apellidoTutor;
		this.correoTutor = correoTutor;
		this.direccionTutor = direccionTutor;
		this.cedulaEstudiante = cedulaEstudiante;
		this.nombreEstudiante = nombreEstudiante;
		this.apellidoEstudiante = apellidoEstudiante;
		this.correoEstudiante = correoEstudiante;
		this.direccionEstudiante = direccionEstudiante;
		this.programa = programa;
		this.correoPrograma = correoPrograma;
		this.descripcionPrograma = descripcionPrograma;
		this.tematica = tematica;
		this.descripcionTematica = descripcionTematica;
		this.area = area;
		this.descripcionArea = descripcionArea;
		this.fechadefensa = fechadefensa;
		this.profesordefensa = profesordefensa;
		this.observacionfactibilidad = observacionfactibilidad;
	}







	public Proyecto(String tituloTeg, String descripcionTeg,
			java.util.Date fecha, String cedulaProfesor, String nombreTutor,
			String apellidoTutor, String correoTutor, String direccionTutor,
			String cedulaEstudiante, String nombreEstudiante,
			String apellidoEstudiante, String correoEstudiante,
			String direccionEstudiante, String programa, String correoPrograma,
			String descripcionPrograma, String tematica,
			String descripcionTematica, String area, String descripcionArea) {
		super();
		this.tituloTeg = tituloTeg;
		this.descripcionTeg = descripcionTeg;
		this.fechaTeg = fecha;
		this.cedulaProfesor = cedulaProfesor;
		this.nombreTutor = nombreTutor;
		this.apellidoTutor = apellidoTutor;
		this.correoTutor = correoTutor;
		this.direccionTutor = direccionTutor;
		this.cedulaEstudiante = cedulaEstudiante;
		this.nombreEstudiante = nombreEstudiante;
		this.apellidoEstudiante = apellidoEstudiante;
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
	
	public java.util.Date getFechadefensa() {
		return fechadefensa;
	}







	public void setFechadefensa(java.util.Date fechadefensa) {
		this.fechadefensa = fechadefensa;
	}







	public java.util.Date getHoradefensa() {
		return horadefensa;
	}







	public void setHoradefensa(java.util.Date horadefensa) {
		this.horadefensa = horadefensa;
	}







	public String getLugardefensa() {
		return lugardefensa;
	}







	public void setLugardefensa(String lugardefensa) {
		this.lugardefensa = lugardefensa;
	}







	public String getProfesordefensa() {
		return profesordefensa;
	}







	public void setProfesordefensa(String profesordefensa) {
		this.profesordefensa = profesordefensa;
	}







	public String getObservacionfactibilidad() {
		return observacionfactibilidad;
	}







	public void setObservacionfactibilidad(String observacionfactibilidad) {
		this.observacionfactibilidad = observacionfactibilidad;
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



	public String getCedulaProfesor() {
		return cedulaProfesor;
	}



	public void setCedulaProfesor(String cedulaProfesor) {
		this.cedulaProfesor = cedulaProfesor;
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



	public String getDescripcionTematica() {
		return descripcionTematica;
	}



	public void setDescripcionTematica(String descripcionTematica) {
		this.descripcionTematica = descripcionTematica;
	}



	public String getDescripcionArea() {
		return descripcionArea;
	}



	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}
	
	
	
	

}
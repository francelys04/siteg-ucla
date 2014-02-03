package modelo.reporte;

import java.util.Date;

public class DefensaTeg {
	
		private String tituloTeg;
		private String nombreTutor;
		private String nombreEstudiante;
		private String estatusDefensa;
		private Date fechaDefensa ;
		
	 public DefensaTeg(String tituloteg, String nombretutor, String nombreEstudiante, String estatusdefensa, Date fechaDefensa) {
			super();
			this.tituloTeg = tituloteg;
			this.nombreTutor = nombretutor;
			this.nombreEstudiante = nombreEstudiante;
			this.estatusDefensa = estatusdefensa;
			this.fechaDefensa= fechaDefensa;}


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
	public String getEstatusDefensa() {
		return estatusDefensa;
	}
	public void setEstatusDefensa(String estatusDefensa) {
		this.estatusDefensa = estatusDefensa;
	}
	public String getNombreEstudiante() {
		return nombreEstudiante;
	}
	public void setNombrestudiante(String nombrestudiante) {
		this.nombreEstudiante = nombrestudiante;
	}
	public Date getFechaDefensa() {
		return fechaDefensa;
	}
	public void setFechaDefensa(Date fechaDefensa) {
		this.fechaDefensa = fechaDefensa;
	}
}

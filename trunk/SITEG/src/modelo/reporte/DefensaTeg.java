package modelo.reporte;

import java.util.Date;

public class DefensaTeg {
	
		private String tituloTeg;
		private String nombreTutor;
		private String nombreEstudiante;
		private String estatusDefensa;
		private Date fechaDefensa ;
		private String programa;
		private String tematica;
		private String area;
		
		public DefensaTeg(String tituloTeg, String nombreTutor,
				String nombreEstudiante, 
				Date fechaDefensa, String programa, String tematica, String area) {
			super();
			this.tituloTeg = tituloTeg;
			this.nombreTutor = nombreTutor;
			this.nombreEstudiante = nombreEstudiante;
			
			this.fechaDefensa = fechaDefensa;
			this.programa = programa;
			this.tematica = tematica;
			this.area = area;
		}
	
		
		
		
		
		





		public DefensaTeg(String tituloTeg, String nombreTutor,
				String nombreEstudiante, String estatusDefensa,
				Date fechaDefensa) {
			super();
			this.tituloTeg = tituloTeg;
			this.nombreTutor = nombreTutor;
			this.nombreEstudiante = nombreEstudiante;
			this.estatusDefensa = estatusDefensa;
			this.fechaDefensa = fechaDefensa;
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
		public String getEstatusDefensa() {
			return estatusDefensa;
		}
		public void setEstatusDefensa(String estatusDefensa) {
			this.estatusDefensa = estatusDefensa;
		}
		public Date getFechaDefensa() {
			return fechaDefensa;
		}
		public void setFechaDefensa(Date fechaDefensa) {
			this.fechaDefensa = fechaDefensa;
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
		
	
}

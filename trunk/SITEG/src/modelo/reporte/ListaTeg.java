package modelo.reporte;

import modelo.Teg;

public class ListaTeg {
		private Teg teg;
		private String nombreEstudiantes;
		private String programa;
		
		public ListaTeg(Teg teg, String nombreEstudiantes, String programa) {
			super();
			this.teg = teg;
			this.nombreEstudiantes = nombreEstudiantes;
			this.programa = programa;
		}
		public Teg getTeg() {
			return teg;
		}
		public void setTeg(Teg teg) {
			this.teg = teg;
		}
		public String getNombreEstudiantes() {
			return nombreEstudiantes;
		}
		public void setNombreEstudiantes(String nombreEstudiantes) {
			this.nombreEstudiantes = nombreEstudiantes;
		}
		public String getPrograma() {
			return programa;
		}
		public void setPrograma(String programa) {
			this.programa = programa;
		}

}



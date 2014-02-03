package modelo.reporte;

import modelo.Teg;

public class ListaTeg {
		private Teg teg;
		private String nombreEstudiantes;
		
		public ListaTeg(Teg teg, String nombreEstudiantes) {
			super();
			this.teg = teg;
			this.nombreEstudiantes = nombreEstudiantes;
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

}



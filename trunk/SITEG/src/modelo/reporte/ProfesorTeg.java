package modelo.reporte;

public class ProfesorTeg {
		private String nombre;
		private String titulo;
		private String cargo;
		private String estatusTeg;


		public ProfesorTeg(String nombre, String titulo, String cargo, String estatusTeg) {
			super();
			this.nombre = nombre;
			this.titulo = titulo;
			this.cargo = cargo;
			this.estatusTeg = estatusTeg;
		}

		public String getNombre() {
			return nombre;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public String getTitulo() {
			return titulo;
		}
		
		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}
		
		public String getCargo() {
			return cargo;
		}
		
		public void setCargo(String cargo) {
			this.cargo = cargo;
		}
		
		public String getEstatusTeg() {
			return estatusTeg;
		}

		public void setEstatusTeg(String estatusTeg) {
			this.estatusTeg = estatusTeg;
		}
	}
package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="archivo")
public class Archivo {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "tipo_archivo")
	private String tipoArchivo;
	
	@Column(name = "documento")
	private byte[] contenidoDocumento = new byte[] {};
	
		@Column(name = "tamano_documento")
	private Long tamanoDocumento = 0l;
		
		
		public Archivo() {
			super();
			// TODO Auto-generated constructor stub
		}


		public Archivo(long id, String nombre, byte[] contenidoDocumento,
				Long tamanoDocumento) {
			super();
			this.id = id;
			this.nombre = nombre;
			this.contenidoDocumento = contenidoDocumento;
			this.tamanoDocumento = tamanoDocumento;
		}


		public long getId() {
			return id;
		}


		public void setId(long id) {
			this.id = id;
		}


		public String getNombre() {
			return nombre;
		}


		public void setNombre(String nombre) {
			this.nombre = nombre;
		}


		public byte[] getContenidoDocumento() {
			return contenidoDocumento;
		}


		public void setContenidoDocumento(byte[] contenido) {
			if (contenido != null) {
				this.setTamanoDocumento(new Long(contenido.length));
			} else {
				contenido = new byte[] {};
			}
			this.contenidoDocumento = contenido;
		}


		public Long getTamanoDocumento() {
			return tamanoDocumento;
		}


		public void setTamanoDocumento(Long tamanoDocumento) {
			this.tamanoDocumento = tamanoDocumento;
		}


		public String getTipoArchivo() {
			return tipoArchivo;
		}


		public void setTipoArchivo(String tipoArchivo) {
			this.tipoArchivo = tipoArchivo;
		}
		
		

}

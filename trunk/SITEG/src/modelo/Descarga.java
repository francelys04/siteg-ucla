package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="descarga")
public class Descarga {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "tipo_archivo")
	private String tipoArchivo;
	
	@Column(name = "documento")
	private byte[] contenidoDocumento = new byte[] {};
	
	@Column(name = "tamano_documento")
	private Long tamanoDocumento = 0l;
	
	@Column(name="estatus")
	private Boolean estatus;
	
	@ManyToOne
	@JoinColumn(name="programa_id", referencedColumnName = "id")
	private Programa programa;
	
			
		public Descarga() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Descarga(long id, String nombre, String descripcion,
				String tipoArchivo, byte[] contenidoDocumento,
				Long tamanoDocumento, Boolean estatus, Programa programa) {
			super();
			this.id = id;
			this.nombre = nombre;
			this.descripcion = descripcion;
			this.tipoArchivo = tipoArchivo;
			this.contenidoDocumento = contenidoDocumento;
			this.tamanoDocumento = tamanoDocumento;
			this.estatus = estatus;
			this.programa = programa;
		}

		public Programa getPrograma() {
			return programa;
		}

		public void setPrograma(Programa programa) {
			this.programa = programa;
		}

		public String getDescripcion() {
			return descripcion;
		}


		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}


		public Boolean getEstatus() {
			return estatus;
		}

		public void setEstatus(Boolean estatus) {
			this.estatus = estatus;
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

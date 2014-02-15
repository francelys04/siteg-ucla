package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="archivo")
public class Archivo {
	
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;
	
	@Column(name = "nombre", length = 100)
	private String nombre;
	
	@Column(name = "descripcion", length = 500)
	private String descripcion;
	
	@Column(name = "documento_tipo", length = 100)
	private String tipoDocumento;
	
	@Column(name = "archivo_tipo", length = 100)
	private String tipoArchivo;
	
	@Column(name = "documento")
	private byte[] contenidoDocumento = new byte[] {};
	
	@Column(name = "documento_tamanio")
	private Long tamanoDocumento = 0l;
	
	@Column(name="estatus")
	private Boolean estatus;
	
	@ManyToOne(optional = false)
	@JoinColumn(name="programa_id", referencedColumnName = "id")
	private Programa programa;
	
			
		public Archivo() {
			super();
			// TODO Auto-generated constructor stub
		}
				
		public Archivo(long id, String nombre, String descripcion,
				String tipoDocumento, String tipoArchivo,
				byte[] contenidoDocumento, Long tamanoDocumento,
				Boolean estatus, Programa programa) {
			super();
			this.id = id;
			this.nombre = nombre;
			this.descripcion = descripcion;
			this.tipoDocumento = tipoDocumento;
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

		public String getTipoDocumento() {
			return tipoDocumento;
		}

		public void setTipoDocumento(String tipoDocumento) {
			this.tipoDocumento = tipoDocumento;
		}

		public String getTipoArchivo() {
			return tipoArchivo;
		}

		public void setTipoArchivo(String tipoArchivo) {
			this.tipoArchivo = tipoArchivo;
		}


	
		

}

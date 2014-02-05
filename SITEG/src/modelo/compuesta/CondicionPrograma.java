package modelo.compuesta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import modelo.Condicion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.id.CondicionProgramaId;

@Entity
@Table(name = "condicion_programa")
@IdClass(CondicionProgramaId.class)
public class CondicionPrograma {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "condicion_id", referencedColumnName = "id")
	private Condicion condicion;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "programa_id", referencedColumnName = "id")
	private Programa programa;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "lapso_id", referencedColumnName = "id")
	private Lapso lapso;
	
	
	
	@Column(name = "valor")
	private int valor;

	public CondicionPrograma(Condicion condicion, Programa programa, Lapso lapso,
			int valor) {
		super();
		this.condicion = condicion;
		this.programa = programa;
		this.lapso = lapso;
		this.valor = valor;
	}

	public CondicionPrograma() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(Condicion condicion) {
		this.condicion = condicion;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public Lapso getLapso() {
		return lapso;
	}

	public void setLapso(Lapso lapso) {
		this.lapso = lapso;
	}
	
}

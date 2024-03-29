package modelo.compuesta;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.compuesta.id.ProgramaRequisitoId;

@Entity
@Table(name = "programa_requisito")
@IdClass(ProgramaRequisitoId.class)
public class ProgramaRequisito {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "programa_id", referencedColumnName = "id")
	private Programa programa;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "requisito_id", referencedColumnName = "id")
	private Requisito requisito;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "lapso_id", referencedColumnName = "id")
	private Lapso lapso;

	public ProgramaRequisito() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProgramaRequisito(Programa programa, Requisito requisito, Lapso lapso) {
		super();
		this.programa = programa;
		this.requisito = requisito;
		this.lapso = lapso;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public Requisito getRequisito() {
		return requisito;
	}

	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}

	public Lapso getLapso() {
		return lapso;
	}

	public void setLapso(Lapso lapso) {
		this.lapso = lapso;
	}
}

package modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "programa_area")
@IdClass(ProgramaAreaId.class)
public class ProgramaArea {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "programa_id", referencedColumnName = "id")
	private Programa programa;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "area_investigacion_id", referencedColumnName = "id")
	private AreaInvestigacion area;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "lapso_id", referencedColumnName = "id")
	private Lapso lapso;

	public ProgramaArea() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProgramaArea(Programa programa, AreaInvestigacion area, Lapso lapso) {
		super();
		this.programa = programa;
		this.area = area;
		this.lapso = lapso;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public AreaInvestigacion getArea() {
		return area;
	}

	public void setArea(AreaInvestigacion area) {
		this.area = area;
	}

	public Lapso getLapso() {
		return lapso;
	}

	public void setLapso(Lapso lapso) {
		this.lapso = lapso;
	}
}

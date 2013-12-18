package modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "profesor_area")
@IdClass(ProfesorAreaId.class)
public class ProfesorArea {

	@Id
	@ManyToOne
	@JoinColumn(name = "profesor_id", referencedColumnName = "cedula")
	private Profesor profesor;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "area_investigacion_id", referencedColumnName = "id")
	private AreaInvestigacion area;
	
	

	public ProfesorArea() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProfesorArea(Profesor profesor, AreaInvestigacion area) {
		super();
		this.profesor = profesor;
		this.area = area;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public AreaInvestigacion getArea() {
		return area;
	}

	public void setArea(AreaInvestigacion area) {
		this.area = area;
	}

}
package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="item_factibilidad")
@IdClass(ItemFactibilidadId.class)
public class ItemFactibilidad {

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "item_evaluacion_id", referencedColumnName = "id")
	private ItemEvaluacion item;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "factibilidad_id", referencedColumnName = "id")
	private Factibilidad factibilidad;
	
	@Column(name ="ponderacion")
	private String ponderacion;

	public ItemFactibilidad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ItemFactibilidad(ItemEvaluacion item, Factibilidad factibilidad,
			String ponderacion) {
		super();
		this.item = item;
		this.factibilidad = factibilidad;
		this.ponderacion = ponderacion;
	}

	public ItemEvaluacion getItem() {
		return item;
	}

	public void setItem(ItemEvaluacion item) {
		this.item = item;
	}

	public Factibilidad getFactibilidad() {
		return factibilidad;
	}

	public void setFactibilidad(Factibilidad factibilidad) {
		this.factibilidad = factibilidad;
	}

	public String getPonderacion() {
		return ponderacion;
	}

	public void setPonderacion(String ponderacion) {
		this.ponderacion = ponderacion;
	}	
}

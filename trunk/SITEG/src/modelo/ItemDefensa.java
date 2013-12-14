package modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item_defensa")
@IdClass(ItemDefensaId.class)
public class ItemDefensa {

	@Id
	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private ItemEvaluacion item;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "defensa_id", referencedColumnName = "id")
	private Defensa defensa;
	
	@Column(name = "ponderacion")
	private String ponderacion;

	public ItemDefensa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ItemDefensa(ItemEvaluacion item,
			Defensa evaluacionDefensa, String ponderacion) {
		super();
		this.item = item;
		this.defensa = evaluacionDefensa;
		this.ponderacion = ponderacion;
	}

	public ItemEvaluacion getItem() {
		return item;
	}

	public void setItem(ItemEvaluacion item) {
		this.item = item;
	}

	public String getPonderacion() {
		return ponderacion;
	}

	public void setPonderacion(String ponderacion) {
		this.ponderacion = ponderacion;
	}

	public Defensa getDefensa() {
		return defensa;
	}

	public void setDefensa(Defensa defensa) {
		this.defensa = defensa;
	}
	
}

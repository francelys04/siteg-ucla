package modelo;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "item_evaluacion")
public class ItemEvaluacion {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private long id;
	
	@Column(name = "nombre", length = 100)
	private String nombre;
	
	@Column(name = "descripcion", length = 600)
	private String descripcion;
	
	@Column(name = "tipo", length = 100)
	private String tipo;
	
	@Column(name = "estatus")
	private Boolean estatus;
	
	@OneToMany(mappedBy = "item")
	private Set<ItemFactibilidad> itemsFactibilidad;
	
	@OneToMany(mappedBy = "item")
	private Set<ItemDefensa> itemsDefensa;
	
	@OneToMany(mappedBy="item")
	private Set<ProgramaItem> programasItems;

	
	public ItemEvaluacion(long id, String nombre, String descripcion, Boolean estatus, String tipo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.tipo = tipo;
	}

	public ItemEvaluacion() {
		super();
		// TODO Auto-generated constructor stub
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

	public Set<ItemFactibilidad> getItemsFactibilidad() {
		return itemsFactibilidad;
	}

	public void setItemsFactibilidad(Set<ItemFactibilidad> itemsFactibilidad) {
		this.itemsFactibilidad = itemsFactibilidad;
	}

	public Set<ItemDefensa> getItemsDefensa() {
		return itemsDefensa;
	}

	public void setItemsDefensa(Set<ItemDefensa> itemsDefensa) {
		this.itemsDefensa = itemsDefensa;
	}

	public Set<ProgramaItem> getProgramasItems() {
		return programasItems;
	}

	public void setProgramasItems(Set<ProgramaItem> programasItems) {
		this.programasItems = programasItems;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}

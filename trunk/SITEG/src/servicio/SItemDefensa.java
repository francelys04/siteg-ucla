package servicio;

import interfazdao.IItemDefensaDAO;

import java.util.List;

import modelo.Defensa;
import modelo.compuesta.ItemDefensa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SItemDefensa {
	@Autowired
	private IItemDefensaDAO interfaceItemDefensa;
	
	public void guardar(ItemDefensa itemdefensa){
		interfaceItemDefensa.save(itemdefensa);
	}
	
	public List<ItemDefensa> buscarItemDefensa(Defensa defensa) {
		List<ItemDefensa> ItemDefensa;
		// ItemFactibilidad = interfaceItemFactibilidad.BuscarFactibilidad(factibilidad);
	    ItemDefensa = interfaceItemDefensa.findByDefensa(defensa);
		return ItemDefensa;
	}

	public void guardarVarios(List<ItemDefensa> items) {
		// TODO Auto-generated method stub
		interfaceItemDefensa.save(items);
	}

}

package servicio;

import interfazdao.IItemFactibilidadDAO;

import java.util.List;

import modelo.Factibilidad;
import modelo.compuesta.ItemFactibilidad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SItemFactibilidad")
public class SItemFactibilidad {

	@Autowired
	private IItemFactibilidadDAO interfaceItemFactibilidad;

	public List<ItemFactibilidad> buscarItemFactibilidad(
			Factibilidad factibilidad) {
		List<ItemFactibilidad> ItemFactibilidad;
		ItemFactibilidad = interfaceItemFactibilidad
				.findByfactibilidad(factibilidad);
		return ItemFactibilidad;
	}

	public void guardar(ItemFactibilidad itemfactibilidad) {
		interfaceItemFactibilidad.save(itemfactibilidad);
	}

}

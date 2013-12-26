package servicio;

import java.util.List;

import interfazdao.IItemFactibilidadDAO;
import modelo.Factibilidad;
import modelo.ItemFactibilidad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SItemFactibilidad {
@Autowired
private IItemFactibilidadDAO interfaceItemFactibilidad;
//Busca los item evaluados de un proyecto
public List<ItemFactibilidad> buscarItemFactibilidad(Factibilidad factibilidad) {
	List<ItemFactibilidad> ItemFactibilidad;
	// ItemFactibilidad = interfaceItemFactibilidad.BuscarFactibilidad(factibilidad);
    ItemFactibilidad = interfaceItemFactibilidad.findByfactibilidad(factibilidad);
	return ItemFactibilidad;
}


}

package servicio;

import java.util.List;

import interfazdao.IProgramaItemDAO;

import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.ProgramaItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SProgramaItem {

	@Autowired
	private IProgramaItemDAO interfaceProgramaItem;
	
	public void guardar (List<ProgramaItem> programasItems){
		interfaceProgramaItem.save(programasItems);
	}

	public List<ItemEvaluacion> buscarItemsEnPrograma(Programa programa,
			Lapso lapso) {
		// TODO Auto-generated method stub
		List<ItemEvaluacion> items;
		items = interfaceProgramaItem.buscarItemsPrograma(programa, lapso);
		return items;
	}

	public void limpiar(List<ProgramaItem> programasItems) {
		// TODO Auto-generated method stub
		interfaceProgramaItem.delete(programasItems);
	}
}

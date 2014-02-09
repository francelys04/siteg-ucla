package servicio;

import java.util.List;

import interfazdao.IItemDefensaDAO;
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

	public void guardarVarios(List<ItemDefensa> items) {
		// TODO Auto-generated method stub
		interfaceItemDefensa.save(items);
	}

}

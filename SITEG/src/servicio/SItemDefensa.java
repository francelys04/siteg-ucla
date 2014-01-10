package servicio;

import interfazdao.IItemDefensaDAO;

import modelo.ItemDefensa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SItemDefensa {
	@Autowired
	private IItemDefensaDAO interfaceItemDefensa;
	
	public void guardar(ItemDefensa itemdefensa){
		interfaceItemDefensa.save(itemdefensa);
	}

}

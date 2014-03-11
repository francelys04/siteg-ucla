package servicio;

import interfazdao.ICondicionDAO;

import java.util.List;

import modelo.Condicion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SCondicion {

	@Autowired
	private ICondicionDAO interfaceCondicion;
	
	public List<Condicion> buscarActivos (){
		List<Condicion> condiciones;
		condiciones  = interfaceCondicion.findByEstatusTrue();
		
		return condiciones;
	}
	
	
}

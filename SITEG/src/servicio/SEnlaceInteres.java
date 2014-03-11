package servicio;

import interfazdao.IEnlaceInteresDAO;

import java.util.List;

import modelo.EnlaceInteres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SEnlaceInteres {
	
	@Autowired
	private IEnlaceInteresDAO interfaceEnlace;
	
	public void guardar(EnlaceInteres enlace){
		interfaceEnlace.save(enlace);
	}
	
	public EnlaceInteres buscarEnlace(long id){
		return interfaceEnlace.findOne(id);
	}
	
	
	public List<EnlaceInteres> buscarActivos(){
		List<EnlaceInteres> enlaces;
		enlaces = interfaceEnlace.findByEstatusTrue();
		return enlaces;
	}
	

}

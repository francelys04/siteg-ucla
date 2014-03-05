package servicio;

import java.util.List;

import interfazdao.ITegEstatusDAO;
import modelo.Actividad;
import modelo.Teg;
import modelo.TegEstatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class STegEstatus {

	@Autowired
	private ITegEstatusDAO interfaceTegEstatus;

	public void guardar(TegEstatus tegEstatus) {
		// TODO Auto-generated method stub
		interfaceTegEstatus.save( tegEstatus);
	}
	
	public List<TegEstatus> buscarEstatusSegunTeg(String nombre,List<Teg> teg){
		List<TegEstatus> tegsEstatus;
		tegsEstatus=interfaceTegEstatus.buscarEstatusSegunTeg(nombre, teg);
		return tegsEstatus;
	}
	
	
	public List<TegEstatus> buscarTegEstatusPorNombre(String nombre) {
		List<TegEstatus> tegEstatus;
		tegEstatus = interfaceTegEstatus.findByNombre(nombre);
		return tegEstatus;
	}
	
}

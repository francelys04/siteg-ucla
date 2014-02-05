package servicio;

import java.util.List;

import interfazdao.ICondicionProgramaDAO;
import interfazdao.ILapsoDAO;

import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.CondicionPrograma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SCondicionPrograma {

	@Autowired
	private ICondicionProgramaDAO interfaceCondicionPrograma;
	@Autowired
	private ILapsoDAO interfaceLapso;
	
	public void guardar(List<CondicionPrograma> condicionesProgramas) {
		interfaceCondicionPrograma.save(condicionesProgramas);
	}
	
	public List<CondicionPrograma> buscarCondicionesPrograma(Programa programa, Lapso lapso){
		List<CondicionPrograma> condicionesProgramas;
		condicionesProgramas = interfaceCondicionPrograma.findByProgramaAndLapso(programa, lapso);
		return condicionesProgramas;
	}

	public List<CondicionPrograma> buscarUltimasCondiciones(Programa programa) {
		// TODO Auto-generated method stub
		List<CondicionPrograma> condiciones;
		condiciones = interfaceCondicionPrograma.buscarCondicionesActuales(programa, interfaceLapso.buscarLapsoVigente());
		return condiciones;
	}
	
	
}

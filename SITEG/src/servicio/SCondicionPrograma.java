package servicio;

import java.util.List;

import interfazdao.ICondicionProgramaDAO;

import modelo.CondicionPrograma;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SCondicionPrograma {

	@Autowired
	private ICondicionProgramaDAO interfaceCondicionPrograma;

	public void guardar(List<CondicionPrograma> condicionesProgramas) {
		interfaceCondicionPrograma.save(condicionesProgramas);
	}
	
	public List<CondicionPrograma> buscarCondicionesPrograma(Programa programa, Lapso lapso){
		List<CondicionPrograma> condicionesProgramas;
		condicionesProgramas = interfaceCondicionPrograma.findByProgramaAndLapso(programa, lapso);
		return condicionesProgramas;
	}
}

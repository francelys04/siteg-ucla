package servicio;

import java.util.List;

import interfazdao.ICronogramaDAO;

import modelo.Cronograma;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SCronograma {

	@Autowired
	private ICronogramaDAO interfaceCronograma;
	
	public void guardar(List<Cronograma> cronogramas){
		interfaceCronograma.save(cronogramas);
	}
	
	public List<Cronograma> buscarCronogramaPorLapsoYPrograma(Programa programa, Lapso lapso){
		List<Cronograma> cronogramas;
		cronogramas = interfaceCronograma.findByProgramaAndLapsoOrderByFechaInicioAsc(programa, lapso);
		return cronogramas;
	}

	public void limpiar(List<Cronograma> cronogramas) {
		// TODO Auto-generated method stub
		interfaceCronograma.delete(cronogramas);
	}
}

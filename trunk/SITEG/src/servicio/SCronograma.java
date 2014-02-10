package servicio;

import java.util.List;

import interfazdao.ICronogramaDAO;
import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.Cronograma;
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
	
	public Cronograma buscarCronogramaPorLapsoProgramaYActividad(Programa programa, Lapso lapso, Actividad actividad){
		Cronograma cronogramaActividad;
		cronogramaActividad = interfaceCronograma.findByProgramaAndLapsoAndActividad(programa, lapso, actividad);
		return cronogramaActividad;
	}
	

	public void limpiar(List<Cronograma> cronogramas) {
		// TODO Auto-generated method stub
		interfaceCronograma.delete(cronogramas);
	}
}

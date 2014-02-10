package interfazdao;

import java.util.List;

import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.Cronograma;
import modelo.compuesta.id.CronogramaId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICronogramaDAO extends JpaRepository<Cronograma, CronogramaId> {

	public List<Cronograma> findByProgramaAndLapsoOrderByFechaInicioAsc(Programa programa, Lapso lapso);
	
	
	/*Busca la actividad que contien la fecha de entrega del TEG*/
	public Cronograma findByProgramaAndLapsoAndActividad(Programa programa, Lapso lapso, Actividad actividad);
	
}

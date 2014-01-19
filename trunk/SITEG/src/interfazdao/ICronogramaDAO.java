package interfazdao;

import java.util.List;

import modelo.Cronograma;
import modelo.CronogramaId;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICronogramaDAO extends JpaRepository<Cronograma, CronogramaId> {

	public List<Cronograma> findByProgramaAndLapsoOrderByFechaInicioAsc(Programa programa, Lapso lapso);
}

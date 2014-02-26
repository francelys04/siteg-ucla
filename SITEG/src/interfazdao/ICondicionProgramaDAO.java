package interfazdao;

import java.util.List;

import modelo.Condicion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.CondicionPrograma;
import modelo.compuesta.id.CondicionProgramaId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICondicionProgramaDAO extends JpaRepository<CondicionPrograma, CondicionProgramaId> {

	public List<CondicionPrograma> findByProgramaAndLapso(Programa programa, Lapso lapso);

	@Query("select cp from CondicionPrograma cp where cp.programa =?1 and cp.lapso =?2")
	public List<CondicionPrograma> buscarCondicionesActuales(Programa programa, Lapso lapso);

	public CondicionPrograma findByCondicionAndProgramaAndLapso(
			Condicion condicion, Programa p, Lapso lapso);
}

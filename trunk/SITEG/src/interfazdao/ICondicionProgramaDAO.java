package interfazdao;

import java.util.List;

import modelo.CondicionPrograma;
import modelo.CondicionProgramaId;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICondicionProgramaDAO extends JpaRepository<CondicionPrograma, CondicionProgramaId> {

	public List<CondicionPrograma> findByProgramaAndLapso(Programa programa, Lapso lapso);
}

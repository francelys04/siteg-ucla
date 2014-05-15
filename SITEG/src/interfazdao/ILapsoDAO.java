package interfazdao;

import java.util.List;

import modelo.Lapso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ILapsoDAO extends JpaRepository<Lapso, Long> {

	public List<Lapso> findByEstatusTrueOrderByNombreAsc();
	
	@Query("select max(cpp.lapso) from CondicionPrograma cpp where cpp.lapso in (select l from Lapso l where l.estatus=true)")
	public Lapso buscarLapsoVigente();
	
	@Query("select max(l.id) from Lapso l where l.estatus=true")
	public Long lapsoActual();

	public Lapso findByNombreAllIgnoreCase(String nombre);

	public List<Lapso> findByEstatusFalseOrderByNombreAsc();
}



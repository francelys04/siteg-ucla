package interfazdao;
import java.util.List;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IRequisitoDAO extends JpaRepository<Requisito, Long> {

	public List<Requisito> findByEstatusTrueOrderByNombreAsc();
	
	public Requisito findById(Long id);

	@Query("select r from Requisito r where r.id not in (select pr.requisito from ProgramaRequisito pr where pr.programa = ?1 and pr.lapso = ?2)")
	public List<Requisito> buscarDisponibles(Programa programa, Lapso lapso);
	
}


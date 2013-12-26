package interfazdao;
import java.util.List;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IRequisitoDAO extends JpaRepository<Requisito, Long> {

	@Query("select e from Requisito e where e.estatus=true")
	public List<Requisito> buscarRequisitosActivos();
	
	public Requisito findById(Long id);

	@Query("select r from Requisito r where r.id not in (select pr.requisito from ProgramaRequisito pr where pr.programa = ?1 and pr.lapso = ?2)")
	public List<Requisito> buscarDisponibles(Programa programa, Lapso lapso);
	
	
}


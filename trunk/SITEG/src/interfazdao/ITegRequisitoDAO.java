package interfazdao;





import java.util.List;


import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.TegRequisito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface  ITegRequisitoDAO extends JpaRepository<TegRequisito, String>{
	
	
	
	@Query("select tr.requisito from TegRequisito tr  where tr.teg = ?1 ")
	public List<Requisito> buscaTegRequisitosSeleccionados(Teg teg);
	
	@Query("select pr.requisito from ProgramaRequisito pr where pr.programa = ?1 and pr.lapso = ?2 and pr.requisito.id not in (select tr.requisito from TegRequisito tr  where tr.teg = ?3)")
	public List<Requisito> buscarRequisitosProgramaDisponibles(Programa programa, Lapso lapso, Teg teg);
	

}

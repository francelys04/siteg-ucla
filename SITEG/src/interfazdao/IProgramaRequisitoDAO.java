package interfazdao;

import java.util.List;

import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.compuesta.ProgramaRequisito;
import modelo.compuesta.id.ProgramaRequisitoId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProgramaRequisitoDAO extends JpaRepository<ProgramaRequisito, ProgramaRequisitoId> {

	@Query("select distinct r from Requisito r where r.id in (select pr.requisito from ProgramaRequisito pr where pr.programa = ?1 and pr.lapso = ?2)")
	public List<Requisito> buscarRequisitosPrograma(Programa programa, Lapso lapso);
	
	@Query ("select pr.requisito from ProgramaRequisito pr where pr.programa = ?1 and pr.lapso = ?2")
	public List<Requisito> buscarRequistos(Programa programa, Lapso lapso);
	
}

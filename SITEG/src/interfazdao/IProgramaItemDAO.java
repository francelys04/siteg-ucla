package interfazdao;

import java.util.List;

import modelo.AreaInvestigacion;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.ProgramaItem;
import modelo.ProgramaItemId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProgramaItemDAO extends JpaRepository<ProgramaItem, ProgramaItemId> {

	@Query("select distinct i from ItemEvaluacion i where i.id in (select pi.item from ProgramaItem pi where pi.programa = ?1 and pi.lapso = ?2)")
	public List<ItemEvaluacion> buscarItemsPrograma(Programa programa, Lapso lapso);
}
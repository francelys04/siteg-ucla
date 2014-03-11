package interfazdao;

import java.util.List;

import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.ProgramaItem;
import modelo.compuesta.id.ProgramaItemId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProgramaItemDAO extends JpaRepository<ProgramaItem, ProgramaItemId> {

	@Query("select distinct i from ItemEvaluacion i where i.id in (select pi.item from ProgramaItem pi where pi.programa = ?1 and pi.lapso = ?2)")
	public List<ItemEvaluacion> buscarItemsPrograma(Programa programa, Lapso lapso);
}

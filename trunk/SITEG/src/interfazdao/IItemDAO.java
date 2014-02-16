package interfazdao;


import java.util.List;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IItemDAO extends JpaRepository<ItemEvaluacion, String> {

	@Query("select i from ItemEvaluacion i where i.estatus=true")
	public List<ItemEvaluacion> buscarItemsActivos();

	public ItemEvaluacion findById(long codigo);
	
	@Query("select i from ItemEvaluacion i where i.id not in (select pi.item from ProgramaItem pi where pi.programa = ?1 and pi.lapso = ?2)")
	public List<ItemEvaluacion> buscarDisponibles(Programa programa, Lapso lapso);
	
	@Query("select i from ItemEvaluacionir where i.estatus=true order by i.tipo, i.nombre asc")
	public List<ItemEvaluacion> itemsPorNombre();
	
}


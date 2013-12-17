package interfazdao;


import java.util.List;

import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IActividadDAO extends JpaRepository<Actividad, Long> {

	@Query("select a from Actividad a where a.estatus=true")
	public List<Actividad> buscarActividadesActivos();

	public Actividad findById(long id);
	@Query("select a from Actividad a where a.id not in (select cr.actividad from Cronograma cr where cr.programa = ?1 and cr.lapso = ?2)")
	public List<Actividad> buscarActividadDisponible(Programa programa,
			Lapso lapso);

	public Actividad findByNombre(String label);
	
}


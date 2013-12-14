package interfazdao;


import java.util.List;

import modelo.Actividad;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IActividadDAO extends JpaRepository<Actividad, Long> {

	@Query("select a from Actividad a where a.estatus=true")
	public List<Actividad> buscarActividadesActivos();

	public Actividad findById(long id);
	
}


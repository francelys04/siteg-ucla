package interfazdao;


import java.util.List;

import modelo.AreaInvestigacion;

import modelo.Profesor;
import modelo.ProfesorArea;
import modelo.ProfesorAreaId;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProfesorAreaDAO extends JpaRepository<ProfesorArea, ProfesorAreaId> {
	

	//Grid de la derecha
		@Query("select distinct a from AreaInvestigacion a where a.id in (select aa.area from ProfesorArea aa where aa.profesor = ?1)")
		public List<AreaInvestigacion> buscarAreasProfesor(Profesor profesor);

}

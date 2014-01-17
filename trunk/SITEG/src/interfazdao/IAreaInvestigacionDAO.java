package interfazdao;


import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAreaInvestigacionDAO extends JpaRepository<AreaInvestigacion, Long> {


	 public AreaInvestigacion findByNombre(String nombre);

	public List<AreaInvestigacion> findByEstatusTrue();

	public AreaInvestigacion findById(long codigo);
	
	//Grid de la izquierda	
	@Query("select a from AreaInvestigacion a where a.id not in (select aa.area from ProgramaArea aa where aa.programa = ?1 and aa.lapso = ?2)")
	public List<AreaInvestigacion> buscarDisponibles(Programa programa, Lapso lapso);
	
	@Query("select a from AreaInvestigacion a where a.id in (select aa.area from ProgramaArea aa where aa.programa = ?1 and aa.lapso = ?2)")
	public List<AreaInvestigacion> buscarAreasPrograma(Programa programa, Lapso lapso);
	
	
	
	
}



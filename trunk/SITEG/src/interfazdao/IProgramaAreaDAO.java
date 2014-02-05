package interfazdao;

import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.ProgramaArea;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProgramaAreaDAO extends JpaRepository<ProgramaArea, String>{

	//Grid de la derecha
	@Query("select distinct a from AreaInvestigacion a where a.id in (select aa.area from ProgramaArea aa where aa.programa = ?1 and aa.lapso = ?2)")
	public List<AreaInvestigacion> buscarAreasPrograma(Programa programa, Lapso lapso);
	
	/*
	 * Para listar las areas por Programa*/
	@Query("select distinct a from AreaInvestigacion a where a.id in (select aa.area from ProgramaArea aa where aa.programa = ?1)")
	public List<AreaInvestigacion> buscarAreasPrograma(Programa programa);
	
	
	//@Query("select a  from ProgramaArea a where a.area in (select areas from Profesor p where p.cedula  in (select p from Profesor p where p.usuario = ?1))")
	//public Programa BuscarPrograma (Usuario usuario);
	
	
}

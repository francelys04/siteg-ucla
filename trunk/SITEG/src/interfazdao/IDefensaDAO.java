package interfazdao;

import java.util.Date;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IDefensaDAO extends JpaRepository<Defensa, Long> {

	Defensa findByTeg(Teg teg);

	@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.estatus=?1 and teg.fecha between ?2 and ?3)")
	public List<Defensa> buscarDefensaTegSegunEstatus(String estatus,Date fechaInicio,Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.estatus=?1 and teg.tematica=?2 and teg.fecha between ?3 and ?4 )")
	public List<Defensa> buscarDefensaTegSegunEstatusTematica(String estatus,Tematica tematica,Date fechaInicio,Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.estatus=?1 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?2) and teg.fecha between ?3 and ?4 )")
	public List<Defensa> buscarDefensaTegSegunEstatusArea(String estatus,AreaInvestigacion area,Date fechaInicio,Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.estatus=?1 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?2)) and teg.fecha between ?3 and ?4 )")
	public List<Defensa> buscarDefensaTegSegunEstatusPrograma(String estatus,Programa programa,Date fechaInicio,Date fechaFin);
	
	public List<Defensa> findByFechaBetween(Date fechaInicio, Date fechaFin);
	
	/*********************************Querys Defensas para todos los estatus y fechas de defensas ******************************************************************/
	@Query("select defensa from Defensa defensa where defensa.fecha between ?1 and ?2")
	public List<Defensa> buscarDefensaTeg(Date fechaInicio, Date fechaFin);
	
	//@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.tematica=?1) and defensa.fecha between ?2 and ?3 ")
	//public List<Defensa> buscarDefensaTegSegunTematica(Programa programa,Tematica tematica,Date fechaInicio, Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.teg in (SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica=?2 AND teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1) AND fecha between ?3 AND ?4")
	public List<Defensa> buscarDefensaTegSegunTematica(Programa programa,Tematica tematica,Date fechaInicio, Date fechaFin);
	
	
	
	//@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?1)) )and defensa.fecha between ?2 and ?3 ")
	//public List<Defensa> buscarDefensaTegSegunPrograma(Programa programa, Date fechaInicio, Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.teg in (SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1) AND fecha between ?2 AND ?3") 
	public List<Defensa> buscarDefensaTegSegunPrograma(Programa programa, Date fechaInicio, Date fechaFin);
	
	
	
	
	//@Query("select DISTINCT defensa from Defensa defensa where defensa.teg in (select DISTINCT teg from Teg teg INNER JOIN teg.estudiantes estudiante where teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?4) AND estudiante.programa=?4 AND teg.tematica.areaInvestigacion=?1)and defensa.fecha between ?2 and ?3")
    //public List<Defensa> buscarDefensaTegSegunArea(AreaInvestigacion area, Date fechaInicio, Date fechaFin, Programa programa);
	
	@Query("select defensa from Defensa defensa where defensa.teg in (SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion=?1 AND teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?4) AND estudiante.programa=?4) AND fecha between ?2 AND ?3")
	public List<Defensa> buscarDefensaTegSegunArea(AreaInvestigacion area, Date fechaInicio, Date fechaFin, Programa programa);
	
	
	
	
	/*********************************Querys con estatus de defensa y fecha de defensas**************************************/
	
	@Query("select defensa from Defensa defensa where defensa.estatus=?1 and defensa.fecha between ?2 and ?3")
	public List<Defensa> buscarDefensaTegSegunEstatus2(String estatus,Date fechaInicio, Date fechaFin);
	

//	@Query("select defensa from Defensa defensa where defensa.estatus=?1 and defensa.teg in (select teg from Teg teg where teg.tematica=?2) and defensa.fecha between ?3 and ?4 ")
//	public List<Defensa> buscarDefensaTegSegunEstatusTematica2(String estatus,Tematica tematica,Date fechaInicio,Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.estatus=?5 and defensa.teg in (SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica=?2 AND teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1) AND fecha between ?3 AND ?4")
	public List<Defensa> buscarDefensaTegSegunEstatusTematica2(Programa programa,Tematica tematica,Date fechaInicio, Date fechaFin, String estatus);

	//	@Query("select defensa from Defensa defensa where defensa.estatus=?1 and defensa.fecha between ?3 and ?4 and defensa.teg in (select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?2)))")
    //	public List<Defensa> buscarDefensaTegSegunEstatusPrograma2(String estatus,Programa programa,Date fechaInicio,Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.estatus=?4 and defensa.teg in (SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1) AND fecha between ?2 AND ?3") 
	public List<Defensa> buscarDefensaTegSegunEstatusPrograma2(Programa programa,Date fechaInicio,Date fechaFin,String estatus);
	
//	@Query("select defensa from Defensa defensa where defensa.estatus=?1 and defensa.fecha between ?3 and ?4 and defensa.teg in (select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?2) )")
//	public List<Defensa> buscarDefensaTegSegunEstatusArea2(String estatus,AreaInvestigacion area,Date fechaInicio,Date fechaFin);
	
	@Query("select defensa from Defensa defensa where defensa.estatus=?5 and defensa.teg in (SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion=?1 AND estudiante.programa=?4) AND fecha between ?2 AND ?3")
	public List<Defensa>  buscarDefensaTegSegunEstatusArea2(AreaInvestigacion area, Date fechaInicio, Date fechaFin, Programa programa, String estatus);

	
	
}

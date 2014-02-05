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
	
	@Query("select defensa from Defensa defensa where defensa.teg.estatus=?1 and defensa.teg.fecha between ?2 and ?3")
	public List<Defensa> buscarDefensaTegSegunEstatus2(String estatus,Date fechaInicio,Date fechaFin);

	//
	@Query("select defensa from Defensa defensa where defensa.teg.fecha between ?1 and ?2")
	public List<Defensa> buscarDefensaTeg2(Date fechaInicio, Date fechaFin);
	
	@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.tematica=?1 and teg.fecha between ?2 and ?3 )")
	public List<Defensa> buscarDefensaTegSegunTematica(Tematica tematica,Date fechaInicio, Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?1)) and teg.fecha between ?2 and ?3 )")
	public List<Defensa> buscarDefensaTegSegunPrograma(Programa programa, Date fechaInicio, Date fechaFin);

	@Query("select defensa from Defensa defensa where defensa.teg in (select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and teg.fecha between ?2 and ?3 )")
	public List<Defensa> buscarDefensaTegSegunArea(AreaInvestigacion area, Date fechaInicio, Date fechaFin);

	
	
}

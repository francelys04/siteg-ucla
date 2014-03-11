package interfazdao;

import java.util.Date;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Tematica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ISolicitudTutoriaDAO extends JpaRepository<SolicitudTutoria, Long> {

	public List<SolicitudTutoria> findByEstatusLikeAndProfesor(
			String estatusPorRevisar, Profesor profesor);
	
	public SolicitudTutoria findByEstatusLikeAndEstudiantes(
			String estatusDefensaAsignada, Estudiante estudiante);
	
	public List<SolicitudTutoria> findByEstatusLike(
			String estatusDefensaAsignada);
	
	public List<SolicitudTutoria> findByEstudiantes(Estudiante estudiante);

	public List<SolicitudTutoria> findByTematicaAndEstatusAndFechaBetweenOrderByProfesorDesc(
			Tematica tematica, String estatus2, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndTematicaAndEstatusAndFechaBetween(
			Profesor profesor, Tematica tematica, String estatus2,
			Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByEstatusAndFechaBetweenOrderByProfesorDesc(
			String estatus2, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndEstatusAndFechaBetween(
			Profesor profesor, String estatus2, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndTematicaAndFechaBetween(
			Profesor profesor, Tematica tematica, Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndFechaBetween(
			Profesor profesor, Date fechaInicio, Date fechaFin);

	@Query("select count(e) from SolicitudTutoria e where e.profesor=?1")
	public long countByProfesor(Profesor profesor);

	@Query("select count(e) from SolicitudTutoria e where e.profesor=?1 and e.tematica=?2")
	public long countByProfesorAndTematica(Profesor profesor, Tematica tematica);
	
	/**********************************Querys para buscar la lista de solicitudes dado todos los programas y un estatus especifico ***********************************************************/
	@Query("select solicitud from SolicitudTutoria solicitud where fecha between ?1 and ?2 Order by solicitud.tematica.id")
	public List<SolicitudTutoria> buscarSolicitudPorVariosProgramaUnEstatus(Date fechaInicio,Date fechaFin);
	
	/*********************************** Querys para buscar la lista de solicitud dado varias areas, un programa, un estatus, fecha inicio y fecha fin ******************************/	
	@Query("select solicitud from SolicitudTutoria solicitud where estatus=?1 and solicitud.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?2)) and fecha between ?2 and ?3")
	public List<SolicitudTutoria> buscarSolicitudPorProgramaVariasAreasUnEstatus1(String estatus, Programa programa,Date fechaInicio,Date fechaFin);
	
	@Query("select solicitud from SolicitudTutoria solicitud where solicitud.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?1)) and fecha between ?2 and ?3")
	public List<SolicitudTutoria> buscarSolicitudPorProgramaVariasAreasUnEstatus(Programa programa,Date fechaInicio,Date fechaFin);
	
	@Query("select solicitud from SolicitudTutoria solicitud where solicitud.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?1)) and fecha between ?2 and ?3")
	public List<SolicitudTutoria>buscarSolicitudPorProgramaVariasAreas(Programa programa,Date fechaInicio,Date fechaFin);
	/******************************* Querys para buscar la lista de solicitud dado una tematica un estatus, fecha inicio y fecha fin ******************************/	
	@Query("select solicitud from SolicitudTutoria solicitud where solicitud.tematica=?1 and fecha between ?2 and ?3")
	public List<SolicitudTutoria> buscarSolicitudPorFechayEstatus(Tematica tematica,Date fechaInicio,Date fechaFin);
	
	/********************************** Query para buscar la lista de solicitud dado a una area de investigacion*******************************************************************/
	@Query("select solicitud from SolicitudTutoria solicitud where solicitud.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and fecha between ?2 and ?3")
	public List<SolicitudTutoria> buscarSolicitudAreaestatus1(AreaInvestigacion areaInvestigacion, Date fechaInicio, Date fechaFin);
	
	@Query("select solicitud from SolicitudTutoria solicitud where  solicitud.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and fecha between ?2 and ?3")
	public List<SolicitudTutoria> buscarSolicitudAreaestatus(AreaInvestigacion areaInvestigacion, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByFechaBetweenOrderByProfesorDesc(Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findByTematicaAndFechaBetweenOrderByProfesorDesc(
			Tematica tematica, Date fechaInicio, Date fechaFin);
	
	public List<SolicitudTutoria> findByTematicaAreaInvestigacionAndFechaBetweenOrderByProfesorDesc(
			AreaInvestigacion areaI, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndFechaBetween(
			List<Estudiante> estudiantes, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndTematicaAreaInvestigacionAndFechaBetween(
			List<Estudiante> estudiantes, AreaInvestigacion areaI,
			Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndTematicaAreaInvestigacionAndFechaBetween(
			Profesor profesor, AreaInvestigacion areaI, Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndProfesorAndFechaBetween(
			List<Estudiante> estudiantes, Profesor profesor, Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndProfesorAndTematicaAreaInvestigacionAndFechaBetween(
			List<Estudiante> estudiantes, Profesor profesor,
			AreaInvestigacion areaI, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByTematicaAndTematicaAreaInvestigacionAndFechaBetweenOrderByProfesorDesc(
			Tematica tematicaI, AreaInvestigacion areaI, Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndTematicaAndFechaBetween(
			List<Estudiante> estudiantes, Tematica tematicaI, Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndTematicaAndTematicaAreaInvestigacionAndFechaBetween(
			List<Estudiante> estudiantes, Tematica tematicaI,
			AreaInvestigacion areaI, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByTematicaAndProfesorAndTematicaAreaInvestigacionAndFechaBetween(
			Tematica tematicaI, Profesor profesor, AreaInvestigacion areaI,
			Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndTematicaAndProfesorAndFechaBetween(
			List<Estudiante> estudiantes, Tematica tematicaI,
			Profesor profesor, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findDistinctByEstudiantesInAndTematicaAndTematicaAreaInvestigacionAndProfesorAndFechaBetween(
			List<Estudiante> estudiantes, Tematica tematicaI,
			AreaInvestigacion areaI, Profesor profesor, Date fechaInicio,
			Date fechaFin);

}

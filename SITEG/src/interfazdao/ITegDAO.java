package interfazdao;

import java.util.Date;
import java.util.List;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITegDAO extends JpaRepository<Teg, Long> {

	// Buscar tegs por profesores
	public List<Teg> findByTutor(List<Profesor> profesores);

	public List<Teg> findByProfesores(Profesor p);

	/* Busca los teg asociados al Estudiante */
	public List<Teg> findByEstudiantes(Estudiante estudiante);

	/* Busca los teg asociados al Profesor */
	public List<Teg> findByTutor(Profesor profesor);

	/*
	 * Busca un teg asociado a un estudiante que tengan estatus avances
	 * finalizados
	 */
	public Teg findByEstatusLikeAndEstudiantes(
			String estatusAvancesFinalizados, Estudiante estudiante);

	/*
	 * Busca un teg asociado a un estudiante que tengan estatus avances
	 * finalizados
	 */
	public List<Teg> findByEstatus(String estatusProyectoFactible);

	public List<Teg> findByEstatusOrEstatus(String estatusProyectoFactible,
			String estatusProyectoEnDesarrollo);

	/* Busca ultim teg registrado */
	@Query("select max(t.id) from Teg t")
	public Long ultimoTegRegistrado();

	public Teg findByEstatusAndEstudiantes(String string, Estudiante estudiante);

	public List<Teg> findByEstatusOrEstatusLikeAndTutor(String estatus1,
			String estatus2, Profesor profesor);

	// Busca los tegs de los estudiantes del programa dado y con estatus dado
	@Query("select t from Teg t where t.estatus=?2 and t.estudiantes in (select e from Estudiante e where e.programa=?1)")
	public List<Teg> buscarTegsPorPrograma(Programa programa, String string);

	public List<Teg> findByEstatusAndEstudiantesInOrderByIdAsc(String estatus,
			List<Estudiante> list);

	public List<Teg> findByTutorAndTematicaAndFechaBetweenOrderByFechaAsc(
			Profesor buscarProfesorPorCedula, Tematica tematica,
			Date fechaInicio, Date fechaFin);

	public List<Teg> findByTutorAndTematicaAndEstatusAndFechaBetweenOrderByFechaAsc(
			Profesor profesor, Tematica tematica, String estatus2,
			Date fechaInicio, Date fechaFin);

	public List<Teg> findByTutorAndFechaBetweenOrderByFechaAsc(
			Profesor profesor, Date fechaInicio, Date fechaFin);

	public List<Teg> findByTutorAndEstatusAndFechaBetweenOrderByFechaAsc(
			Profesor profesor, String estatus2, Date fechaInicio, Date fechaFin);

	@Query("select teg from Teg teg where teg.estatus=?1 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?2 ) and fecha between ?3 and ?4 ")
	public List<Teg> buscarUltimasTematicas(String estatus,
			AreaInvestigacion area, Date fechaInicio, Date fechaFin);

	public List<Teg> findByTematicaAndFechaBetween(Tematica tematica,
			Date fechaInicio, Date fechaFin);

	/************************************ Inicio de Query para el Reporte Trabajos *********************************************/

	/**
	 * Querys para buscar la lista de teg dado un programa, una tematica, un estatus, fecha
	 * inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND teg.estatus=?2 AND teg.tematica=?3 AND fecha between ?4 AND ?5 ")
	public List<Teg> buscarTegPorTematicaEstatusPrograma(Programa programa, String estatus,
			Tematica tematica, Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado un programa, un area, estatus, fecha inicio y
	 * fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND teg.tematica.areaInvestigacion=?2 AND teg.estatus=?3 AND fecha between ?4 AND ?5 ")
	public List<Teg> buscarTegPorAreaEstatusPrograma(Programa programa, AreaInvestigacion area1, String estatus,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado un programa, un area, fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND teg.tematica.areaInvestigacion=?2 AND fecha between ?3 AND ?4")
	public List<Teg> buscarTegPorAreaPrograma(Programa programa, AreaInvestigacion area1,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado un programa, un area, Varios estatus,
	 * fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND teg.tematica.areaInvestigacion=?2 AND (teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 or teg.estatus=?8 or teg.estatus=?9 or teg.estatus=?10) AND fecha between ?11 AND ?12 ")
	public List<Teg> buscarTegPorVariosEstatusAreaPrograma(Programa programa, AreaInvestigacion area1,
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin);
	
	/**
	 * Querys para buscar la lista de teg dado un programa, una tematica, Varios estatus,
	 * fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND teg.tematica=?2 AND (teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 or teg.estatus=?8 or teg.estatus=?9 or teg.estatus=?10) AND fecha between ?11 AND ?12 ")
	public List<Teg> buscarTegPorVariosEstatusTematicaPrograma(Programa programa, Tematica tematica, 
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado un programa, un area, fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND teg.tematica=?2 AND fecha between ?3 AND ?4")
	public List<Teg> buscarTegPorTematicaPrograma(Programa programa, Tematica tematica,
			Date fechaInicio, Date fechaFin);
	
	/**
	 * Querys para buscar la lista de teg dado varias areas, un programa, un
	 * estatus, fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND teg.estatus=?2 AND fecha between ?3 AND ?4")
	public List<Teg> buscarTegPorProgramaEstatus(Programa programa, 
			String estatus, Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg un programa, Varios
	 * estatus, fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND (teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 or teg.estatus=?8 or teg.estatus=?9) AND fecha between ?10 AND ?11 ")
	public List<Teg> buscarTegPorProgramaVariosEstatus(Programa programa, 
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg un programa, fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND fecha between ?2 AND ?3 ")
	public List<Teg> buscarTegPorPrograma(Programa programa, 
			Date fechaInicio, Date fechaFin);
	
	/**
	 * Querys para buscar la lista de tg dado todos los programas y un estatus
	 **/
	@Query("select teg from Teg teg where teg.estatus=?1 and fecha between ?2 and ?3 Order by teg.tematica.id")
	public List<Teg> buscarTegPorEstatus(String estatus,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado todos los programas y Varios
	 * estatus
	 **/
	@Query("select teg from Teg teg where (teg.estatus=?1 or teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 or teg.estatus=?8) and fecha between ?9 and ?10 Order by teg.tematica.id")
	public List<Teg> buscarTegPorVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado todos los programas y Varios
	 * estatus
	 **/
	@Query("select teg from Teg teg where fecha between ?1 and ?2 Order by teg.tematica.id")
	public List<Teg> buscarTegPorFecha(Date fechaInicio, Date fechaFin);
	
	/**
	 * Querys para buscar la lista de teg dado una tematica un estatus, fecha
	 * inicio y fecha fin
	 **/
	@Query("select teg from Teg teg where teg.tematica=?1 and teg.estatus=?2 and fecha between ?3 and ?4 ")
	public List<Teg> buscarTegPorTematicaEstatus(Tematica tematica, String estatus,
			Date fechaInicio, Date fechaFin);
	
	/**
	 * Querys para buscar la lista de teg dado una tematica, varios estatus, fecha
	 * inicio y fecha fin
	 **/
	@Query("select teg from Teg teg where (teg.estatus=?1 or teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 or teg.estatus=?8) and teg.tematica=?9 and fecha between ?10 and ?11 ")
	public List<Teg> buscarTegPorTematicaVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, String estatus8, Tematica tematica,
			Date fechaInicio, Date fechaFin);
	
	/**
	 * Querys para buscar la lista de teg dado una area, un estatus, fecha
	 * inicio y fecha fin
	 **/
	@Query("select teg from Teg teg where teg.tematica.areaInvestigacion=?1 and teg.estatus=?2 and fecha between ?3 and ?4 ")
	public List<Teg> buscarTegPorAreaEstatus(AreaInvestigacion area1, String estatus,
			Date fechaInicio, Date fechaFin);
	
	/**
	 * Querys para buscar la lista de teg dado una area, varios estatus, fecha
	 * inicio y fecha fin
	 **/
	@Query("select teg from Teg teg where (teg.estatus=?1 or teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 or teg.estatus=?8) and teg.tematica.areaInvestigacion=?9 and fecha between ?10 and ?11 ")
	public List<Teg> buscarTegPorAreaVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, String estatus8, AreaInvestigacion area1,
			Date fechaInicio, Date fechaFin);
	
	/************************************ Fin de Query para el Reporte Trabajos *********************************************/
	
	/************************************ Inicio de Query para el Reporte Profesor ********************************************/

	/**
	 * Querys para buscar la lista de teg dado una tematica un estatus, fecha
	 * inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 and teg.tematica=?2 and fecha between ?3 and ?4 ")
	public List<Teg> buscarTegPorFechaTematicaPrograma(Programa programa,
			Tematica tematica, Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado un area, fecha inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 and teg.tematica.areaInvestigacion=?2 and fecha between ?3 and ?4 ")
	public List<Teg> buscarTegPorFechaAreaPrograma(Programa programa,
			AreaInvestigacion area1, Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado varias areas, un programa, fecha
	 * inicio y fecha fin
	 **/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND fecha BETWEEN ?2 AND ?3")
	public List<Teg> buscarTegPorFechayPrograma(Programa programa,
			Date fechaInicio, Date fechaFin);


	/** Querys para buscar la lista de tg dado todos los programas **/
	@Query("select teg from Teg teg where fecha between ?1 and ?2 Order by teg.tematica.id")
	public List<Teg> buscarTodosTegporFecha(Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado una tematica un estatus, fecha
	 * inicio y fecha fin
	 **/
	@Query("select teg from Teg teg where teg.tematica=?1 and fecha between ?2 and ?3 ")
	public List<Teg> buscarTegPorFechaTematica(Tematica tematica,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado un area, fecha inicio y fecha fin
	 **/
	@Query("select teg from Teg teg where teg.tematica.areaInvestigacion=?1 and fecha between ?2 and ?3 ")
	public List<Teg> buscarTegPorFechaArea(AreaInvestigacion area1,
			Date fechaInicio, Date fechaFin);

	/************************************ Fin de Query para el Reporte Profesor ********************************************/
	
	
	/************************************ Query para el Reporte TEG ********************************************/
	/**
	 * Querys para buscar la lista de teg dado todos los programas, un área, una
	 * tematica y un estatus
	 **/
	@Query("select teg from Teg teg where teg.tematica=?1 and teg.estatus=?2 and fecha between ?3 and ?4 Order by teg.tematica.id")
	public List<Teg> buscarTodosProgramasUnAreaUnaTematicaUnEstatus(
			Tematica tematica, String estatus, Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado todos los programas, un área, una
	 * tematica y todos los estatus
	 **/
	@Query("select teg from Teg teg where teg.tematica=?1 and fecha between ?2 and ?3 Order by teg.tematica.id")
	public List<Teg> buscarTodosProgramasUnAreaUnaTematicaTodosEstatus(
			Tematica tematica, Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado todos los programas, un área,
	 * todas las tematicas y un estatus
	 **/
	@Query("select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and teg.estatus=?2 and fecha between ?3 and ?4 Order by teg.tematica.id")
	public List<Teg> buscarTodosProgramasUnAreaTodasTematicaUnEstatus(
			AreaInvestigacion areaInvestigacion, String estatus,
			Date fechaInicio, Date fechaFin);

	/**
	 * Querys para buscar la lista de teg dado todos los programas, un área,
	 * todas las tematicas y todos los estatus
	 **/
	@Query("select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and fecha between ?2 and ?3 Order by teg.tematica.id")
	public List<Teg> buscarTodosProgramasUnAreaTodasTematicaTodosEstatus(
			AreaInvestigacion areaInvestigacion, Date fechaInicio, Date fechaFin);


	/******************************** Inicio Reporte Tematicas mas solicitadas ***********************************/
	/****************************************** Por Programa ***********************************************/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 AND fecha BETWEEN ?2 AND ?3 and (teg.estatus=?4 or teg.estatus=?5) Order by teg.tematica.id")
	public List<Teg> buscarUltimasTematicasProgramaDosEstatus(Programa programa,
			Date fechaInicio, Date fechaFin, String estatusProyectoTeg1,
			String estatusProyectoTeg2);

	/****************************************** Por Area *****************************************************/
	@Query("select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and fecha between ?2 and ?3 and (teg.estatus=?4 or teg.estatus=?5) Order by teg.tematica.id")
	public List<Teg> buscarUltimasTematicasAreaDosEstatus(AreaInvestigacion area,Date fechaInicio, Date fechaFin,
			String estatus1, String estatus2);

	/****************************************** Por Programa y Area *******************************************/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 and teg.tematica.areaInvestigacion=?2 and fecha between ?3 and ?4 and (teg.estatus=?5 or teg.estatus=?6) Order by teg.tematica.id")
	public List<Teg> buscarUltimasTematicasProgramaAreaDosEstatus(
			Programa programa, AreaInvestigacion area1, Date fechaInicio,
			Date fechaFin, String estatusProyectoTeg1,
			String estatusProyectoTeg2);

	/****************************************** Todos ****************************************************/
	@Query("select teg.tematica.id from Teg teg where (teg.estatus=?1 or teg.estatus=?2) and fecha between ?3 and ?4 Order by teg.tematica.id")
	public List<Long> buscarUltimasDosEstatus(String estatusProyectoTeg1,
			String estatusProyectoTeg2, Date fechaInicio, Date fechaFin);

	/******************************* Querys con las 5 tematicas mas solicitadas segun sus estatus *********************************/

	@Query("select teg from Teg teg where (teg.estatus=?1 or teg.estatus=?2) and teg.tematica in ?3 and fecha between ?4 and ?5")
	public List<Teg> buscarUltimasOrdenadasEstatus(String estatusProyectoTeg1,
			String estatusProyectoTeg2, List<Tematica> tematicas,
			Date fechaInicio, Date fechaFin);
	/****************************** Fin Reporte Tematicas mas solicitadas ****************************************************/
	@Query("select count(teg) from Teg teg where teg.estatus=?1 and teg.tematica=?2 and fecha between ?3 and ?4")
	public long countByEstatus(String estatusProyectoTeg, Tematica tematica,
			Date fechaInicio, Date fechaFin);

	/*******************************
	 * Querys para buscar la lista de teg dado una tematica, fecha inicio y
	 * fecha fin
	 ******************************/
	@Query("select teg from Teg teg where teg.estatus=?1 and fecha between ?2 and ?3 ")
	public List<Teg> buscarTegPorFecha(String estatus, Date fechaInicio,
			Date fechaFin);

	/*******************************
	 * Querys para buscar la lista de teg dado una tematica, Varios estatus,
	 * fecha inicio y fecha fin
	 ******************************/
	@Query("select teg from Teg teg where (teg.estatus=?1 or teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 and teg.tematica=?8) and fecha between ?9 and ?10")
	public List<Teg> buscarTegporFechayVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, Tematica tematica,
			Date fechaInicio, Date fechaFin);

	/***********************************
	 * Querys para buscar la lista de teg dado varias areas, un programa, un
	 * estatus, fecha inicio y fecha fin
	 ******************************/
	@Query("select teg from Teg teg where teg.estatus=?1 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?2)) and fecha between ?3 and ?4")
	public List<Teg> buscarTegPorProgramaUnEstatus(String estatus,
			Programa programa, Date fechaInicio, Date fechaFin);

	/**********************************
	 * Querys para buscar la lista de teg dado varias areas, un programa, Varios
	 * estatus, fecha inicio y fecha fin
	 ******************************/
	@Query("select teg from Teg teg where teg.estatus=?1 or teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?8)) and fecha between ?9 and ?10")
	public List<Teg> buscarTegPorProgramaVariasAreasVariosEstatus(
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7,
			Programa programa, Date fechaInicio, Date fechaFin);

	/**********************************
	 * Querys para buscar la lista de tg dado todos los programas y Varios
	 * estatus
	 ***********************************************************/
	@Query("select teg from Teg teg where teg.estatus=?1 or teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 or teg.estatus=?6 or teg.estatus=?7 and fecha between ?8 and ?9 Order by teg.tematica.id")
	public List<Teg> buscarTegPorVariosProgramasVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, Date fechaInicio, Date fechaFin);

	/********************************** Query para buscar la lista de teg dado a una area de investigacion *******************************************************************/
	@Query("select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and fecha between ?2 and ?3")
	public List<Teg> buscarTegArea(AreaInvestigacion areaInvestigacion,
			Date fechaInicio, Date fechaFin);

	/********************************** Query para buscar la lista de teg dado a una area de investigacion *******************************************************************/
	@Query("select teg from Teg teg where  teg.estatus=?4 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and fecha between ?2 and ?3")
	public List<Teg> buscarTegAreaestatus(AreaInvestigacion areaInvestigacion,
			Date fechaInicio, Date fechaFin, String estatus);

	@Query("select teg from Teg teg where  teg.estatus=?2 or teg.estatus=?3 or teg.estatus=?4 or teg.estatus=?5 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and fecha between ?6 and ?7")
	public List<Teg> buscarTegSegunAreaInvestigacionPorDosFechasyEstatus(
			AreaInvestigacion area, String estatus1, String estatus2,
			String estatus3, String estatus4, Date fechaInicio, Date fechaFin);

	/********************************** Query para buscar la lista de teg dado a un programa *******************************************************************/

	@Query("select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?1)) and fecha between ?2 and ?3")
	public List<Teg> buscarTegPrograma(Programa programa, Date fechaInicio,
			Date fechaFin);

	/********************************** Query para buscar la lista de teg de todos los programas *******************************************************************/
	@Query("select teg from Teg teg where teg.fecha between ?1 and ?2")
	public List<Teg> buscarTegTodos(Date fechaInicio, Date fechaFin);

	/****************************** Inicio Reporte Promedio Gestion Teg ************************************/
	/******************************* Todos ****************************************************************/
	@Query("select teg from Teg teg where (teg.estatus=?1 or teg.estatus=?2)")
	public List<Teg> buscarTegSegunEstatus(String estatus1, String estatus2);

	/******************************* Por programa *********************************************************/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 and (teg.estatus=?2 or teg.estatus=?3)")
	public List<Teg> buscarTegSegunProgramaDosEstatus(Programa programa,
			String estatusProyectoTeg1, String estatusProyectoTeg2);

	/******************************* Por programa y area ******************************************************/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 and teg.tematica.areaInvestigacion=?2 and (teg.estatus=?3 or teg.estatus=?4)")
	public List<Teg> buscarTegSegunAreaProgramaDosEstatus(Programa programa,
			AreaInvestigacion area1, String estatusProyectoTeg1,
			String estatusProyectoTeg2);

	/******************************* Por area *****************************************/
	@Query("select teg from Teg teg where teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?1) and (teg.estatus=?2 or teg.estatus=?3)")
	public List<Teg> buscarTegSegunAreaDosEstatus(AreaInvestigacion area,
			String estatus1, String estatus2);

	/******************************** Por tematica *********************************************************/
	@Query("select teg from Teg teg where teg.tematica=?1 and (teg.estatus=?2 or teg.estatus=?3)")
	public List<Teg> buscarTegSegunTematicaDosEstatus(Tematica tematica,
			String estatus1, String estatus2);

	/******************************* Por tematica y programa **********************************************/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1 and teg.tematica=?2 and (teg.estatus=?3 or teg.estatus=?4) ")
	public List<Teg> buscarTegSegunTematicaProgramaDosEstatus(
			Programa programa, Tematica tematica, String estatusProyectoTeg1,
			String estatusProyectoTeg2);

	/******************************** Por tematica, area y programa *******************************************/
	@Query("SELECT DISTINCT teg FROM Teg teg INNER JOIN teg.estudiantes estudiante WHERE teg.tematica.areaInvestigacion IN (SELECT programaarea.area FROM ProgramaArea programaarea WHERE programaarea.programa=?1) AND estudiante.programa=?1  and teg.tematica.areaInvestigacion=?2  and teg.tematica=?3 and (teg.estatus=?4 or teg.estatus=?5) ")
	public List<Teg> buscarTegSegunTematicaAreaProgramaDosEstatus(
			Programa programa, AreaInvestigacion area, Tematica tematica,
			String estatusProyectoTeg1, String estatusProyectoTeg2);

	/********************************* Por tematica y area **************************************************/
	@Query("select teg from Teg teg where teg.tematica=?1 and teg.tematica.areaInvestigacion=?2 and (teg.estatus=?3 or teg.estatus=?4)")
	public List<Teg> buscarTegSegunTematicaAreaDosEstatus(Tematica tematica,
			AreaInvestigacion area, String estatus1, String estatus2);

	/****************************** Fin Reporte Promedio Gestion Teg ************************************/

	@Query("select teg from Teg teg where teg in ?1")
	public List<Teg> buscarSegunTegs(List<Teg> tegs);
	
	/***Reporte Evaluacion******/
	@Query("select teg from Teg teg where teg.estatus=?1 or teg.estatus=?2 and teg.tematica=?3 and fecha between ?4 and ?5 ")
	public List<Teg> buscarTegDeUnaTematicaPorDosFechasyVariosEstatus1(
			String estatus1, String estatus2, Tematica tematica1,
			Date fechaInicio, Date fechaFin);
	
	@Query("select teg from Teg teg where teg.estatus=?1 and teg.tematica=?2 and fecha between ?3 and ?4 ")
	public List<Teg> buscarTegPorFechayEstatus1(String estatus,
			Tematica tematica, Date fechaInicio, Date fechaFin);
}
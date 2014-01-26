package interfazdao;

import java.util.Date;
import java.util.List;


import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITegDAO extends JpaRepository <Teg, Long>  {
	
	
	public Teg findById(long id);



	//Evaluar Revisiones
//	@Query("select t from Teg t where t.estatus='TEGRegistrado'")
//	public List<Teg> buscarTegRegistrado();
	
	//Buscar tegs por profesores
	public List<Teg> findByTutor(List<Profesor> profesores);
	
	public List<Teg> findByProfesores(Profesor p);
//	@Query("select t from Teg t where t.estatus='Proyecto Registrado'")
//	public List<Teg> ProyectoRegistrado();	
	
//	@Query("select t from Teg t where t.estatus='Solicitando Registro'")
//	public List<Teg> TegSolicitandoRegistro();	

	/*Busca los teg asociados al Estudiante*/
	public List<Teg> findByEstudiantes(Estudiante estudiante);
		
	/*Busca los teg asociados al Profesor*/
	public List<Teg> findByTutor(Profesor profesor);
	
	/*Busca un teg asociado a un estudiante que tengan estatus avances finalizados*/
	public Teg findByEstatusLikeAndEstudiantes(
			String estatusAvancesFinalizados, Estudiante estudiante);
	
	/*Busca un teg asociado a un estudiante que tengan estatus avances finalizados*/
	public List<Teg> findByEstatus(
			String estatusProyectoFactible);

	public Teg findByEstatusAndEstudiantes(String string, Estudiante estudiante);

	//Busca los tegs de los estudiantes del programa dado y con estatus dado
	@Query("select t from Teg t where t.estatus=?2 and t.estudiantes in (select e from Estudiante e where e.programa=?1)")
	public List<Teg> buscarTegsPorPrograma(Programa programa, String string);

	public List<Teg> findByEstatusAndEstudiantesInOrderByIdAsc(String estatus, List<Estudiante> list);

	public List<Teg> findByTutorAndTematicaAndFechaBetween(
			Profesor buscarProfesorPorCedula, Tematica tematica,
			Date fechaInicio, Date fechaFin);

	public List<Teg> findByTutorAndTematicaAndEstatusAndFechaBetween(
			Profesor profesor, Tematica tematica, String estatus2,
			Date fechaInicio, Date fechaFin);

	public List<Teg> findByTutorAndFechaBetween(Profesor profesor,
			Date fechaInicio, Date fechaFin);

	public List<Teg> findByTutorAndEstatusAndFechaBetween(Profesor profesor,
			String estatus2, Date fechaInicio, Date fechaFin);
	
	@Query("select teg from Teg teg where teg.estatus=?1 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?2 ) and fecha between ?3 and ?4 ")
	public List<Teg> buscarUltimasTematicas(String estatus,AreaInvestigacion area,Date fechaInicio,Date fechaFin);

	public List<Teg> findByTematicaAndFechaBetween(Tematica tematica,
			Date fechaInicio, Date fechaFin);	
	
	
/******************************   Querys para ordenar segun id tematica segun programa segun area ***********************************************/
	
	//Lista para ordenar los ids de las tematicas segun un programa  segun un estatus y fecha inicio y fecha fin
	@Query("select teg.tematica.id from Teg teg where teg.estatus=?1 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion=?2 ) and fecha between ?3 and ?4 Order by teg.tematica.id")
	public List<Long> buscarUltimasTematicasProgramaAreaEstatus(String estatus,AreaInvestigacion area,Date fechaInicio,Date fechaFin);
	

/******************************   Querys para ordenar segun id tematica segun programa para todas las areas ***********************************************/
	
	//Lista para ordenar los ids de las tematicas segun un programa  por estatus y fecha inicio y fecha fin
	@Query("select teg.tematica.id from Teg teg where teg.estatus=?1 and teg.tematica in (select tematica from Tematica tematica where tematica.areaInvestigacion in (select programaarea.area from ProgramaArea programaarea where programaarea.programa=?2)) and fecha between ?3 and ?4 Order by teg.tematica.id")
	public List<Long> buscarUltimasTematicasProgramaEstatus(String estatus,Programa programa,Date fechaInicio,Date fechaFin);
	

	/*******************************   Querys para ordenar las ids tematicas para todos los programas *******************************/
	
	
	//Lista para ordenar los ids de las tematicas para todos los programas por estatus  y fecha inicio y fecha fin
	@Query("select teg.tematica.id from Teg teg where teg.estatus=?1 and fecha between ?2 and ?3 Order by teg.tematica.id")
	public List<Long> buscarUltimasEstatus(String estatus,Date fechaInicio,Date fechaFin);	

	
	
/*******************************  Querys con las 5 tematicas mas solicitadas segun sus estatus *********************************/
	
	@Query("select teg from Teg teg where teg.estatus=?1 and teg.tematica in ?2 and fecha between ?3 and ?4")
	public List<Teg> buscarUltimasOrdenadasEstatus(String estatus,List<Tematica> tematicas,Date fechaInicio,Date fechaFin);

	
	
	
	
	

}


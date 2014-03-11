package servicio;

import interfazdao.IEstudianteDAO;
import interfazdao.ITegDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class STeg {
	@Autowired
	private ITegDAO interfaceTeg;
	@Autowired
	private IEstudianteDAO interfaceEstudiante;

	private String[] estatus = { "Solicitando Registro", "Proyecto Registrado",
			"Comision Asignada", "Factibilidad Evaluada", "Proyecto Factible",
			"Proyecto No Factible", "Avances Finalizados", "TEG Registrado",
			"Revisiones Finalizadas", "Solicitando Defensa",
			"Defensa Asignada", "TEG Aprobado", "TEG Reprobado",
			"Jurado Asignado", "Proyecto en Desarrollo",
			"Trabajo en Desarrollo" };

	public Teg buscarTeg(long id) {
		return interfaceTeg.findOne(id);

	}

	public List<Teg> BuscarTegCalificandoDefensa() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[10]);
		return tegs;

	}

	public List<Teg> buscarTegs(String estatus) {
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus);
		return tegs;

	}

	// Evaluar Revisiones
	public List<Teg> buscarTegRegistrado() {
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus[7], estatus[15]);
		return tegs;

	}

	public List<Teg> buscarTegFactibilidad() {
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus[4], estatus[5]);
		return tegs;

	}

	public List<Teg> buscarActivos() {
		List<Teg> tegs;
		tegs = interfaceTeg.findAll();
		return tegs;
	}

	public void guardar(Teg objetoTeg1) {
		// TODO Auto-generated method stub
		interfaceTeg.save(objetoTeg1);
	}

	/* Buscar los teg que tengan estatus Proyecto Registrado */
	public List<Teg> BuscarProyectoRegistrado() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[1]);
		return tegs;

	}

	public List<Teg> buscarProyectoAsignado() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[2]);
		return tegs;

	}

	public List<Teg> BuscarTegSolicitandoRegistro() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[0]);
		return tegs;

	}

	/* Busca los teg asociados al Profesor */
	public List<Teg> buscarTutoriaProfesor(Profesor profesor) {
		List<Teg> teg;
		teg = interfaceTeg.findByTutor(profesor);
		return teg;
	}

	/* Busca los teg asociados al Estudiante */
	public List<Teg> buscarTegPorEstudiante(Estudiante estudiante) {
		// TODO Auto-generated method stub
		List<Teg> teg;
		teg = interfaceTeg.findByEstudiantes(estudiante);
		return teg;
	}

	public List<Teg> buscarTegPorProfesor(String estatus1, String estatus2,
			Profesor profesor) {
		List<Teg> teg;
		teg = interfaceTeg.findByEstatusOrEstatusLikeAndTutor(estatus1,
				estatus2, profesor);
		return teg;

	}

	public Teg ultimoTeg(Estudiante estudiante) {
		List<Teg> tegs = interfaceTeg.findByEstudiantes(estudiante);
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date mayor = new Date();
		long id = 0;
		try {
			mayor = formato.parse("1900-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Teg teg = new Teg();
		for(int i = 0; i<tegs.size(); i ++){
			if((tegs.get(i).getFecha().after(mayor)|| tegs.get(i).getFecha().equals(mayor))&& tegs.get(i).getId()>id){
				mayor = tegs.get(i).getFecha();
				id = tegs.get(i).getId();
				teg = tegs.get(i);
			}
		}
		return teg;
	}
	/*
	 * Busca un teg asociado a un estudiante que tengan estatus avances
	 * finalizados
	 */
	public Teg buscarTegEstudiantePorEstatus(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusAndEstudiantes(estatus[6],
				estudiante);
		return teg;
	}

	/* Busca un teg asociado a un estudiante que tenga estatus teg aprobado */
	public Teg buscarTegEstudiantePorEstatusAprobado(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusLikeAndEstudiantes(estatus[11],
				estudiante);
		return teg;
	}

	/* Buscar los teg que tengan estatus Proyecto Registrado */
	public List<Teg> buscarProyectoFactible() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus[4], estatus[14]);
		return tegs;

	}

	/* Buscar los teg que tengan estatus Factibilidad Evaluada */
	public List<Teg> buscarProyectoFactibilidadEvaluada() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[3]);
		return tegs;

	}

	/* Buscar ultimo teg registrado */
	public Teg buscarUltimoTeg() {
		Teg ultimoTeg;
		ultimoTeg = interfaceTeg.findOne(interfaceTeg.ultimoTegRegistrado());
		return ultimoTeg;
	}

	public Teg buscarTegConRevisionFinal(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusAndEstudiantes(estatus[8], estudiante);
		return teg;
	}

	public Teg buscarTegSolicitandoDefensa(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusAndEstudiantes(estatus[9], estudiante);
		return teg;
	}

	public List<Teg> buscarTegPorProgramaParaDefensa(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[9], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	/*
	 * Buscar los teg que tengan estatus Factibilidad Evaluada de acuerdo a un
	 * programa
	 */

	public List<Teg> buscarTegPorProgramaParaRegistrarFactibilidad(
			Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[3], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	/*
	 * Buscar los teg que tengan estatus Solicitando Registro de acuerdo a un
	 * programa
	 */

	public List<Teg> buscarTegPorProgramaParaRegistrarTeg(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[0], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	/*
	 * Buscar los teg que tengan estatus Proyecto Registrado de acuerdo a un
	 * programa
	 */

	public List<Teg> buscarTegPorProgramaParaAsignarComision(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[1], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	/*
	 * Buscar los teg que tengan estatus Jurado Asignado de acuerdo a un
	 * programa
	 */

	public List<Teg> buscarTegPorProgramaParaDefensa2(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[13], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	public List<Teg> buscarTegDeComision(Profesor obtenerUsuarioProfesor) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByProfesores(obtenerUsuarioProfesor);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorDosFechasYTematica(
			Profesor buscarProfesorPorCedula, Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg
				.findByTutorAndTematicaAndFechaBetweenOrderByFechaAsc(
						buscarProfesorPorCedula, tematica, fechaInicio,
						fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorTematicaPorDosFechasYEstatus(
			Profesor profesor, Tematica tematica, String estatus2,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg
				.findByTutorAndTematicaAndEstatusAndFechaBetweenOrderByFechaAsc(
						profesor, tematica, estatus2, fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarTodosTegsDeTutorPorDosFechas(Profesor profesor,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByTutorAndFechaBetweenOrderByFechaAsc(profesor,
				fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorDosFechasYEstatus(Profesor profesor,
			String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg
				.findByTutorAndEstatusAndFechaBetweenOrderByFechaAsc(profesor,
						estatus2, fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarUltimasTematicas(String estatus,
			AreaInvestigacion area, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.buscarUltimasTematicas(estatus, area, fechaInicio,
				fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegsSegunAreaUnEstatus(AreaInvestigacion area,
			Date fechaInicio, Date fechaFin, String estatus) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegAreaestatus(area, fechaInicio, fechaFin,
				estatus);
		return tegs;

	}

	public List<Teg> buscarTegsDeTematicaPorDosFechas(Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.findByTematicaAndFechaBetween(tematica,
				fechaInicio, fechaFin);
	}
	
	
   /***********************Inicio Reporte Tematicas Mas Solicitadas ************************/
	/*************************** Por Programa **********************************************/
	public List<Teg> buscarUltimasTematicasProgramaEstatus(Programa programa,
			Date fechaInicio, Date fechaFin, String estatusProyectoTeg1,
			String estatusProyectoTeg2) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg
				.buscarUltimasTematicasProgramaDosEstatus(programa, fechaInicio,
						fechaFin, estatusProyectoTeg1, estatusProyectoTeg2);
		return tegs;
	}
	/************************** Por Area  ***************************************************/
	public List<Teg> buscarUltimasTematicasAreaEstatus(AreaInvestigacion area,
			Date fechaInicio, Date fechaFin, String estatusProyectoTeg1,
			String estatusProyectoTeg2) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg
				.buscarUltimasTematicasAreaDosEstatus(area, fechaInicio,
						fechaFin, estatusProyectoTeg1, estatusProyectoTeg2);
		return tegs;
	}
	/*********************** Por Programa y Area**********************************************/
	public List<Teg> buscarUltimasTematicasProgramaAreaEstatus(
			Programa programa, AreaInvestigacion area1, Date fechaInicio,
			Date fechaFin, String estatusProyectoTeg1,
			String estatusProyectoTeg2) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.buscarUltimasTematicasProgramaAreaDosEstatus(programa,
				area1, fechaInicio, fechaFin, estatusProyectoTeg1,
				estatusProyectoTeg2);
		return tegs;
	}
	/*************************** Todos ***********************************************************/
	public List<Long> buscarUltimasEstatus(String estatusProyectoTeg1,
			String estatusProyectoTeg2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Long> tegs;
		tegs = interfaceTeg.buscarUltimasDosEstatus(estatusProyectoTeg1,
				estatusProyectoTeg2, fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarUltimasOrdenadasEstatus(String estatusProyectoTeg1,
			String estatusProyectoTeg2, List<Tematica> tematicas,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.buscarUltimasOrdenadasEstatus(estatusProyectoTeg1,
				estatusProyectoTeg2, tematicas, fechaInicio, fechaFin);
		return tegs;
	}
	/**************************** Fin Reporte Tematicas Mas Solicitadas****************************/
	public long contadorEstatus(String estatusProyectoTeg, Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		long tegs;
		tegs = interfaceTeg.countByEstatus(estatusProyectoTeg, tematica,
				fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegPorDosFechasyUnEstatus(String estatus,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFecha(estatus, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegDeUnaTematicaPorDosFechasyVariosEstatus(
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7,
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegporFechayVariosEstatus(estatus1, estatus2,
				estatus3, estatus4, estatus5, estatus6, estatus7, tematica,
				fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorProgramaVariasAreasVariosEstatus(
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7,
			Programa programa, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorProgramaVariasAreasVariosEstatus(
				estatus1, estatus2, estatus3, estatus4, estatus5, estatus6,
				estatus7, programa, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorVariosProgramasVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorVariosProgramasVariosEstatus(estatus1,
				estatus2, estatus3, estatus4, estatus5, estatus6, estatus7,
				fechaInicio, fechaFin);

	}

	public List<Teg> buscarTegsSegunArea(AreaInvestigacion area,
			Date fechaInicio, Date fechaFin) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegArea(area, fechaInicio, fechaFin);
		return tegs;

	}

	public List<Teg> buscarTegsSegunPrograma(Programa programa,
			Date fechaInicio, Date fechaFin) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegPrograma(programa, fechaInicio, fechaFin);
		return tegs;

	}

	public List<Teg> buscarTegTodos(Date fechaInicio, Date fechaFin) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegTodos(fechaInicio, fechaFin);
		return tegs;
	}

	/*************************Inicio Report Promedio Gestion de Teg ************************************/
	/***************************Todos*******************************************************************/
	public List<Teg> buscarSegunDosEstatus(String estatus1, String estatus2) {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus1, estatus2);
		return tegs;

	}
	
	/*********************************Por programa *****************************************************/
	public List<Teg> buscarTegSegunProgramaDosEstatus(Programa programa,
			String estatus1, String estatus2) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegSegunProgramaDosEstatus(programa,estatus1,estatus2);
		return tegs;
	}
	/******************************** Por area   ********************************************************/
	public List<Teg> buscarTegSegunAreaDosEstatus(AreaInvestigacion area, String estatus1, String estatus2) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegSegunAreaDosEstatus(area, estatus1, estatus2);
		return tegs;
	}
	/******************************** Por tematica ******************************************************/
	public List<Teg> buscarTegSegunTematicaDosEstatus(Tematica tematica, String estatus1, String estatus2) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegSegunTematicaDosEstatus(tematica, estatus1, estatus2);
		return tegs;
	}
	/******************************* Por programa area ***************************************************/
	public List<Teg> buscarTegSegunAreaProgramaDosEstatus(
			Programa programa,AreaInvestigacion area, String estatus1, String estatus2) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegSegunAreaProgramaDosEstatus(programa,area, estatus1, estatus2);
		return tegs;
	}
	/******************************* Por programa tematica ************************************************/
	public List<Teg> buscarTegSegunTematicaProgramaDosEstatus(Programa programa,Tematica tematica,
			String estatus1, String estatus2) {
		List<Teg> tegs;
		tegs = interfaceTeg
				.buscarTegSegunTematicaProgramaDosEstatus(programa, tematica, estatus1, estatus2);
		return tegs;
	}
	/******************************** Por area tematica ****************************************************/
	public List<Teg> buscarTegSegunTematicaAreaDosEstatus(Tematica tematica,AreaInvestigacion area,
			String estatus1, String estatus2) {
		List<Teg> tegs;
		tegs = interfaceTeg
				.buscarTegSegunTematicaAreaDosEstatus(tematica,area, estatus1, estatus2);
		return tegs;
	}
	/********************************* Por programa area tematica ******************************************/
	public List<Teg> buscarTegSegunTematicaAreaProgramaDosEstatus(Programa programa,AreaInvestigacion area,Tematica tematica,
			String estatus1, String estatus2) {
		List<Teg> tegs;
		tegs = interfaceTeg
				.buscarTegSegunTematicaAreaProgramaDosEstatus(programa,area,tematica, estatus1, estatus2);
		return tegs;
	}
	/*************************Fin Report Promedio Gestion de Teg ************************************/
	
	/*---- Servicios para Reporte Trabajos ----*/
	public List<Teg> buscarTegPorTematicaEstatusPrograma(Programa programa,
			String estatus, Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorTematicaEstatusPrograma(programa, estatus, tematica,
				fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorAreaEstatusPrograma(Programa programa, AreaInvestigacion area1,
			String estatus, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorAreaEstatusPrograma(programa, area1, estatus, fechaInicio,
				fechaFin);
	}

	public List<Teg> buscarTegPorAreaPrograma(Programa programa, AreaInvestigacion area1,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorAreaPrograma(programa, area1, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorVariosEstatusAreaPrograma(Programa programa, AreaInvestigacion area1,
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorVariosEstatusAreaPrograma(programa, area1, estatus1,
				estatus2, estatus3, estatus4, estatus5, estatus6, estatus7,
				estatus8, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorVariosEstatusTematicaPrograma( Programa programa, Tematica tematica, 
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorVariosEstatusTematicaPrograma(programa, tematica, 
				estatus1, estatus2, estatus3, estatus4, estatus5, estatus6,
				estatus7, estatus8, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorTematicaPrograma(Programa programa, Tematica tematica, 
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorTematicaPrograma(programa, tematica, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorProgramaEstatus(Programa programa,
			String estatus,	Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorProgramaEstatus(programa,
				estatus, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorProgramaVariosEstatus(Programa programa, 
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorProgramaVariosEstatus( programa,
				estatus1, estatus2, estatus3, estatus4, estatus5, estatus6,
				estatus7, estatus8, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorPrograma(Programa programa, 
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorPrograma( programa,
				fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorEstatus(String estatus,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorEstatus(estatus,
				fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, String estatus8,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorVariosEstatus(estatus1,
				estatus2, estatus3, estatus4, estatus5, estatus6, estatus7,
				estatus8, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorFecha(Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFecha(fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorTematicaEstatus(Tematica tematica,
			String estatus, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorTematicaEstatus(tematica, estatus, fechaInicio,
				fechaFin);
	}
	
	public List<Teg> buscarTegPorTematicaVariosEstatus(String estatus1, String estatus2,
			String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, String estatus8,
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorTematicaVariosEstatus(estatus1,
				estatus2, estatus3, estatus4, estatus5, estatus6, estatus7,
				estatus8, tematica, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorAreaEstatus(AreaInvestigacion area1,
			String estatus, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorAreaEstatus(area1, estatus, fechaInicio,
				fechaFin);
	}
	
	public List<Teg> buscarTegPorAreaVariosEstatus(String estatus1, String estatus2,
			String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, String estatus8,
			AreaInvestigacion area1, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorAreaVariosEstatus(estatus1,
				estatus2, estatus3, estatus4, estatus5, estatus6, estatus7,
				estatus8, area1, fechaInicio, fechaFin);
	}
	
	/*---- Fin de Servicios para Reporte Trabajos ----*/
	
	/*---- Servicios para Reporte Profesor ----*/

	public List<Teg> buscarTegDeUnaTematicaPorDosFechas(Programa programa,
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechaTematicaPrograma(programa,
				tematica, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegDeUnAreaPorDosFechas(Programa programa,
			AreaInvestigacion area1, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechaAreaPrograma(programa, area1,
				fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegDeUnProgramaPorDosFechas(Programa programa,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechayPrograma(programa, fechaInicio,
				fechaFin);
	}

	public List<Teg> buscarTodosTegPorDosFechas(Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTodosTegporFecha(fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegUnaTematicaPorDosFechas(Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechaTematica(tematica, fechaInicio,
				fechaFin);
	}

	public List<Teg> buscarTegUnAreaPorDosFechas(AreaInvestigacion area1,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechaArea(area1, fechaInicio, fechaFin);
	}
	
	/*----  Fin de Servicios para Reporte Profesor ----*/

	/*---- Servicios para Reporte Teg ----*/
	public List<Teg> buscarTodosProgramasUnAreaUnaTematicaUnEstatus(
			Tematica tematica, String estatus, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTodosProgramasUnAreaUnaTematicaUnEstatus(
				tematica, estatus, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTodosProgramasUnAreaUnaTematicaTodosEstatus(
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTodosProgramasUnAreaUnaTematicaTodosEstatus(
				tematica, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTodosProgramasUnAreaTodasTematicaUnEstatus(
			AreaInvestigacion areaInvestigacion, String estatus,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTodosProgramasUnAreaTodasTematicaUnEstatus(
				areaInvestigacion, estatus, fechaInicio, fechaFin);

	}

	public List<Teg> buscarTodosProgramasUnAreaTodasTematicaTodosEstatus(
			AreaInvestigacion areaInvestigacion, Date fechaInicio, Date fechaFin) {
		return interfaceTeg
				.buscarTodosProgramasUnAreaTodasTematicaTodosEstatus(
						areaInvestigacion, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegDeUnaTematicaPorDosFechasyVariosEstatus1(
			String estatus1, String estatus2, Tematica tematica1, Date fechaInicio,
			Date fechaFin) {
		// TODO Auto-generated method stub
		return interfaceTeg.buscarTegDeUnaTematicaPorDosFechasyVariosEstatus1(
				estatus1, estatus2, tematica1,fechaInicio,
				 fechaFin);
	}
	
	public List<Teg> buscarTegDeUnaTematicaPorDosFechasyUnEstatus1(
			String estatus, Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechayEstatus1(estatus, tematica,
								fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegSegunAreaInvestigacionPorDosFechasyEstatus(
			AreaInvestigacion area1, String estatus1, String estatus2,
			String estatus12, String estatus22, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		return interfaceTeg.buscarTegSegunAreaInvestigacionPorDosFechasyEstatus(
				 area1, estatus1,  estatus2,
				estatus12, estatus22, fechaInicio, fechaFin);
	}

}

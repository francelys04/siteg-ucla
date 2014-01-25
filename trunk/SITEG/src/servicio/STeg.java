package servicio;

import java.util.Date;
import java.util.List;

import interfazdao.IActividadDAO;
import interfazdao.IEstudianteDAO;
import interfazdao.ITegDAO;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.Usuario;

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
			"Defensa Asignada", "TEG Aprobado", "TEG Reprobado", "Jurado Asignado"};

	public Teg buscarTeg(long id) {
		return interfaceTeg.findOne(id);

	}
	
	public List<Teg> BuscarTegCalificandoDefensa() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[10]);
		return tegs;

	}
	// Evaluar Revisiones
	public List<Teg> buscarTegRegistrado() {
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[7]);
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

	/*Buscar los teg que tengan estatus Proyecto Registrado*/
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

	/*Busca un teg asociado a un estudiante que tengan estatus avances finalizados*/
	public Teg buscarTegEstudiantePorEstatus(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusLikeAndEstudiantes(
				estatus[6], estudiante);
		return teg;
	}
	
	
	/*Buscar los teg que tengan estatus Proyecto Registrado*/
	public List<Teg> buscarProyectoFactible() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[4]);
		return tegs;

	}
	
	/*Buscar los teg que tengan estatus Factibilidad Evaluada*/
	public List<Teg> buscarProyectoFactibilidadEvaluada() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[3]);
		return tegs;

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
		tegs =interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(estatus[9], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}
	
	public List<Teg> buscarTegPorProgramaParaDefensa2(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs =interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(estatus[13], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	public List<Teg> buscarTegDeComision(Profesor obtenerUsuarioProfesor) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByProfesores(obtenerUsuarioProfesor);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorDosFechasYTematica(
			Profesor buscarProfesorPorCedula, Tematica tematica, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs=interfaceTeg.findByTutorAndTematicaAndFechaBetween(buscarProfesorPorCedula, tematica, fechaInicio,fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorTematicaPorDosFechasYEstatus(Profesor profesor,
			Tematica tematica, String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs=interfaceTeg.findByTutorAndTematicaAndEstatusAndFechaBetween(profesor,tematica,estatus2, fechaInicio,fechaFin);
		return tegs;
	}

	public List<Teg> buscarTodosTegsDeTutorPorDosFechas(Profesor profesor,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByTutorAndFechaBetween(profesor,fechaInicio,fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorDosFechasYEstatus(Profesor profesor,
			String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByTutorAndEstatusAndFechaBetween(profesor,estatus2,fechaInicio,fechaFin);
		return tegs;
	}
	public List<Teg> buscarUltimasTematicas(String estatus, AreaInvestigacion area,Date fechaInicio,Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs =interfaceTeg.buscarUltimasTematicas(estatus, area,fechaInicio,fechaFin);
		return tegs;
	}
	//Acomodar
//	public Teg buscarTegUnicoPorEstudiante(Estudiante obtenerUsuarioEstudiante) {
//		// TODO Auto-generated method stub
//		Teg teg;
//		teg = interfaceTeg.findByEstudiantes(obtenerUsuarioEstudiante);
//		return teg;
//	}

	public List<Teg> buscarTegsDeTematicaPorDosFechas(Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.findByTematicaAndFechaBetween(
				tematica, fechaInicio, fechaFin);
	}
	


}

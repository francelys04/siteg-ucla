package servicio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import interfazdao.IEstudianteDAO;
import interfazdao.ISolicitudTutoriaDAO;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSolicitudTutoria {

	@Autowired
	private ISolicitudTutoriaDAO interfaceSolicitud;
	@Autowired
	private IEstudianteDAO interfaceEstudiante;
	private String[] Estatus = { "Por Revisar", "Aceptada", "Rechazada",
			"Finalizada" };

	public void guardar(SolicitudTutoria solicitud) {
		interfaceSolicitud.save(solicitud);
	}

	public List<SolicitudTutoria> buscarAceptadas() {
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.findByEstatusLike(Estatus[1]);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudPorRevisar(Profesor profesor) {
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.findByEstatusLikeAndProfesor(
				Estatus[0], profesor);
		return solicitudes;
	}

	public SolicitudTutoria buscarSolicitud(long id) {
		// TODO Auto-generated method stub
		SolicitudTutoria solicitud;
		solicitud = interfaceSolicitud.findOne(id);
		return solicitud;
	}

	public void guardarSolicitud(SolicitudTutoria solicitud) {
		// TODO Auto-generated method stub
		interfaceSolicitud.save(solicitud);
	}

	public SolicitudTutoria buscarSolicitudEstudiantePorRevisar(
			Estudiante estudiante) {
		// TODO Auto-generated method stub
		SolicitudTutoria solicitudes;
		solicitudes = interfaceSolicitud.findByEstatusLikeAndEstudiantes(
				Estatus[0], estudiante);
		return solicitudes;
	}

	public SolicitudTutoria buscarSolicitudAceptadaEstudiante(
			Estudiante estudiante) {
		// TODO Auto-generated method stub
		SolicitudTutoria solicitudes;
		solicitudes = interfaceSolicitud.findByEstatusLikeAndEstudiantes(
				Estatus[1], estudiante);
		return solicitudes;
	}

	/* Busca las solicitudes asociadas al Estudiante */
	public List<SolicitudTutoria> buscarSolicitud(Estudiante estudiante) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudTutoria;
		solicitudTutoria = interfaceSolicitud.findByEstudiantes(estudiante);
		return solicitudTutoria;
	}

	public List<SolicitudTutoria> buscarTodasSolicitudesEntreFechas(Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByFechaBetweenOrderByProfesorDesc(
						fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorTematicaEntreFechas(
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByTematicaAndFechaBetweenOrderByProfesorDesc(tematica,
						fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorEstatusEntreFechas(
			String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByEstatusAndFechaBetweenOrderByProfesorDesc(estatus2,
						fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorTematicaEstatusEntreFechas(
			Tematica tematica, String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByTematicaAndEstatusAndFechaBetweenOrderByProfesorDesc(
						tematica, estatus2, fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorProfesorPorTematicaEstatusEntreFechas(
			Profesor profesor, Tematica tematica, String estatus2,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByProfesorAndTematicaAndEstatusAndFechaBetween(profesor,
						tematica, estatus2, fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorProfesorEstatusEntreFechas(
			Profesor profesor, String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByProfesorAndEstatusAndFechaBetween(profesor, estatus2,
						fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorProfesorTematicaEntreFechas(
			Profesor profesor, Tematica tematica, Date fechaInicio,
			Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByProfesorAndTematicaAndFechaBetween(profesor, tematica,
						fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorProfesorEntreFechas(
			Profesor profesor, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.findByProfesorAndFechaBetween(
				profesor, fechaInicio, fechaFin);
		return solicitudes;
	}

	public Long contarSolicitudes(Profesor profesor) {
		long valor = 0;
		valor = interfaceSolicitud.countByProfesor(profesor);
		return valor;
	}

	public long contarSolicitudesPorTematica(Profesor profesor,
			Tematica tematica) {
		// TODO Auto-generated method stub
		long valor = 0;
		valor = interfaceSolicitud.countByProfesorAndTematica(profesor,
				tematica);
		return valor;
	}

	public List<SolicitudTutoria> buscarSolicitudPorVariosProgramaUnEstatus(
			Date fechaInicio, Date fechaFin) {
		return interfaceSolicitud.buscarSolicitudPorVariosProgramaUnEstatus(
				fechaInicio, fechaFin);
	}

	public List<SolicitudTutoria> buscarSolicitudPorProgramaUnEstatus1(
			String estatus, Programa programa, Date fechaInicio, Date fechaFin) {
		return interfaceSolicitud
				.buscarSolicitudPorProgramaVariasAreasUnEstatus(programa,
						fechaInicio, fechaFin);
	}

	public List<SolicitudTutoria> buscarSolicitudPorProgramaUnEstatus(
			Programa programa, Date fechaInicio, Date fechaFin) {
		return interfaceSolicitud
				.buscarSolicitudPorProgramaVariasAreasUnEstatus(programa,
						fechaInicio, fechaFin);
	}

	public List<SolicitudTutoria> buscarSolicitudPorPrograma(Programa programa,
			Date fechaInicio, Date fechaFin) {
		return interfaceSolicitud.buscarSolicitudPorProgramaVariasAreas(
				programa, fechaInicio, fechaFin);
	}

	public List<SolicitudTutoria> buscarSolicitudDeUnaTematicaPorDosFechasyUnEstatus(
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceSolicitud.buscarSolicitudPorFechayEstatus(tematica,
				fechaInicio, fechaFin);
	}

	public List<SolicitudTutoria> buscarSolicitudSegunAreaUnEstatus(
			AreaInvestigacion area, Date fechaInicio, Date fechaFin) {
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.buscarSolicitudAreaestatus(area,
				fechaInicio, fechaFin);
		return solicitudes;

	}

	public List<SolicitudTutoria> buscarSolicitudSegunAreaUnEstatus1(
			AreaInvestigacion area, Date fechaInicio, Date fechaFin) {
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.buscarSolicitudAreaestatus1(area,
				fechaInicio, fechaFin);
		return solicitudes;

	}

	public List<SolicitudTutoria> buscarSolicitudesPorTematicayAreaYFechas(
			Tematica tematicaI, AreaInvestigacion areaI, Date fechaInicio,
			Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByTematicaAndTematicaAreaInvestigacionAndFechaBetweenOrderByProfesorDesc(tematicaI,
						areaI, fechaInicio, fechaFin);
		return solicitudes;
	}
	
	public List<SolicitudTutoria> buscarSolicitudesPorAreaYFechas(
			AreaInvestigacion areaI, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByTematicaAreaInvestigacionAndFechaBetweenOrderByProfesorDesc(
						areaI, fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorProgramaYFechas(
			Programa programaI, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndFechaBetween(estudiantes,
							fechaInicio, fechaFin);
		}
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorProgramaYAreaYFechas(
			Programa programaI, AreaInvestigacion areaI, Date fechaInicio,
			Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndTematicaAreaInvestigacionAndFechaBetween(
							estudiantes, areaI, fechaInicio, fechaFin);
		}
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorProfesorYAreaEntreFechas(
			Profesor profesor, AreaInvestigacion areaI, Date fechaInicio,
			Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByProfesorAndTematicaAreaInvestigacionAndFechaBetween(
						profesor, areaI, fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorProfesorYProgramaEntreFechas(
			Profesor profesor, Programa programaI, Date fechaInicio,
			Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndProfesorAndFechaBetween(
							estudiantes, profesor, fechaInicio, fechaFin);
		}
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorProfesorYProgramaYAreaEntreFechas(
			Profesor profesor, Programa programaI, AreaInvestigacion areaI,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndProfesorAndTematicaAreaInvestigacionAndFechaBetween(
							estudiantes, profesor, areaI, fechaInicio, fechaFin);
		}
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorProgramaYFechas(
			Tematica tematicaI, Programa programaI, Date fechaInicio,
			Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndTematicaAndFechaBetween(
							estudiantes, tematicaI, fechaInicio, fechaFin);
		}
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorProgramaYAreaYFechas(
			Tematica tematicaI, Programa programaI, AreaInvestigacion areaI,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndTematicaAndTematicaAreaInvestigacionAndFechaBetween(
							estudiantes, tematicaI, areaI, fechaInicio, fechaFin);
		}
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorTematicaProfesorYAreaEntreFechas(
			Tematica tematicaI, Profesor profesor, AreaInvestigacion areaI,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud
				.findByTematicaAndProfesorAndTematicaAreaInvestigacionAndFechaBetween(tematicaI,
						profesor, areaI, fechaInicio, fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorTematicaProfesorYProgramaEntreFechas(
			Tematica tematicaI, Profesor profesor, Programa programaI,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndTematicaAndProfesorAndFechaBetween(
							estudiantes, tematicaI, profesor, fechaInicio, fechaFin);
		}
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarPorTematicaProfesorYProgramaYAreaEntreFechas(
			Tematica tematicaI, Profesor profesor, Programa programaI,
			AreaInvestigacion areaI, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes = interfaceEstudiante
				.findByPrograma(programaI);
		List<SolicitudTutoria> solicitudes = new ArrayList<SolicitudTutoria>();
		if (!estudiantes.isEmpty()) {
			solicitudes = interfaceSolicitud
					.findDistinctByEstudiantesInAndTematicaAndTematicaAreaInvestigacionAndProfesorAndFechaBetween(
							estudiantes, tematicaI, areaI, profesor, fechaInicio, fechaFin);
		}
		return solicitudes;
	}

}

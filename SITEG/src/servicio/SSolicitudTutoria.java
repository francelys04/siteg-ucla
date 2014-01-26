package servicio;

import java.util.Date;
import java.util.List;

import interfazdao.ISolicitudTutoriaDAO;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSolicitudTutoria {

	@Autowired
	private ISolicitudTutoriaDAO interfaceSolicitud;
	private String[] Estatus = {"Por Revisar", "Aceptada", "Rechazada", "Finalizada"};
	
	public void guardar(SolicitudTutoria solicitud){
		interfaceSolicitud.save(solicitud);
	}
	
	public List<SolicitudTutoria> buscarAceptadas()
	{
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.findByEstatusLike(Estatus[1]);
		return solicitudes;
	}
	
	public List<SolicitudTutoria> buscarSolicitudPorRevisar(Profesor profesor){
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.findByEstatusLikeAndProfesor(Estatus[0], profesor);
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
        solicitudes = interfaceSolicitud.findByEstatusLikeAndEstudiantes(Estatus[0], estudiante);
        return solicitudes;
	}
	
	
	public SolicitudTutoria buscarSolicitudAceptadaEstudiante(
			Estudiante estudiante) {
		// TODO Auto-generated method stub
        SolicitudTutoria solicitudes;
        solicitudes = interfaceSolicitud.findByEstatusLikeAndEstudiantes(Estatus[1], estudiante);
        return solicitudes;
	}
	
	
	/* Busca las solicitudes asociadas al Estudiante */
	public List<SolicitudTutoria> buscarSolicitud(Estudiante estudiante) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudTutoria;
		solicitudTutoria = interfaceSolicitud.findByEstudiantes(estudiante);
		return solicitudTutoria;
	}

	public List<SolicitudTutoria> buscarTodasSolicitudesEntreFechas(
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.findByFechaBetween(fechaInicio,fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorTematicaEntreFechas(
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes = interfaceSolicitud.findByTematicaAndFechaBetween(tematica,fechaInicio,fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorEstatusEntreFechas(
			String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes=interfaceSolicitud.findByEstatusAndFechaBetween(estatus2,fechaInicio,fechaFin);
		return solicitudes;
	}

	public List<SolicitudTutoria> buscarSolicitudesPorTematicaEstatusEntreFechas(
			Tematica tematica, String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<SolicitudTutoria> solicitudes;
		solicitudes=interfaceSolicitud.findByTematicaAndEstatusAndFechaBetween(tematica,estatus2,fechaInicio,fechaFin);
		return solicitudes;
	}
	
}

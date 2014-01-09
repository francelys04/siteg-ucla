package servicio;

import java.util.List;

import interfazdao.ISolicitudTutoriaDAO;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.SolicitudTutoria;
import modelo.Teg;
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
	
}

package servicio;

import java.util.List;

import interfazdao.ISolicitudTutoriaDAO;

import modelo.Profesor;
import modelo.SolicitudTutoria;
import modelo.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSolicitudTutoria {

	@Autowired
	private ISolicitudTutoriaDAO interfaceSolicitud;
	private String[] Estatus = {"Por Revisar", "Aprobada", "Rechazada"};
	
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
}

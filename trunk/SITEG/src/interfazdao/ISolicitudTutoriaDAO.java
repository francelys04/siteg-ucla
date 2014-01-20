package interfazdao;

import java.util.List;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudTutoriaDAO extends JpaRepository<SolicitudTutoria, Long> {

	public List<SolicitudTutoria> findByEstatusLikeAndProfesor(
			String estatusPorRevisar, Profesor profesor);
	
	public SolicitudTutoria findByEstatusLikeAndEstudiantes(
			String estatusDefensaAsignada, Estudiante estudiante);
	
	public List<SolicitudTutoria> findByEstatusLike(
			String estatusDefensaAsignada);
	
	/*Busca las solicitudes asociadas al Estudiante*/
	public List<SolicitudTutoria> findByEstudiantes(Estudiante estudiante);
}

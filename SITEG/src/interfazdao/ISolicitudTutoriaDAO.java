package interfazdao;

import java.util.Date;
import java.util.List;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudTutoriaDAO extends JpaRepository<SolicitudTutoria, Long> {

	public List<SolicitudTutoria> findByEstatusLikeAndProfesor(
			String estatusPorRevisar, Profesor profesor);
	
	public SolicitudTutoria findByEstatusLikeAndEstudiantes(
			String estatusDefensaAsignada, Estudiante estudiante);
	
	public List<SolicitudTutoria> findByEstatusLike(
			String estatusDefensaAsignada);
	
	public List<SolicitudTutoria> findByEstudiantes(Estudiante estudiante);

	public List<SolicitudTutoria> findByFechaBetween(Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findByTematicaAndFechaBetween(
			Tematica tematica, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByEstatusAndFechaBetween(String estatus2,
			Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByTematicaAndEstatusAndFechaBetween(
			Tematica tematica, String estatus2, Date fechaInicio, Date fechaFin);
}

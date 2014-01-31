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
import org.springframework.data.jpa.repository.Query;

public interface ISolicitudTutoriaDAO extends JpaRepository<SolicitudTutoria, Long> {

	public List<SolicitudTutoria> findByEstatusLikeAndProfesor(
			String estatusPorRevisar, Profesor profesor);
	
	public SolicitudTutoria findByEstatusLikeAndEstudiantes(
			String estatusDefensaAsignada, Estudiante estudiante);
	
	public List<SolicitudTutoria> findByEstatusLike(
			String estatusDefensaAsignada);
	
	public List<SolicitudTutoria> findByEstudiantes(Estudiante estudiante);

	public List<SolicitudTutoria> findByFechaBetweenOrderByProfesorDesc(Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findByTematicaAndFechaBetweenOrderByProfesorDesc(
			Tematica tematica, Date fechaInicio, Date fechaFin);

//	public List<SolicitudTutoria> findByEstatusAndFechaBetween(String estatus2,
//			Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByTematicaAndEstatusAndFechaBetweenOrderByProfesorDesc(
			Tematica tematica, String estatus2, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndTematicaAndEstatusAndFechaBetween(
			Profesor profesor, Tematica tematica, String estatus2,
			Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByEstatusAndFechaBetweenOrderByProfesorDesc(
			String estatus2, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndEstatusAndFechaBetween(
			Profesor profesor, String estatus2, Date fechaInicio, Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndTematicaAndFechaBetween(
			Profesor profesor, Tematica tematica, Date fechaInicio,
			Date fechaFin);

	public List<SolicitudTutoria> findByProfesorAndFechaBetween(
			Profesor profesor, Date fechaInicio, Date fechaFin);

	@Query("select count(e) from SolicitudTutoria e where e.profesor=?1")
	public long countByProfesor(Profesor profesor);

	@Query("select count(e) from SolicitudTutoria e where e.profesor=?1 and e.tematica=?2")
	public long countByProfesorAndTematica(Profesor profesor, Tematica tematica);
}

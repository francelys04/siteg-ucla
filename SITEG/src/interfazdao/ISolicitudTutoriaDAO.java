package interfazdao;

import java.util.List;

import modelo.Profesor;
import modelo.SolicitudTutoria;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudTutoriaDAO extends JpaRepository<SolicitudTutoria, Long> {

	public List<SolicitudTutoria> findByEstatusLikeAndProfesor(
			String estatusPorRevisar, Profesor profesor);
}

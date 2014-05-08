package interfazdao;

import java.util.List;

import modelo.Estudiante;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.seguridad.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEstudianteDAO extends JpaRepository<Estudiante, String> {

	@Query("Select e from Estudiante e where e.estatus = true order by e.programa asc, e.cedula asc")
	public List<Estudiante> findByEstatusTrueOrderByProgramaAsc();

	public List<Estudiante> findBySolicitudesTutorias(SolicitudTutoria solicitud);

// Metodo para buscar los estudiantes asociados a un teg
	public List<Estudiante> findByTegs(Teg Teg);
	
	public Estudiante findByUsuario(Usuario u);

	@Query("select e from Estudiante e where e.usuario=null")
	public List<Estudiante> buscarSinUsuario();

	public List<Estudiante> findByPrograma(Programa programa);

			
	
}

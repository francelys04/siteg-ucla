package interfazdao;


import java.util.List;


import modelo.Estudiante;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Usuario;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEstudianteDAO extends JpaRepository<Estudiante, String> {

	@Query("select e from Estudiante e where e.estatus=true")
	public List<Estudiante> buscarEstudiantesActivos();
	
	public List<Estudiante> findBySolicitudesTutorias(SolicitudTutoria solicitud);

	public Estudiante findByUsuario(Usuario u);
//Metodo para buscar los estudiantes asociados a un teg
public List<Estudiante> findByTegs(Teg Teg);
}


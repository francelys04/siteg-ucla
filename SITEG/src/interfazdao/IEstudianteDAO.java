package interfazdao;


import java.util.List;

import modelo.Estudiante;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEstudianteDAO extends JpaRepository<Estudiante, String> {

	@Query("select e from Estudiante e where e.estatus=true")
	public List<Estudiante> buscarEstudiantesActivos();
	
}


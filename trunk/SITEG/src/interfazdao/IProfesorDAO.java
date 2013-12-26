package interfazdao;

import java.util.List;

import modelo.Profesor;
import modelo.Programa;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProfesorDAO extends JpaRepository<Profesor, String> {

	public Profesor findByCedula(String cedula);

	public List<Profesor> findByEstatusTrue();

	public Profesor findByUsuario(Usuario u);

	@Query("select p from Profesor p where p.programa = ?1")
	public List<Profesor> buscarProfesorporPrograma(Programa programa);

}

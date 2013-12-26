package interfazdao;

import java.util.List;

import modelo.Profesor;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProfesorDAO extends JpaRepository<Profesor, String> {

	public Profesor findByCedula(String cedula);
	

	public List<Profesor> findByEstatusTrue();


	public Profesor findByUsuario(Usuario u);

}

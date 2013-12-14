package interfazdao;

import java.util.List;

import modelo.Profesor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProfesorDAO extends JpaRepository<Profesor, String> {

	public Profesor findByCedula(String cedula);

	public List<Profesor> findByEstatusTrue();

}

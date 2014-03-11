package interfazdao;

import java.util.List;

import modelo.Profesor;
import modelo.Teg;
import modelo.compuesta.Jurado;
import modelo.compuesta.id.JuradoId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IJuradoDAO extends JpaRepository<Jurado, JuradoId> {

	List<Jurado> findByTeg(Teg teg);

	List<Jurado> findByProfesor( Profesor profesor);

}

package interfazdao;

import java.util.List;

import modelo.Jurado;
import modelo.JuradoId;
import modelo.Teg;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IJuradoDAO extends JpaRepository<Jurado, JuradoId> {

	List<Jurado> findByTeg(Teg teg);

}

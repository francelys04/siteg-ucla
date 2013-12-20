package interfazdao;

import java.util.List;

import modelo.Condicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICondicionDAO extends JpaRepository<Condicion, Long> {

	@Query("select c from Condicion c where c.estatus=true")
	public List<Condicion> findByEstatusTrue();
	

}

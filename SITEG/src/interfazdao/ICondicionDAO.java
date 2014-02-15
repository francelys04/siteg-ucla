package interfazdao;

import java.util.List;

import modelo.Condicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICondicionDAO extends JpaRepository<Condicion, Long> {


	public List<Condicion> findByEstatusTrue();
	

}

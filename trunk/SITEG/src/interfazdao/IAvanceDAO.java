package interfazdao;

import java.util.List;

import modelo.Avance;
import modelo.Teg;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAvanceDAO extends JpaRepository<Avance, Long> {

	public List<Avance> findByEstatusTrue();
	
	public List<Avance> findByEstatusLikeAndTeg(
			String estatusAvanceProyecto, Teg teg);
}

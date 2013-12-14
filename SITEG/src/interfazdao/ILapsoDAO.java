package interfazdao;

import java.util.List;

import modelo.Lapso;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ILapsoDAO extends JpaRepository<Lapso, String> {

	@Query("select e from Lapso e where e.estatus=true")
	public List<Lapso> buscarLapsosActivos();

	public Lapso findById(long codigo);

	public Lapso findByNombre(String value);
	
}



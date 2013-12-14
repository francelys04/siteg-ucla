package interfazdao;


import java.util.List;

import modelo.TipoJurado;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITipoJuradoDAO extends JpaRepository<TipoJurado, Long> {

	@Query("select tj from TipoJurado tj where tj.estatus=true")
	public List<TipoJurado> buscarTipoJuradoActivos();

	public TipoJurado findById(long id);
	
}


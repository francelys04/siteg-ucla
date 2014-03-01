package interfazdao;


import java.util.List;

import modelo.TipoJurado;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITipoJuradoDAO extends JpaRepository<TipoJurado, Long> {

	public List<TipoJurado> findByEstatusTrueOrderByNombreAsc();

	public TipoJurado findByNombre(String tipojurado);
	
}


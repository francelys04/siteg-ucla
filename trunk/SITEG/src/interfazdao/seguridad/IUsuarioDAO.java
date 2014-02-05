package interfazdao.seguridad;

import java.util.List;
import modelo.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {

	
	List<Usuario> findByEstatusTrue();
	
	Usuario findByNombre(String name);
	
	
	
}

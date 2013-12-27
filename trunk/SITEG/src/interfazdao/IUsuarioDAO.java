package interfazdao;

import java.util.List;


import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {

	
	List<Usuario> findByEstatusTrue();
	
	Usuario findByNombre(String name);
	
	
	
}

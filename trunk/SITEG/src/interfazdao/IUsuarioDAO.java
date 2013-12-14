package interfazdao;

import java.util.List;

import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {

	List<Usuario> findByEstatusTrue();

	
	
}

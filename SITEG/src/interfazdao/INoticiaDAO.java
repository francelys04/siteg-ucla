package interfazdao;

import java.util.List;

import modelo.Noticia;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface INoticiaDAO extends JpaRepository<Noticia, Long> {

	@Query("select a from Noticia a where a.estatus=true")
	public List<Noticia> buscarNoticiasActivas();
	
}
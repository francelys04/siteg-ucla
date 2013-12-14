package interfazdao;

import java.util.List;

import modelo.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriaDAO extends JpaRepository<Categoria, Long> {

	public Categoria findByNombreAndEstatusTrue(String categorias);

	public List<Categoria> findByEstatusTrue();

}

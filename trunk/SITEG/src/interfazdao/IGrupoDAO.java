package interfazdao;

import java.util.List;

import modelo.Grupo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IGrupoDAO extends JpaRepository<Grupo, Long> {

	public List<Grupo> findByEstatusTrue();
}

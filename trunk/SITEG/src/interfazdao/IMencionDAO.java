package interfazdao;

import java.util.List;

import modelo.Mencion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IMencionDAO extends JpaRepository<Mencion, Long> {

	List<Mencion> findByEstatusTrue();
}

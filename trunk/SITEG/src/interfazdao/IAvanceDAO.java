package interfazdao;


import java.util.List;

import modelo.Actividad;
import modelo.Avance;
import modelo.Lapso;
import modelo.Programa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAvanceDAO extends JpaRepository<Avance, Long> {


	public List<Avance> findByEstatusTrue();

	public Avance findById(long id);
}


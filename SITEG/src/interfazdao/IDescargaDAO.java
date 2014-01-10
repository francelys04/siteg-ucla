package interfazdao;

import java.util.List;

import modelo.Actividad;
import modelo.Archivo;
import modelo.AreaInvestigacion;
import modelo.Descarga;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IDescargaDAO extends JpaRepository<Descarga, Long> {
	public List<Descarga> findByEstatusTrue();


}

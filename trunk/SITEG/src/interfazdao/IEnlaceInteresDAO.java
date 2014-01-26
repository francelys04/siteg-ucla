package interfazdao;

import java.util.List;

import modelo.Actividad;
import modelo.Archivo;
import modelo.AreaInvestigacion;
import modelo.EnlaceInteres;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEnlaceInteresDAO extends JpaRepository<EnlaceInteres, Long> {
	
	public List<EnlaceInteres> findByEstatusTrue();


}

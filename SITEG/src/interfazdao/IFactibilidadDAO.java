package interfazdao;

import java.util.List;

import modelo.Factibilidad;
import modelo.Teg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IFactibilidadDAO extends JpaRepository<Factibilidad, Long> {
	//Busca las factibilidades con estatus = FactibilidadEvaluada
	@Query("select f from Factibilidad f where f.estatus='FactibilidadEvaluada'")
	public List<Factibilidad> buscarFactibilidadActivos();

	@Query("select f from Factibilidad f where f.id=?1")
	public List<Factibilidad> BuscarProfesor(long id);
		
	public Factibilidad findByTeg(Teg teg);

}

package interfazdao;


import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Tematica;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITematicaDAO extends JpaRepository<Tematica, Long> {

	 public Tematica findByNombre(String nombre);
	//busco todos los que su estado es true	
	@Query("select e from Tematica e where e.estatus=true")
	public List<Tematica> buscarTematicasActivas();
	
	public Tematica findById(long codigo);
	
	/*
	 * Busca las tematicas por Areas*/
	public List<Tematica> findByAreaInvestigacion(AreaInvestigacion area2);
	
}



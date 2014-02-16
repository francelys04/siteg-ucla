package interfazdao;


import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Tematica;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITematicaDAO extends JpaRepository<Tematica, Long> {

	 public Tematica findByNombre(String nombre);
	//busco todos los que su estado es true	
	 public List<Tematica> findByEstatusTrueOrderByAreaInvestigacionAsc();
	
	public Tematica findById(long codigo);
	
	/*
	 * Busca las tematicas por Areas*/
	public List<Tematica> findByAreaInvestigacion(AreaInvestigacion area2);
	
    public List<Tematica> findByProfesores(Profesor profesor);
	
	public List<Tematica> findByIdNotIn(List<Long> ids);
	
	
}



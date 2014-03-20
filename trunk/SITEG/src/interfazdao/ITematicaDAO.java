package interfazdao;


import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Tematica;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITematicaDAO extends JpaRepository<Tematica, Long> {

	 public Tematica findByNombre(String nombre);
	//busco todos los que su estado es true	
	 public List<Tematica> findByEstatusTrueOrderByAreaInvestigacionAsc();	
	/*
	 * Busca las tematicas por Areas*/
	public List<Tematica> findByAreaInvestigacion(AreaInvestigacion area2);
	
    public List<Tematica> findByProfesoresOrderByAreaInvestigacionAsc(Profesor profesor);
	
	public List<Tematica> findByIdNotIn(List<Long> ids);
	
	
}



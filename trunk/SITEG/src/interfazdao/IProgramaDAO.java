package interfazdao;


import java.util.List;

import modelo.Profesor;
import modelo.Programa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProgramaDAO extends JpaRepository<Programa, Long> {


	@Query("select p from Programa p where p.estatus=true")
	public List<Programa> buscarProgramasActiva();
	
	public Programa findByNombreAndEstatusTrue(String programas);

	public Programa findByDirectorPrograma(Profesor profesor);

	@Query("select max(p.id) from Programa p where p.estatus=true")
	public Long buscarUltimoPrograma();

	public Programa findByNombreAllIgnoreCase(String nombre);

	public List<Programa> findByEstatusTrueOrderByNombreAsc();

	public List<Programa> findByEstatusFalseOrderByNombreAsc();
		
}


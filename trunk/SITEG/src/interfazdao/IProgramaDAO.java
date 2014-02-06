package interfazdao;


import java.util.List;

import modelo.Profesor;
import modelo.Programa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProgramaDAO extends JpaRepository<Programa, Long> {

	
	 public Programa findByNombre(String nombre);

	@Query("select p from Programa p where p.estatus=true")
	public List<Programa> buscarProgramasActiva();
	
	public Programa findByNombreAndEstatusTrue(String programas);

	public Programa findByDirectorPrograma(Profesor profesor);
		
}


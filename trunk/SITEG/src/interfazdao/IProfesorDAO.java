package interfazdao;

import java.util.List;


import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProfesorDAO extends JpaRepository<Profesor, String> {

	public Profesor findByCedula(String cedula);

	public List<Profesor> findByEstatusTrue();

	public Profesor findByUsuario(Usuario u);
	
	@Query("select p from Profesor p where p.usuario=null")
	public List<Profesor> buscarSinUsuario();


	@Query("select p from Profesor p where p.programa = ?1")
	public List<Profesor> buscarProfesorporPrograma(Programa programa);
	
	
	public List<Profesor> findByTegs(Teg teg);
	
	public List<Profesor> findByCedulaNotIn(List<String> cedulas);
	
	
	
	

	@Query("select p from Profesor p where p.cedula not in (select j.profesor from Jurado j where j.teg = ?1)")
	public List<Profesor> buscarProfesorEnJurado(Teg teg);

}

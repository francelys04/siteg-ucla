package interfazdao;

import java.util.List;

import modelo.Profesor;
import modelo.Teg;
import modelo.Tematica;
import modelo.seguridad.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProfesorDAO extends JpaRepository<Profesor, String> {

	public Profesor findByCedula(String cedula);

	public List<Profesor> findByEstatusTrue();

	public Profesor findByUsuario(Usuario u);
	
	@Query("select p from Profesor p where p.estatus=true and p.usuario=null")
	public List<Profesor> buscarSinUsuario();
	
	public List<Profesor> findByTegs(Teg teg);
	
	public List<Profesor> findByCedulaNotIn(List<String> cedulas);
	
	public List<Profesor> findByTematicas(Tematica tematica);
	
	@Query("select p from Profesor p where p.cedula not in (select j.profesor from Jurado j where j.teg = ?1)")
	public List<Profesor> buscarProfesorEnJurado(Teg teg);
	
	@Query("select p from Profesor p where p.estatus=true and p.cedula not in (select pr.directorPrograma from Programa pr where pr.estatus = true)")
	public List<Profesor> profesorSinPrograma();
	
	@Query("Select p from Profesor p where p.estatus = true order by p.categoria asc, p.cedula asc")
	public List<Profesor> findByEstatusTrueOrderByCategoriaAsc();

	public List<Profesor> findDistinctByCedulaIn(List<String> cedulas);

	public List<Profesor> findByEstatusFalseOrderByCategoriaAsc();

}

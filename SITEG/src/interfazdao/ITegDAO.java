package interfazdao;

import java.util.List;


import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITegDAO extends JpaRepository <Teg, Long>  {
	
	public Teg findById(long id);
	
	

    //Busca los tegs con estatus = ProyectoFactible

	@Query("select t from Teg t where t.estatus='ProyectoFactible'")
	public List<Teg> buscarProyectoFactible();

	//Evaluar Revisiones
	@Query("select t from Teg t where t.estatus='TEGRegistrado'")
	public List<Teg> buscarTegRegistrado();
	
	//Buscar tegs por profesores
	public List<Teg> findByTutor(List<Profesor> profesores);
	
	
	@Query("select t from Teg t where t.estatus='Proyecto Registrado'")
	public List<Teg> ProyectoRegistrado();	
	
	@Query("select t from Teg t where t.estatus='Solicitando Registro'")
	public List<Teg> TegSolicitandoRegistro();	

	/*Busca los teg asociados al Estudiante*/
	public List<Teg> findByEstudiantes(Estudiante estudiante);
		
	/*Busca los teg asociados al Profesor*/
	public List<Teg> findByTutor(Profesor profesor);
	

	
	
}



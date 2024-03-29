package interfazdao.seguridad;


import java.util.ArrayList;
import java.util.List;

import modelo.seguridad.Arbol;
import modelo.seguridad.Grupo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IArbolDAO extends JpaRepository<Arbol, Long> {

	
	 public Arbol findByNombre(String nombre);

	 @Query("select a from Arbol a order by a.id asc")
	public List<Arbol> buscarTodos();

	
	 @Query("select a from Arbol a where a.id = ?1 order by a.id")
	public List<Arbol> buscar(ArrayList<Long> ids);

	public List<Arbol> findByGrupos(Grupo grupo);
		
	
}

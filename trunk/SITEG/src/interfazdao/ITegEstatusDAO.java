package interfazdao;

import java.util.List;

import modelo.Teg;
import modelo.TegEstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITegEstatusDAO extends JpaRepository<TegEstatus, Long> {

	
//	@Query("select tegEstatus from TegEstatus tegEstatus where tegEstatus.nombre=?1 and tegEstatus.teg=?2")
//	public TegEstatus buscarEstatusSegunTeg(String nombre,Teg teg);
//	
	@Query("select tegEstatus from TegEstatus tegEstatus where tegEstatus.nombre=?1 and tegEstatus.teg in ?2")
	public List<TegEstatus> buscarEstatusSegunTeg(String nombre,List<Teg> teg);
	
	
	
	
}

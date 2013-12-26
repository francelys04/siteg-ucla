package interfazdao;
import java.util.List;

import modelo.ItemFactibilidad;
import modelo.ItemEvaluacion;
import modelo.Factibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
   public interface IItemFactibilidadDAO extends JpaRepository<ItemFactibilidad, String> {

	//  @Query ("select i from ItemFactibilidad i where i.factibilidad=?1")
	 //  public List<ItemFactibilidad> BuscarFactibilidad(Factibilidad factibilidad);
	   
   //Busca los item evaluados de un proyecto
	   public List<ItemFactibilidad> findByfactibilidad(Factibilidad factibilidad);
}


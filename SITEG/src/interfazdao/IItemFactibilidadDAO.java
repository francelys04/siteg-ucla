package interfazdao;
import java.util.List;
import modelo.Factibilidad;
import modelo.compuesta.ItemFactibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

   public interface IItemFactibilidadDAO extends JpaRepository<ItemFactibilidad, String> {

	//  @Query ("select i from ItemFactibilidad i where i.factibilidad=?1")
	 //  public List<ItemFactibilidad> BuscarFactibilidad(Factibilidad factibilidad);
	   
   //Busca los item evaluados de un proyecto
	   public List<ItemFactibilidad> findByfactibilidad(Factibilidad factibilidad);
}


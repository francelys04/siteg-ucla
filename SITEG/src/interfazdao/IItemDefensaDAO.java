package interfazdao;


import java.util.List;

import modelo.Defensa;
import modelo.compuesta.ItemDefensa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemDefensaDAO extends JpaRepository<ItemDefensa, String> {

	List<ItemDefensa> findByDefensa(Defensa defensa);

	

}

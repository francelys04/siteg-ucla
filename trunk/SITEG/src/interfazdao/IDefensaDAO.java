package interfazdao;

import modelo.Defensa;
import modelo.Teg;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IDefensaDAO extends JpaRepository<Defensa, Long> {

	Defensa findByTeg(Teg teg);

}

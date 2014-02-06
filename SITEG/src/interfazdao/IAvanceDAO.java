package interfazdao;

import java.util.List;

import modelo.Avance;
import modelo.Teg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAvanceDAO extends JpaRepository<Avance, Long> {

	public List<Avance> findByEstatusTrue();

	public Avance findById(long id);

	/*
	 * Busca un teg asociado a un estudiante que tengan estatus avances
	 * finalizados
	 */
	public List<Avance> findByEstatusLikeAndTeg(
			String estatusAvanceProyecto, Teg teg);
}
